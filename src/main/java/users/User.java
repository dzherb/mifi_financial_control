package users;

import storage.Storable;

public interface User extends Storable {
    String getUsername();
    String getUUID();
    boolean checkPassword(String password);

    default String storageKey() {
        return getUUID();
    }
}
