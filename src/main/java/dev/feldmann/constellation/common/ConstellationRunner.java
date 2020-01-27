package dev.feldmann.constellation.common;

import dev.feldmann.constellation.common.repositories.user.UserRepository;

public class ConstellationRunner {

    public static void main(String[] args) {
        Constellation constellation = new Constellation();

        UserRepository service = constellation
                .getServiceManager()
                .getService(UserRepository.class);

        service.testing();

        constellation.disable();
    }
}
