package dev.feldmann.constellation.common.services;

public interface Service {
    /**
     * Called first on life-cycle, maybe all repositories has not been booted
     */
    public void boot(ServiceProvider provider);

    /**
     * Called after all repositories have been booted
     */
    public void start(ServiceProvider provider);

    /**
     * Called first on life-cycle, maybe all repositories has not been booted
     */
    public void stop(ServiceProvider provider);

    /**
     * Called first on life-cycle, maybe all repositories has not been booted
     */
    public void postStop(ServiceProvider provider);

    /**
     * Set the status for this service, this is called from the service provider
     *
     * @param status Current status
     */
    public void updateStatus(ServiceStatus status);


}
