package dev.feldmann.constellation.common;

import dev.feldmann.constellation.common.database.Database;
import dev.feldmann.constellation.common.config.Config;
import dev.feldmann.constellation.common.repositories.user.UserRepository;
import dev.feldmann.constellation.common.services.ServiceProvider;
import lombok.Getter;

public class Constellation {

    @Getter
    public static Constellation instance;
    /**
     * Responsible for creating database connections
     */
    @Getter
    private Database database;
    @Getter
    private Config config;
    @Getter
    private ServiceManager serviceManager;
    @Getter
    private ServiceProvider commonServiceProvider;

    public Constellation() {
        instance = this;
        this.config = new Config();
        this.serviceManager = new ServiceManager();
        this.database = new Database(config.getDatabase());
        registerProviders();
    }

    private void registerProviders() {
        commonServiceProvider = new ServiceProvider();
        commonServiceProvider.registerService(UserRepository.class, new UserRepository());
        serviceManager.startProvider(commonServiceProvider);
    }

}
