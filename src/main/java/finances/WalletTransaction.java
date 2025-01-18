package finances;

import storage.TransactionsStorage;

public class WalletTransaction extends Transaction {
    // Не uuid, чтобы пользователю было легче обращаться к инстансам для удаления todo а надо ли давать удалять?
    private final String id = String.valueOf(TransactionsStorage.getInstance().all().size() + 1);

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
        return id;
    }
}
