package dev.feldmann.constellation.common;

import dev.feldmann.constellation.common.services.ServiceProvider;

import java.util.ArrayList;
import java.util.List;

public class ServiceManager {

    private List<ServiceProvider> providers = new ArrayList<>();

    /**
     * Starting the all services in the given provider
     *
     * @param provider The provider
     */
    public void startProvider(ServiceProvider provider) {
        provider.startAllServices();
        providers.add(provider);
    }

    /**
     * Stopping all services in the given provider
     *
     * @param provider The provider
     */
    public void stopProvider(ServiceProvider provider) {
        provider.stopAllServices();
        providers.remove(provider);
    }
}
