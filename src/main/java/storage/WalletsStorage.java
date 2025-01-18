package storage;

import finances.Wallet;

public class WalletsStorage extends ListStorage<Wallet> {
    private static WalletsStorage instance;

    private WalletsStorage() {}

    public static WalletsStorage getInstance() {
        if (instance == null) {
            instance = new WalletsStorage();
        }
        return instance;
    }
}
