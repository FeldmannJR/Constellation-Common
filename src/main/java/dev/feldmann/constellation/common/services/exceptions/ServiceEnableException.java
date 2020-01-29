package dev.feldmann.constellation.common.services.exceptions;

import dev.feldmann.constellation.common.services.Service;
import dev.feldmann.constellation.common.services.ServiceStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class ServiceEnableException extends ServiceException {

    @Getter
    private ServiceStatus phase;

    public ServiceEnableException(Throwable cause, Service service, ServiceStatus phase) {
        super(cause, service);
        this.phase = phase;
    }

    @Override
    public String getMessage() {
        return "Exception in booting at " + getService().getClass().getName() + " !";
    }
}
