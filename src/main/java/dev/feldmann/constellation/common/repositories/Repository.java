package dev.feldmann.constellation.common.repositories;

import dev.feldmann.constellation.common.Constellation;
import dev.feldmann.constellation.common.services.Service;
import dev.feldmann.constellation.common.services.ServiceStatus;
import dev.feldmann.constellation.common.utils.ObjectLoader;
import lombok.Getter;
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

    @Override
    public void boot() {
        runMigrations();
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void afterStop() {

    }

    @Override
    public void setStatus(ServiceStatus status) {
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
    private boolean runMigrations() {
        if (!useMigrations()) return true;
        JavaMigration[] javaMigrations = new JavaMigration[0];
        try {
            javaMigrations = loadMigrations();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        Flyway flyway = Flyway.configure()
                .dataSource(Constellation.getInstance().getDatabase().getDataSource(getSchema()))
                .javaMigrations(javaMigrations)
                .load();
        flyway.migrate();
        return true;
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
     * If the repository use migrations
     */
    protected abstract boolean useMigrations();
}
