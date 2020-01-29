package dev.feldmann.constellation.common.repositories;

import dev.feldmann.constellation.common.Constellation;
import dev.feldmann.constellation.common.repositories.exceptions.DuplicateRepositoryName;
import dev.feldmann.constellation.common.services.Service;
import dev.feldmann.constellation.common.services.ServiceHolder;
import dev.feldmann.constellation.common.services.ServiceProvider;
import dev.feldmann.constellation.common.services.ServiceStatus;
import dev.feldmann.constellation.common.utils.ObjectLoader;
import lombok.Getter;
import lombok.SneakyThrows;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.migration.JavaMigration;
import org.jooq.DSLContext;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class Repository implements Service {
    @Getter
    private ServiceStatus status = ServiceStatus.DISABLED;

    @SneakyThrows
    @Override
    public void boot(ServiceProvider provider) {
        checkForUniqueRepositoryName(provider);
        runMigrations();
    }

    @Override
    public void start(ServiceProvider provider) {

    }

    @Override
    public void stop(ServiceProvider provider) {

    }

    @Override
    public void postStop(ServiceProvider provider) {

    }

    @Override
    public void updateStatus(ServiceStatus status) {
        this.status = status;
    }

    /**
     * Load the migrations from the repository
     */
    private JavaMigration[] loadMigrations() throws IOException, ClassNotFoundException {
        List<JavaMigration> list = null;
        list = ObjectLoader.createInstancesFromClasses(this.getClass().getPackage().getName() + ".migrations", JavaMigration.class);
        return list.toArray(new JavaMigration[0]);

    }


    /**
     * Run the migrations from loadMigrations
     */
    private void runMigrations() throws IOException, ClassNotFoundException {
        if (!useMigrations()) return;
        JavaMigration[] javaMigrations = new JavaMigration[0];
        javaMigrations = loadMigrations();
        Flyway flyway = Flyway.configure()
                .dataSource(Constellation.getInstance().getDatabase().getDataSource(getSchema()))
                .javaMigrations(javaMigrations)
                // Using one table per repository, because its possible to have a infinite amount of repositories
                // and flyway doesn't support two migration timelines in the same table
                .table("flyway_migrations_" + getRepositoryName().toLowerCase())
                // TODO Add which server ran the migration, but first is needed to define the server id
                // .installedBy("root")
                .load();
        flyway.migrate();
    }

    private void checkForUniqueRepositoryName(ServiceProvider provider) {
        List<ServiceHolder<Repository>> repositories = provider.getManager().getServicesOfType(Repository.class);
        for (ServiceHolder<Repository> repository : repositories) {
            String name = repository.getService().getRepositoryName();
            if (repository.getService() != this && name.equalsIgnoreCase(this.getRepositoryName())) {
                throw new DuplicateRepositoryName(this);
            }
        }
    }

    /**
     * @return The connection with the current schema
     */
    protected Connection getConnection() throws SQLException {
        return Constellation.getInstance().getDatabase().getConnection(getSchema());
    }

    /**
     * @return DSLContext the DSLContext pre configured with the given schema and connection pool
     */
    protected DSLContext ctx() {
        return Constellation.getInstance().getDatabase().getCtx(getSchema());
    }

    /**
     * Get the schema for the current repository
     */
    protected abstract String getSchema();

    /**
     * The repository name to create the migration tables
     * Needs to be unique across schemas
     */
    protected abstract String getRepositoryName();

    /**
     * If the repository use migrations
     */
    protected abstract boolean useMigrations();
}
