package finances;

import storage.SpendingLimitsStorage;
import storage.Storable;
import users.User;
import utils.UUIDGenerator;

import java.util.List;
import java.util.NoSuchElementException;

public class SpendingLimit implements Storable {
    private String uuid = UUIDGenerator.generate();
    private MoneyBank<User> wallet;
    private Money amount;
    private TransactionCategory category;

    private static SpendingLimitsStorage storage = SpendingLimitsStorage.getInstance();

    private SpendingLimit(MoneyBank<User> wallet, Money limit, TransactionCategory category) {
        this.wallet = wallet;
        amount = limit;
        this.category = category;
    }

    public static SpendingLimit create(MoneyBank<User> wallet, Money limit, TransactionCategory category) {
        try {
            // Если для категории уже есть установленный лимит, то его перезаписываем
            SpendingLimit existingLimitForCategory = storage.find(l -> l.getCategory().equals(category)).get();
            storage.remove(existingLimitForCategory);
        } catch (NoSuchElementException _) {}

        SpendingLimit newLimit = new SpendingLimit(wallet, limit, category);
        storage.add(newLimit);
        return newLimit;
    }

    public MoneyBank<User> getWallet() {
        return wallet;
    }

    public Money getAmount() {
        return amount;
    }

    public TransactionCategory getCategory() {
        return category;
    }

    public static List<SpendingLimit> walletLimits(MoneyBank<User> wallet) {
        return storage
                .all()
                .stream()
                .filter(spendingLimit -> spendingLimit.getWallet().equals(wallet))
                .toList();
    }

    public static boolean isLimitExceededForCategory(TransactionCategory category, MoneyBank<User> wallet) {
        final boolean[] isLimitExceeded = {false};

        Money totalBalanceForCategory = new WalletAnalyticsProcessor(wallet).totalExpensesForCategory(category);

        Currency walletCurrency = totalBalanceForCategory.getCurrency();

        SpendingLimitsStorage.getInstance().find(l -> l.getCategory().equals(category)).ifPresent(l -> {
            int limitAmount = l.getAmount().value(walletCurrency);
            if (totalBalanceForCategory.value() > limitAmount) {
                isLimitExceeded[0] = true;
            }
        });
        return isLimitExceeded[0];
    };

    @Override
    public String storageKey() {
        return uuid;
    }
}
