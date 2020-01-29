package dev.feldmann.constellation.common.services;

import dev.feldmann.constellation.common.services.exceptions.InjectDependenciesException;
import dev.feldmann.constellation.common.services.exceptions.ServiceNotFoundException;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ServiceDependencyInjector {


    static void injectServices(Service service, ServiceProvider provider) throws InjectDependenciesException, ServiceNotFoundException {
        HashMap<Field, Inject> injectFields = ServiceDependencyResolver.getInjectFields(service.getClass());
        for (Map.Entry<Field, Inject> entry : injectFields.entrySet()) {
            Field field = entry.getKey();

            Service inject = provider.getService((Class<? extends Service>) field.getType(), entry.getValue().external());
            if (inject != null) {
                try {
                    field.set(service, inject);
                } catch (IllegalAccessException e) {
                    throw new InjectDependenciesException(e, service);
                }
            } else {
                throw new ServiceNotFoundException(null, service, (Class<? extends Service>) field.getType());
            }
        }
    }
}
