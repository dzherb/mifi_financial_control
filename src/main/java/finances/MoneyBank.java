package finances;

import storage.Storable;

public interface MoneyBank<T> extends Storable {
    T getOwner();  // Не обязательно User, может быть и банк
    Money getBalance();

    boolean deposit(Money amount, TransactionCategory category, String reason);
    default boolean deposit(Money amount, TransactionCategory category) {
        return deposit(amount, category, null);
    }

    boolean withdraw(Money amount, TransactionCategory category, String reason);
    default boolean withdraw(Money amount, TransactionCategory category) {
        return withdraw(amount, category, null);
    }
}
