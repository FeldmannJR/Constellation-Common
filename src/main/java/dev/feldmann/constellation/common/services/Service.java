package dev.feldmann.constellation.common.services;

public interface Service {
    /**
     * Called first on life-cycle, maybe all repositories has not been booted
     */
    public void boot();

    /**
     * Called after all repositories have been booted
     */
    public void start();

    /**
     * Called first on life-cycle, maybe all repositories has not been booted
     */
    public void stop();

    /**
     * Called first on life-cycle, maybe all repositories has not been booted
     */
    public void afterStop();

    /**
     * Set the status for this service, this is called from the service provider
     *
     * @param status Current status
     */
    public void setStatus(ServiceStatus status);

}
