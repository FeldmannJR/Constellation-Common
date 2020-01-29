package dev.feldmann.constellation.common.services;

public enum ServiceStatus {
    DISABLED,
    BOOTING,
    STARTING,
    RUNNING,
    STOPPING,
    POST_STOPPING,
    STOPPED,
    FAILED
}
