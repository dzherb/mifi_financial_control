package storage;

import finances.TransactionCategory;
import finances.WalletTransactionCategory;

import java.util.Optional;

public class TransactionCategoriesStorage extends MapStorage<TransactionCategory> {
    private static TransactionCategoriesStorage instance;

    private TransactionCategoriesStorage() {}

    public static TransactionCategoriesStorage getInstance() {
        if (instance == null) {
            instance = new TransactionCategoriesStorage();
        }
        return instance;
    }

    public TransactionCategory getOrCreate(String name) {
        Optional<TransactionCategory> category = get(name);
        if (category.isPresent()) {
            return category.get();
        }
        TransactionCategory newCategory = new WalletTransactionCategory(name);
        add(newCategory);
        return newCategory;
    }
}
