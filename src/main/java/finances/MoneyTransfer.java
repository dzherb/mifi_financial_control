package finances;

public interface MoneyTransfer {
    boolean transfer(MoneyStorage<?> from, MoneyStorage<?> to, Money amount);
}
