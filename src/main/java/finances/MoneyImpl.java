package finances;

public class MoneyImpl implements Money {
    private final Currency currency;
    private final int amount;

    public static class NegativeAmountException extends RuntimeException {}

    private MoneyImpl(Currency currency, int amount) {
        this.currency = currency;
        this.amount = amount;
    }

    public static Money of(Currency currency, int amount) {
        if (amount < 0) {
            throw new NegativeAmountException();
        }
        return new MoneyImpl(currency, amount);
    }

    @Override
    public Currency getCurrency() {
        return currency;
    }

    @Override
    public int getAmount(Currency currency) {
        return amount;
    }
}
