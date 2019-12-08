package dev.feldmann.constellation.common.repositories.user.migrations;

import dev.feldmann.constellation.common.repositories.Migration;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import static org.jooq.impl.DSL.*;

public class V1__Create_users_table extends Migration {

    @Override
    protected void migrate(DSLContext context) throws Exception {
        context.createTable("users")
                .column("id", SQLDataType.BIGINTUNSIGNED.identity(true))
                .column("uuid", SQLDataType.UUID.nullable(false))
                .column("nickname", SQLDataType.VARCHAR(16).nullable(false))
                .column("created_at", SQLDataType.TIMESTAMP.nullable(false).defaultValue(DSL.now()))
                .column("last_login", SQLDataType.TIMESTAMP.nullable(true))
                .constraints(
                        constraint().primaryKey("id"),
                        constraint().unique("uuid"),
                        constraint().unique("nickname")
                ).execute();
        ;
    }
}
