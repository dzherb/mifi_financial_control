package finances;

import storage.Storable;

public abstract class TransactionCategory implements Storable {
    protected String name;

    String getName() {
        return name;
    }
}
