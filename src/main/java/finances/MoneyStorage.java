package finances;

import storage.Storable;

public interface MoneyStorage<T> extends Storable {
    T getOwner();  // Не обязательно User, может быть и банк
    Money getBalance();
    void setBalance(Money balance);
    boolean deposit(Money amount);
    boolean withdraw(Money amount);
}
