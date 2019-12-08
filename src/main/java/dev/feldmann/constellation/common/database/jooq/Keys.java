/*
 * This file is generated by jOOQ.
 */
package dev.feldmann.constellation.common.database.jooq;


import dev.feldmann.constellation.common.database.jooq.tables.Users;
import dev.feldmann.constellation.common.database.jooq.tables.records.UsersRecord;

import javax.annotation.Generated;

import org.jooq.Identity;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;
import org.jooq.types.ULong;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>constellation_common</code> schema.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.12.3"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<UsersRecord, ULong> IDENTITY_USERS = Identities0.IDENTITY_USERS;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<UsersRecord> KEY_USERS_PRIMARY = UniqueKeys0.KEY_USERS_PRIMARY;
    public static final UniqueKey<UsersRecord> KEY_USERS_UUID = UniqueKeys0.KEY_USERS_UUID;
    public static final UniqueKey<UsersRecord> KEY_USERS_NICKNAME = UniqueKeys0.KEY_USERS_NICKNAME;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 {
        public static Identity<UsersRecord, ULong> IDENTITY_USERS = Internal.createIdentity(Users.USERS, Users.USERS.ID);
    }

    private static class UniqueKeys0 {
        public static final UniqueKey<UsersRecord> KEY_USERS_PRIMARY = Internal.createUniqueKey(Users.USERS, "KEY_users_PRIMARY", Users.USERS.ID);
        public static final UniqueKey<UsersRecord> KEY_USERS_UUID = Internal.createUniqueKey(Users.USERS, "KEY_users_uuid", Users.USERS.UUID);
        public static final UniqueKey<UsersRecord> KEY_USERS_NICKNAME = Internal.createUniqueKey(Users.USERS, "KEY_users_nickname", Users.USERS.NICKNAME);
    }
}