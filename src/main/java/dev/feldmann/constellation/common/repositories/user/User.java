package dev.feldmann.constellation.common.repositories.user;

//import dev.feldmann.constellation.common.database.jooq.tables.records.UsersRecord;
import lombok.Getter;

public class User {

    @Getter
    private UsersRecord record;

    public User(UsersRecord record) {
        this.record = record;
    }

    private class UsersRecord {
    }
}
