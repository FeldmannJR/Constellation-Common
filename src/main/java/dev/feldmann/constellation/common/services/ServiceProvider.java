package dev.feldmann.constellation.common.services;

import lombok.Getter;
import lombok.NonNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;


public class ServiceProvider {

    @NonNull
    private ServiceManager manager;
    @Getter
    @NonNull
    private ServiceStatus status;

    public ServiceProvider(@NonNull ServiceManager manager) {
        this.manager = manager;
        this.status = ServiceStatus.DISABLED;
    }

    @Getter
    private ConcurrentHashMap<Class<? extends Service>, ServiceHolder<? extends Service>> services = new ConcurrentHashMap<>();


    public <T extends Service> void registerService(Class<? extends Service> serviceClass, T service) {
        if (this.status != ServiceStatus.DISABLED) {
            throw new RuntimeException("Trying to register service after provider boot!");
        }
        if (!service.getClass().isAssignableFrom(serviceClass)) {
            throw new ClassCastException(service.getClass().getName() + " cannot be cast to " + serviceClass.getName() + "!");
        }
        if (this.manager.isServiceAlreadyRegistered(service)) {
            throw new RuntimeException("The service " + service.getClass().getName() + " is already registered!");
        }
        services.put(serviceClass, new ServiceHolder(service, ServiceStatus.DISABLED));
    }

    public <T extends Service> T get(@NonNull Class<T> serviceClass) {
        return getService(serviceClass);
    }

    public <T extends Service> T getService(@NonNull Class<T> serviceClass) {
        return getService(serviceClass, false);
    }

    public <T extends Service> T getService(@NonNull Class<T> serviceClass, boolean allowExternal) {
        ServiceHolder<T> serviceHolder = getServiceHolder(serviceClass, allowExternal);
        if (serviceHolder != null) {
            return serviceHolder.getService();
        }
        return null;
    }

    public <T extends Service> ServiceHolder<T> getServiceHolder(@NonNull Class<T> serviceClass, boolean allowExternal) {
        if (services.containsKey(serviceClass)) {
            return (ServiceHolder<T>) services.get(serviceClass);
        }
        if (allowExternal)
            return manager.getServiceHolder(serviceClass);
        return null;
    }


    public boolean hasService(Service service) {
        return services.values().stream().anyMatch(serviceHolder -> serviceHolder.getService() == service);
    }


    private <T extends Service> void forEachService(ServiceHolder<T> serviceHolder, ServiceStatus status, Consumer<ServiceHolder<? extends Service>> action) throws ServiceNotFoundException {
        // If the service already is in the given state just ignore
        if (serviceHolder.getStatus() == status) {
            return;
        }
        T service = serviceHolder.getService();
        // If the service is owned by another provider just ignore the starting procedure
        if (!hasService(service)) {
            return;
        }
        HashMap<Class<? extends Service>, Inject> dependencies = ServiceDependencyResolver.getDependencies(service.getClass());
        for (Class<? extends Service> dependency : dependencies.keySet()) {
            Inject inject = dependencies.get(dependency);
            ServiceHolder<? extends Service> dependencyService = getServiceHolder(dependency, inject.external());
            if (dependencyService == null && inject.required()) {
                throw new ServiceNotFoundException("Cannot find service " + dependency.getName());
            }
            if (dependencyService != null)
                forEachService(dependencyService, status, action);
        }
        serviceHolder.setStatus(status);
        action.accept(serviceHolder);
    }


    private void boot() throws ServiceNotFoundException {
        this.status = ServiceStatus.BOOTING;
        for (ServiceHolder<? extends Service> serviceIt : this.services.values()) {
            forEachService(serviceIt, ServiceStatus.BOOTING, serviceHolder -> {
                Service service = serviceHolder.getService();
                try {
                    ServiceDependencyInjector.injectServices(service, this);
                } catch (InjectDependenciesException e) {
                    e.printStackTrace();
                }
                long start = System.currentTimeMillis();
                service.boot(this);
                serviceHolder.setBootTime(System.currentTimeMillis() - start);
            });
        }
    }

    private void start() throws ServiceNotFoundException {
        this.status = ServiceStatus.STARTING;
        for (ServiceHolder<? extends Service> serviceIt : this.services.values()) {
            forEachService(serviceIt, ServiceStatus.STARTING, serviceHolder -> {
                Service service = serviceHolder.getService();
                long start = System.currentTimeMillis();
                service.start(this);
                serviceHolder.setStatus(ServiceStatus.RUNNING);
                serviceHolder.setStartTime(System.currentTimeMillis() - start);
            });
        }
    }


    private void stop() throws ServiceNotFoundException {
        this.status = ServiceStatus.STOPPING;
        for (ServiceHolder<? extends Service> serviceIt : this.services.values()) {
            forEachService(serviceIt, ServiceStatus.STOPPING, serviceHolder -> {
                Service service = serviceHolder.getService();
                long start = System.currentTimeMillis();
                service.stop(this);
                serviceHolder.setStopTime(System.currentTimeMillis() - start);
            });
        }
    }

    private void postStop() throws ServiceNotFoundException {
        this.status = ServiceStatus.POST_STOPPING;
        for (ServiceHolder<? extends Service> serviceIt : this.services.values()) {
            forEachService(serviceIt, ServiceStatus.POST_STOPPING, serviceHolder -> {
                Service service = serviceHolder.getService();
                service.postStop(this);
                long start = System.currentTimeMillis();
                serviceHolder.setPostStopTime(System.currentTimeMillis() - start);
                serviceHolder.setStatus(ServiceStatus.STOPPED);
            });
        }
    }

    public void enable() throws ServiceNotFoundException {
        if (this.status == ServiceStatus.DISABLED) {
            boot();
            start();
        }
    }

    public void disable() throws ServiceNotFoundException {
        if (this.status == ServiceStatus.RUNNING) {
            stop();
            postStop();
        }
    }


}
