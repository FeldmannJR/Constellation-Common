package dev.feldmann.constellation.common;

import dev.feldmann.constellation.common.database.Database;
import dev.feldmann.constellation.common.config.Config;
import dev.feldmann.constellation.common.repositories.user.TestService;
import dev.feldmann.constellation.common.repositories.user.UserRepository;
import dev.feldmann.constellation.common.services.ServiceManager;
import dev.feldmann.constellation.common.services.exceptions.ServiceEnableException;
import dev.feldmann.constellation.common.services.exceptions.ServiceException;
import dev.feldmann.constellation.common.services.exceptions.ServiceNotFoundException;
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
    }

    public void enable() {
        info("Enabling");
        this.config = new Config();
        info("Loading config");
        config.load();
        this.serviceManager = new ServiceManager();
        this.database = new Database(config.getDatabase());
        try {
            registerProviders();
        } catch (ServiceException e) {
            if (e instanceof ServiceEnableException) {
                //TODO shutdown the server
            }
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void disable() {
        info("Disabling");
        try {
            commonServiceProvider.disable();
            serviceManager.removeProvider(commonServiceProvider);
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        commonServiceProvider = null;
        serviceManager = null;
    }

    private void registerProviders() throws ServiceException {
        commonServiceProvider = serviceManager.createProvider();
        commonServiceProvider.registerService(UserRepository.class, new UserRepository());
        commonServiceProvider.registerService(TestService.class, new TestService());
        commonServiceProvider.enable();
    }


    public void info(String info) {
        //TODO Proper logging
        System.out.println("[Constellation-Common] " + info);
    }

}
