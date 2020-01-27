package dev.feldmann.constellation.common;

import dev.feldmann.constellation.common.database.Database;
import dev.feldmann.constellation.common.config.Config;
import dev.feldmann.constellation.common.repositories.user.TestService;
import dev.feldmann.constellation.common.repositories.user.UserRepository;
import dev.feldmann.constellation.common.services.ServiceManager;
import dev.feldmann.constellation.common.services.ServiceNotFoundException;
import dev.feldmann.constellation.common.services.ServiceProvider;
import lombok.Getter;

public class Constellation {

    public static boolean DEBUG = true;

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
        try {
            registerProviders();
        } catch (ServiceNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void registerProviders() throws ServiceNotFoundException {
        commonServiceProvider = serviceManager.createProvider();
        commonServiceProvider.registerService(UserRepository.class, new UserRepository());
        commonServiceProvider.registerService(TestService.class, new TestService());
        commonServiceProvider.enable();
    }

    public void disable() {
        try {
            commonServiceProvider.disable();
            serviceManager.removeProvider(commonServiceProvider);
        } catch (ServiceNotFoundException e) {
            e.printStackTrace();
        }
        commonServiceProvider = null;
        serviceManager = null;
    }

}
