package dev.feldmann.constellation.common.services;

public class InjectDependenciesException extends Exception {
    public InjectDependenciesException(Throwable cause) {
        super(cause);
    }

    public InjectDependenciesException() {
    }

    public InjectDependenciesException(String message) {
        super(message);
    }
}
