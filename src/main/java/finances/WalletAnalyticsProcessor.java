package finances;

import storage.TransactionsStorage;
import users.User;

import java.util.stream.Stream;

public class WalletAnalyticsProcessor {
    private final MoneyBank<User> wallet;
    private final TransactionsStorage transactionsStorage = TransactionsStorage.getInstance();

    public WalletAnalyticsProcessor(MoneyBank<User> wallet) {
        this.wallet = wallet;
    }

    public Money totalIncome() {
        return aggregateByTransactionType(TransactionType.INCOME);
    }

    public Money totalExpenses() {
        return aggregateByTransactionType(TransactionType.EXPENSE);
    }

    private Money aggregateByTransactionType(TransactionType type) {
        Currency walletCurrency = wallet.getBalance().getCurrency();

        int total = transactionsStorage
                .all()
                .stream()
                .filter(tr -> tr.getBank().equals(wallet) && tr.getType().equals(type))
                .reduce(0, (a, tr) -> {
                    return a + tr.getAmount().value(walletCurrency);
                }, (_, tr2) -> tr2);
        return MoneyImpl.of(walletCurrency, total);
    }

    public Money totalIncomeForCategory(TransactionCategory category) {
        return aggregateByCategoryAndTransactionType(category, TransactionType.INCOME);
    }

    public Money totalExpensesForCategory(TransactionCategory category) {
        return aggregateByCategoryAndTransactionType(category, TransactionType.EXPENSE);
    }

    public Money aggregateByCategoryAndTransactionType(TransactionCategory category, TransactionType type) {
        Stream<Transaction> transactionsForCategory = transactionsStorage
                .all()
                .stream()
                .filter((t) -> t.getBank().equals(wallet) && t.getCategory().equals(category) && t.getType().equals(type));

        Currency walletCurrency = wallet.getBalance().getCurrency();

        int amount = transactionsForCategory
                .reduce(0, (a, tr) -> a + tr.getAmount().value(walletCurrency), (_, tr2) -> tr2);

        return MoneyImpl.of(walletCurrency, amount);
    }
}
