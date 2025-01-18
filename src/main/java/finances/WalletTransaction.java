package finances;

import storage.TransactionsStorage;

public class WalletTransaction extends Transaction {
    private final String id = String.valueOf(TransactionsStorage.getInstance().all().size() + 1);

    private WalletTransaction(Wallet wallet, TransactionType type, TransactionCategory category, Money amount, String description) {
        this.bank = wallet;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.description = description;
    }

    public static Transaction commit(Wallet wallet, TransactionType type, TransactionCategory category, Money amount, String description) {
        Transaction transaction = new WalletTransaction(wallet, type, category, amount, description);
        TransactionsStorage.getInstance().add(transaction);
        return transaction;
    }

    @Override
    public String storageKey() {
        return id;
    }
}
