package storage;

import finances.Transaction;

public class TransactionsStorage extends ListStorage<Transaction> {
    private static TransactionsStorage instance;

    private TransactionsStorage() {}

    public static TransactionsStorage getInstance() {
        if (instance == null) {
            instance = new TransactionsStorage();
        }
        return instance;
    }
}
