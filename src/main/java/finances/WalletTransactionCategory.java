package finances;

public class WalletTransactionCategory extends TransactionCategory {
    public WalletTransactionCategory(String name) {
        this.name = name;
    }

    @Override
    public String storageKey() {
        return name;
    }
}
