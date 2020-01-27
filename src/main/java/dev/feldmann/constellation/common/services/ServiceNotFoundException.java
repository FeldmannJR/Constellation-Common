package dev.feldmann.constellation.common.services;

public class ServiceNotFoundException extends Exception {
    public ServiceNotFoundException(Throwable cause) {
        super(cause);
    }

    public ServiceNotFoundException() {
    }

    public ServiceNotFoundException(String message) {
        super(message);
    }
}
