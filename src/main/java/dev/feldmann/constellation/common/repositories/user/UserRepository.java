package dev.feldmann.constellation.common.repositories.user;

import static dev.feldmann.constellation.common.database.jooq.Tables.*;

import dev.feldmann.constellation.common.database.jooq.tables.records.UsersRecord;
import dev.feldmann.constellation.common.repositories.Repository;
import dev.feldmann.constellation.common.services.Inject;
import dev.feldmann.constellation.common.services.ServiceStatus;
import lombok.Getter;
import lombok.NonNull;

import java.util.Optional;
import java.util.UUID;

public class UserRepository extends Repository {


    @Inject
    TestService testService;

    @Override
    public String getSchema() {
        return "constellation_common";
    }

    @Override
    protected String getRepositoryName() {
        return "users";
    }

    @Override
    public boolean useMigrations() {
        return true;
    }


    public void testing() {
        testService.test();
    }

    /**
     * Load Player by UUID
     *
     * @param uuid The player UUID
     */
    public Optional<User> load(UUID uuid) {
        UsersRecord user = ctx().selectFrom(USERS)
                .where(USERS.UUID.eq(uuid.toString())).fetchAny();
        if (user != null) {
            return Optional.of(new User(user));
        }
        return Optional.empty();
    }

    /**
     * Register a new user
     */
    public User registerUser(@NonNull UUID uuid, @NonNull String nickname) {
        UsersRecord users = ctx().newRecord(USERS);
        users.setUuid(uuid.toString());
        users.setNickname(nickname);
        users.store();
        return new User(users);
    }


}
