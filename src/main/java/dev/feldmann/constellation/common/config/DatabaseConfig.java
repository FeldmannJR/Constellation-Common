package dev.feldmann.constellation.common.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.SQLDialect;


@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class DatabaseConfig extends BaseConfig {
    private String username;
    private String password;
    private String host;
    private String port;
    private String defaultSchema;
    private SQLDialect dialect;
    private String driverClass;

    @Override
    public void load() {
        username = env("DATABASE_USER", "root");
        password = env("DATABASE_PASSWORD", "senha123");
        host = env("DATABASE_HOST", "localhost");
        port = env("DATABASE_PORT", "3306");
        defaultSchema = env("DATABASE_SCHEMA", "constellation_common");
        dialect = SQLDialect.MARIADB;
    }

    public String buildURL(String schema) {
        return "jdbc:mysql://" + getHost() + ":" + getPort() + "/" + schema + "?useSSL=false";
    }
}
