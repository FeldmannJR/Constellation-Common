package dev.feldmann.constellation.common.services;

import java.lang.reflect.Field;
import java.util.HashMap;

public class ServiceDependencyResolver {

    static HashMap<Class<? extends Service>, Inject> getDependencies(Class<? extends Service> serviceClass) {
        HashMap<Field, Inject> injectFields = getInjectFields(serviceClass);
        HashMap<Class<? extends Service>, Inject> dependencies = new HashMap<>();
        for (Field field : injectFields.keySet()) {
            dependencies.put((Class<? extends Service>) field.getType(), injectFields.get(field));
        }
        return dependencies;
    }

    static HashMap<Field, Inject> getInjectFields(Class<? extends Service> serviceClass) {
        HashMap<Field, Inject> injectFields = new HashMap<>();
        Field[] fields = serviceClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (Service.class.isAssignableFrom(field.getType())) {
                Inject inject = field.getDeclaredAnnotation(Inject.class);
                if (inject != null) {
                    injectFields.put(field, inject);
                }
            }
        }
        return injectFields;
    }
}
