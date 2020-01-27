package dev.feldmann.constellation.common.repositories.user;

import dev.feldmann.constellation.common.services.Service;
import dev.feldmann.constellation.common.services.ServiceProvider;
import dev.feldmann.constellation.common.services.ServiceStatus;

public class TestService implements Service {
    @Override
    public void boot(ServiceProvider provider) {
        System.out.println("BOOTING");
    }

    @Override
    public void start(ServiceProvider provider) {
        System.out.println("STARTING");
    }

    @Override
    public void stop(ServiceProvider provider) {
        System.out.println("STOPPING");
    }

    @Override
    public void postStop(ServiceProvider provider) {
        System.out.println("POST STOPPING");
    }

    @Override
    public void updateStatus(ServiceStatus status) {
    }

    public void test() {
        System.out.println("Testing");
    }
}
