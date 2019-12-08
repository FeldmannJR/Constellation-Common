package dev.feldmann.constellation.common.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.feldmann.constellation.common.config.DatabaseConfig;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class responsible for creating database connections
 */
@RequiredArgsConstructor
public class Database {

    @NonNull
    private DatabaseConfig config;

    private ConcurrentHashMap<String, HikariDataSource> dataSources = new ConcurrentHashMap<>();

    public DSLContext getCtx() {
        return getCtx(config.getDefaultSchema());
    }

    public DSLContext getCtx(String schema) {
        return DSL.using(getDataSource(schema), config.getDialect());
    }

    public Connection getConnection() throws SQLException {
        return getConnection(config.getDefaultSchema());
    }

    public Connection getConnection(String schema) throws SQLException {
        return getDataSource(schema).getConnection();
    }

    /**
     * Get a datasource from cache or create a new one if doesn't exist
     *
     * @param schema The schema used for the creation of the data source
     * @return A datasource for the given schema
     */
    public HikariDataSource getDataSource(String schema) {
        if (!dataSources.containsKey(schema)) {
            dataSources.put(schema, createDataSource(schema));
        }
        return dataSources.get(schema);
    }

    /**
     * Create a datasource for the given schema
     *
     * @param schema The schema used for the creation of the data source
     * @return A new data source
     */
    private HikariDataSource createDataSource(String schema) {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl("jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + schema+"?useSSL=false");
        hikariConfig.setUsername(config.getUsername());
        hikariConfig.setPassword(config.getPassword());
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");
        hikariConfig.setMaximumPoolSize(15);
        hikariConfig.setIdleTimeout(30000);
        hikariConfig.setCatalog(schema);
        hikariConfig.addDataSourceProperty("characterEncoding", "utf8");
        hikariConfig.addDataSourceProperty("useUnicode", "true");
        hikariConfig.setLeakDetectionThreshold(15000);
        return new HikariDataSource(hikariConfig);
    }

}
