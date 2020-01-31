package dev.feldmann.constellation.common.database;

import dev.feldmann.constellation.common.config.Config;
import dev.feldmann.constellation.common.config.DatabaseConfig;
import dev.feldmann.jooqConfigDiscovery.jooq.JOOQGenerator;
import org.jooq.meta.jaxb.Generator;

public abstract class ConstellationJooqGenerator extends JOOQGenerator {

    private DatabaseConfig config = null;

    public DatabaseConfig getConfig() {
        if(config==null){
            Config coConfig = new Config();
            coConfig.load();
            config = coConfig.getDatabase();
        }
        return config;
    }

    @Override
    protected String getDatabaseURL(String s) {
        return getConfig().buildURL(s);
    }

    @Override
    protected String getDatabaseUser() {
        return getConfig().getUsername();
    }

    @Override
    protected String getDatabasePassword() {
        return getConfig().getPassword();
    }


}
