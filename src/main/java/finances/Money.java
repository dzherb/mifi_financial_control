package finances;

public interface Money {
    Currency getCurrency();
    int value(Currency currency);
    default int value() {
        return value(getCurrency());
    }
}
