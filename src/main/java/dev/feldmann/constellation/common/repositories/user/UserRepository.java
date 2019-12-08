package dev.feldmann.constellation.common.repositories.user;

import dev.feldmann.constellation.common.repositories.Repository;
import dev.feldmann.constellation.common.services.ServiceStatus;
import lombok.Getter;

public class UserRepository implements Repository {
    @Getter
    private ServiceStatus status = ServiceStatus.DISABLED;

    @Override
    public String getSchema() {
        return "common";
    }

    @Override
    public boolean useMigrations() {
        return true;
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void afterStop() {

    }

    @Override
    public void setStatus(ServiceStatus status) {
        this.status = status;
    }
}
