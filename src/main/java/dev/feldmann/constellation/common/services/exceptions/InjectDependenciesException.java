package dev.feldmann.constellation.common.services.exceptions;

import dev.feldmann.constellation.common.services.Service;

public class InjectDependenciesException extends ServiceException {

    public InjectDependenciesException(Throwable cause, Service service) {
        super(cause, service);
    }
}
