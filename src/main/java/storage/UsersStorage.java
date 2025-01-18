package storage;

import users.User;

public class UsersStorage extends ListStorage<User> {
    private static UsersStorage instance;
    private UsersStorage() {
        instance = this;
    }

    public static UsersStorage getInstance() {
        if (instance == null) {
            instance = new UsersStorage();
        }
        return instance;
    }
}
