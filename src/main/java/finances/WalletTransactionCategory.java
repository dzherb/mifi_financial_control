package finances;

import utils.UUIDGenerator;

import java.util.Objects;

public class WalletTransactionCategory extends TransactionCategory {
    private String uuid = UUIDGenerator.generate();

    public WalletTransactionCategory(String name) {
        this.name = name;
    }

    @Override
    public String storageKey() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WalletTransactionCategory that)) return false;
        return Objects.equals(uuid, that.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }

    @Override
    public String toString() {
        return name;
    }
}
