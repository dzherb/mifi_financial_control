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
    public int value(Currency currency) {
        // Сейчас валюты в расчет не берем,
        // Но в дальнейшем здесь можно рассчитывать по курсу переданной валюты
        return amount;
    }
}
