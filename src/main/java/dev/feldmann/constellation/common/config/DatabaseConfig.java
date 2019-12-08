package dev.feldmann.constellation.common.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jooq.SQLDialect;


@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class DatabaseConfig {
    private String username = Env.get("DATABASE_USER", "root");
    private String password = Env.get("DATABASE_PASSWORD", "senha123");
    private String host = Env.get("DATABASE_HOST", "localhost");
    private String port = Env.get("DATABASE_PORT", "3306");
    private String defaultSchema = Env.get("DATABASE_SCHEMA", "common");
    private SQLDialect dialect = SQLDialect.MARIADB;
}
