package dev.feldmann.constellation.common.services;

import lombok.NonNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.security.Provider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ServiceManager {

    private List<ServiceProvider> providers = new ArrayList<>();

    public void registerProvider(ServiceProvider provider) {
        providers.add(provider);
    }


    public ServiceProvider createProvider() {
        ServiceProvider provider = new ServiceProvider(this);
        registerProvider(provider);
        return provider;
    }

    public boolean removeProvider(@NonNull ServiceProvider provider) {
        // You can only remove providers that already are stopped
        if (provider.getStatus() != ServiceStatus.STOPPED && provider.getStatus() != ServiceStatus.DISABLED) {
            return false;
        }
        if (providers.contains(provider)) {
            providers.remove(provider);
            return true;
        }
        return false;
    }

    public boolean isServiceAlreadyRegistered(Service service) {
        for (ServiceProvider provider : providers) {
            if (provider.hasService(service)) {
                return true;
            }
        }
        return false;
    }


    public <T extends Service> List<ServiceHolder<T>> getServicesOfType(Class<T> type) {
        List<ServiceHolder<T>> services = new ArrayList<>();
        for (ServiceProvider provider : providers) {
            Collection<ServiceHolder<? extends Service>> values = provider.getServices().values();
            for (ServiceHolder<? extends Service> serviceHolder : values) {
                if(type.isAssignableFrom(serviceHolder.getService().getClass())){
                    services.add((ServiceHolder<T>) serviceHolder);
                }
            }
        }
        return services;
    }

    public <T extends Service> T getService(Class<T> serviceClass) {
        ServiceHolder<T> holder = getServiceHolder(serviceClass);
        if (holder != null)
            return holder.getService();
        return null;
    }

    public <T extends Service> ServiceHolder<T> getServiceHolder(Class<T> serviceClass) {
        for (ServiceProvider provider : providers) {
            ServiceHolder<T> serviceHolder = provider.getServiceHolder(serviceClass, false);
            if (serviceHolder != null) {
                return serviceHolder;
            }
        }
        return null;
    }
}
