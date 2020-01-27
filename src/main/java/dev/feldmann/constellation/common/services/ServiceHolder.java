package dev.feldmann.constellation.common.services;

import lombok.*;

public class ServiceHolder<T extends Service> {

    /**
     * The service instance
     */
    @Getter
    @NonNull
    private T service;
    @Getter
    @NonNull
    private ServiceStatus status;


    @Setter
    @Getter
    private long bootTime = -1;

    @Setter
    @Getter
    private long startTime = -1;

    @Setter
    @Getter
    private long stopTime = -1;

    @Setter
    @Getter
    private long postStopTime = -1;



    public ServiceHolder(@NonNull T service, @NonNull ServiceStatus status) {
        this.service = service;
        this.status = status;
    }

     void setStatus(ServiceStatus status) {
        this.status = status;
        this.service.updateStatus(status);
    }

}
