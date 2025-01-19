package storage;

import finances.SpendingLimit;

public class SpendingLimitsStorage extends ListStorage<SpendingLimit> {
    private static SpendingLimitsStorage instance;

    private SpendingLimitsStorage() {}

    public static SpendingLimitsStorage getInstance() {
        if (instance == null) {
            instance = new SpendingLimitsStorage();
        }
        return instance;
    }

    @Override
    public void updateInstance(Storage<? extends Storable> storage) {
        instance.items = ((SpendingLimitsStorage) storage).items;
    }
}
