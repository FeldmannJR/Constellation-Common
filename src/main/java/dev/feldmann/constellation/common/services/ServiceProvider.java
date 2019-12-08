package dev.feldmann.constellation.common.services;

import lombok.Getter;
import lombok.NonNull;

import java.util.concurrent.ConcurrentHashMap;

public class ServiceProvider {
    @Getter
    private ConcurrentHashMap<Class<? extends Service>, Service> services = new ConcurrentHashMap<>();

    public <T extends Service> void registerService(Class<? extends Service> serviceClass, T service) {
        if (!service.getClass().isAssignableFrom(serviceClass)) {
            throw new ClassCastException(service.getClass().getName() + " cannot be cast to " + serviceClass.getName() + "!");
        }
        services.put(serviceClass, service);
    }

    public <T extends Service> T get(@NonNull Class<T> serviceClass) {
        return getService(serviceClass);
    }

    public <T extends Service> T getService(@NonNull Class<T> serviceClass) {
        if (!services.containsKey(serviceClass)) {
            return (T) services.get(serviceClass);
        }
        return null;
    }

    public void startAllServices() {
        for (Service service : this.services.values()) {
            service.setStatus(ServiceStatus.BOOTING);
            service.boot();
        }
        for (Service service : this.services.values()) {
            service.setStatus(ServiceStatus.STARTING);
            service.start();
            service.setStatus(ServiceStatus.RUNNING);
        }

    }

    public void stopAllServices() {
        for (Service service : this.services.values()) {
            service.setStatus(ServiceStatus.STOPPING);
            service.stop();
        }
        for (Service service : this.services.values()) {
            service.afterStop();
            service.setStatus(ServiceStatus.DISABLED);
        }
    }

}
