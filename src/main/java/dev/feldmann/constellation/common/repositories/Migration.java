package dev.feldmann.constellation.common.repositories;

import org.flywaydb.core.api.MigrationVersion;
import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;

public abstract class Migration extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        this.migrate(DSL.using(context.getConnection()));
    }

    protected abstract void migrate(DSLContext context) throws Exception;

}
