package dev.feldmann.constellation.common.repositories.user;

import dev.feldmann.constellation.common.repositories.Repository;
import dev.feldmann.constellation.common.services.ServiceStatus;
import lombok.Getter;

public class UserRepository extends Repository {

    @Override
    public String getSchema() {
        return "common";
    }

    @Override
    public boolean useMigrations() {
        return true;
    }
}
