package dev.feldmann.constellation.common.services.exceptions;

import dev.feldmann.constellation.common.services.Service;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class ServiceNotFoundException extends ServiceException {

    @Getter
    private Class<? extends Service> required;

    public ServiceNotFoundException(Throwable cause, Service service, Class<? extends Service> required) {
        super(cause, service);
        this.required = required;
    }

    @Override
    public String getMessage() {
        return "Cannot find " + required.getName() + " in the service container!";
    }
}
