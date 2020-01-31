package dev.feldmann.constellation.common.repositories.user;


import dev.feldmann.constellation.common.database.ConstellationJooqGenerator;
import dev.feldmann.jooqConfigDiscovery.jooq.EnableJOOQGenerator;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.Generator;

@EnableJOOQGenerator
public class JOOQGenerator extends ConstellationJooqGenerator {
    @Override
    public Generator getGenerator() {

        return new Generator().withDatabase(
                createDatabase()
                        .withOutputCatalogToDefault(true)
                        .withOutputSchemaToDefault(true)
                        .withIncludes("users")

        ).withTarget(createTarget());
    }

    @Override
    public String getSchema() {
        return "constellation_common";
    }
}
