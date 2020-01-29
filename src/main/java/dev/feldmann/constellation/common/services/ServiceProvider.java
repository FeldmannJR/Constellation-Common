package dev.feldmann.constellation.common.services;

import dev.feldmann.constellation.common.services.exceptions.InjectDependenciesException;
import dev.feldmann.constellation.common.services.exceptions.ServiceEnableException;
import dev.feldmann.constellation.common.services.exceptions.ServiceException;
import dev.feldmann.constellation.common.services.exceptions.ServiceNotFoundException;
import lombok.Getter;
import lombok.NonNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class ServiceProvider {

    @NonNull
    @Getter
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


    private <T extends Service> void addServicesToListByDependency(ServiceHolder<T> serviceHolder, List<ServiceHolder<? extends Service>> toAddList) throws ServiceNotFoundException {
        // If the service already in the load list just ignore
        if (toAddList.contains(serviceHolder)) {
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
                throw new ServiceNotFoundException(null, service, dependency);
            }
            if (dependencyService != null)
                addServicesToListByDependency(dependencyService, toAddList);
        }
        toAddList.add(serviceHolder);
    }

    private List<ServiceHolder<? extends Service>> getServicesOrderedByDependency() throws ServiceNotFoundException {
        List<ServiceHolder<? extends Service>> list = new ArrayList<>();
        for (ServiceHolder<? extends Service> serviceIt : this.services.values()) {
            addServicesToListByDependency(serviceIt, list);
        }
        return list;
    }

    private void boot() throws ServiceException {
        this.status = ServiceStatus.BOOTING;
        for (ServiceHolder<? extends Service> serviceHolder : getServicesOrderedByDependency()) {
            Service service = serviceHolder.getService();
            try {
                ServiceDependencyInjector.injectServices(service, this);
            } catch (InjectDependenciesException e) {
                e.printStackTrace();
            }
            long start = System.currentTimeMillis();
            serviceHolder.setStatus(ServiceStatus.BOOTING);
            try {
                service.boot(this);
            } catch (Exception ex) {
                this.status = ServiceStatus.FAILED;
                serviceHolder.setStatus(ServiceStatus.FAILED);
                throw new ServiceEnableException(ex, service, ServiceStatus.BOOTING);
            }
            serviceHolder.setBootDuration(System.currentTimeMillis() - start);
        }
    }

    private void start() throws ServiceException {
        this.status = ServiceStatus.STARTING;
        for (ServiceHolder<? extends Service> serviceHolder : getServicesOrderedByDependency()) {
            Service service = serviceHolder.getService();
            long start = System.currentTimeMillis();
            serviceHolder.setStatus(ServiceStatus.STARTING);
            try {
                service.start(this);
            } catch (Exception ex) {
                this.status = ServiceStatus.FAILED;
                serviceHolder.setStatus(ServiceStatus.FAILED);
                throw new ServiceEnableException(ex, service, ServiceStatus.STARTING);
            }
            serviceHolder.setStatus(ServiceStatus.RUNNING);
            serviceHolder.setStartDuration(System.currentTimeMillis() - start);
        }
    }


    private void stop() throws ServiceException {
        this.status = ServiceStatus.STOPPING;
        for (ServiceHolder<? extends Service> serviceHolder : getServicesOrderedByDependency()) {
            Service service = serviceHolder.getService();
            long start = System.currentTimeMillis();
            serviceHolder.setStatus(ServiceStatus.STOPPING);
            service.stop(this);
            serviceHolder.setStopDuration(System.currentTimeMillis() - start);
        }
    }

    private void postStop() throws ServiceException {
        this.status = ServiceStatus.POST_STOPPING;
        for (ServiceHolder<? extends Service> serviceHolder : getServicesOrderedByDependency()) {
            Service service = serviceHolder.getService();
            long start = System.currentTimeMillis();
            serviceHolder.setStatus(ServiceStatus.POST_STOPPING);
            service.postStop(this);
            serviceHolder.setPostStopDuration(System.currentTimeMillis() - start);
            serviceHolder.setStatus(ServiceStatus.STOPPED);
        }
        this.status = ServiceStatus.STOPPED;
    }

    public void enable() throws ServiceException {
        if (this.status == ServiceStatus.DISABLED) {
            boot();
            start();
        }
    }

    public void disable() throws ServiceException {
        if (this.status == ServiceStatus.RUNNING) {
            stop();
            postStop();
        }
    }


}
