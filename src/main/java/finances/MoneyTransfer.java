package finances;

public interface MoneyTransfer {
    boolean transfer(MoneyBank<?> from, MoneyBank<?> to, Money amount);
}
