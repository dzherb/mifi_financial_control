package finances;

import storage.TransactionsStorage;
import users.User;
import utils.UUIDGenerator;

public class Wallet implements MoneyBank<User> {
    public static final Currency DEFAULT_CURRENCY = Currency.RUB;
    private final TransactionsStorage transactionsStorage = TransactionsStorage.getInstance();

    private final String uuid;
    private User owner;
    private Currency currency;

    public Wallet(User owner) {
        this(owner, DEFAULT_CURRENCY);
    }

    private Wallet(User owner, Currency currency) {
        this.owner = owner;
        this.currency = currency;
        this.uuid = UUIDGenerator.generate();
    }

    @Override
    public User getOwner() {
        return owner;
    }

    @Override
    public Money getBalance() {
        // Не очень эффективно на большом объеме,
        // в реальных условиях нужно кэшировать результат последней транзакции
        int amount = transactionsStorage
                .all()
                .stream()
                .reduce(0, (a, tr) -> {
                    if (tr.getType().equals(TransactionType.INCOME)) {
                        return a + tr.getAmount().value(currency);
                    }
                    return a - tr.getAmount().value(currency);
                }, (_, tr2) -> tr2);

        return MoneyImpl.of(currency, amount);
    }

    @Override
    public boolean deposit(Money amount, TransactionCategory category, String reason) {
        WalletTransaction.commit(this, TransactionType.INCOME, category, amount, reason);
        return true;
    }

    @Override
    public boolean withdraw(Money amount, TransactionCategory category, String reason) {
        WalletTransaction.commit(this, TransactionType.EXPENSE, category, amount, reason);
        return true;
    }

    @Override
    public String storageKey() {
        return uuid;
    }
}
