package finances;

public interface Money {
    Currency getCurrency();
    int getAmount(Currency currency);
    default int getAmount() {
        return getAmount(getCurrency());
    }
}
