package finances;

import storage.TransactionsStorage;
import utils.UUIDGenerator;

public class WalletTransaction extends Transaction {
    private final String uuid = UUIDGenerator.generate();

    private WalletTransaction(Wallet wallet, TransactionType type, TransactionCategory category, Money amount, String description) {
        this.bank = wallet;
        this.type = type;
        this.category = category;
        this.amount = amount;
        if (description != null && !description.isEmpty()) {
            this.description = description;
        } else {
            this.description = null;
        }
    }

    public static Transaction commit(Wallet wallet, TransactionType type, TransactionCategory category, Money amount, String description) {
        Transaction transaction = new WalletTransaction(wallet, type, category, amount, description);
        TransactionsStorage.getInstance().add(transaction);
        return transaction;
    }

    @Override
    public String storageKey() {
        return uuid;
    }
}
