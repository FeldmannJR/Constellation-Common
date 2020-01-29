package dev.feldmann.constellation.common.services.exceptions;

import dev.feldmann.constellation.common.services.Service;
import lombok.Getter;

public abstract class ServiceException extends Exception {

    @Getter
    private Service service;

    public ServiceException(Throwable cause, Service service) {
        super(cause);
        this.service = service;
    }

    @Override
    public String getMessage() {
        return "Exception with service " + getService().getClass().getName() + "!";
    }

}
