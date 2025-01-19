package cli;

import finances.*;
import storage.TransactionCategoriesStorage;
import storage.UsersStorage;
import storage.WalletsStorage;
import users.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FinancesLayerCommandsHandler extends CLI {
    private final Wallet wallet;
    private final WalletAnalyticsProcessor walletAnalyticsProcessor;

    protected FinancesLayerCommandsHandler(User user) {
        wallet = WalletsStorage.getInstance().find(w -> w.getOwner().getUUID().equals(user.getUUID())).get();
        walletAnalyticsProcessor = new WalletAnalyticsProcessor(wallet);
    }

    @Override
    void run() {
        showBalance();
        do {
            availableCommands();
        }
        while (handleNextCommand());
    }

    private void showBalance() {
        print("\nБаланс вашего кошелька: ");
        print(wallet.getBalance().toString() + "\n", TextColor.PURPLE);
    }

    private void availableCommands() {
        print("\nДоступные команды:\n");
        printCommandDescription("analytics", "аналитика доходов и расходов");
        printCommandDescription("deposit", "записать доход");
        printCommandDescription("spend", "записать расход");
        printCommandDescription("limits", "установить лимиты");
        printCommandDescription("transfer", "перевести деньги другому пользователю");
        printCommandDescription("back", "вернуться в главное меню");
    }

    private boolean handleNextCommand() {
        boolean stayAtCurrentLevel = true;

        switch (getInput()) {
            case "analytics":
                onAnalyticsCommand();
                break;
            case "deposit":
                onDepositCommand();
                break;
            case "spend":
                onSpendCommand();
                break;
            case "limits":
                onLimitsCommand();
                break;
            case "transfer":
                onTransferCommand();
                break;
            case "back":
                stayAtCurrentLevel = false;
                break;
            default:
                onUnknownCommand();
        }
        return stayAtCurrentLevel;
    }

    private void onAnalyticsCommand() {
        showTotalStatistics();
    }

    private void showTotalStatistics() {
        print("\nОбщие доходы: ");
        print(walletAnalyticsProcessor.totalIncome() + "\n", TextColor.PURPLE);
        print("Общие расходы: ");
        print(walletAnalyticsProcessor.totalExpenses() + "\n", TextColor.PURPLE);
        List<TransactionCategory> categories = getTransactionCategoriesFromInput();
        for (TransactionCategory category : categories) {
            print("\nОбщий расход по категории ");
            print(category + ": ", TextColor.CYAN);
            print(walletAnalyticsProcessor.totalExpensesForCategory(category) + "\n", TextColor.PURPLE);
            print("Общий доход по категории ");
            print(category + ": ", TextColor.CYAN);
            print(walletAnalyticsProcessor.totalIncomeForCategory(category) + "\n", TextColor.PURPLE);
        }
    }

    private List<TransactionCategory> getTransactionCategoriesFromInput() {
        print("\nВведите категории, по которым хотите получить статистику. Чтобы закончить ввод, используйте символ \"!\"\n");

        List<TransactionCategory> transactionCategories = new ArrayList<>();

        String input;
        while (true){
            input = getInput();
            if (input.equals("!")) {
                break;
            }
            Optional<TransactionCategory> category = TransactionCategoriesStorage.getInstance().get(input);
            if (category.isPresent()) {
                transactionCategories.add(category.get());
                print("\nКатегория добавлена в список\n", TextColor.GREEN);
            } else {
                print("\nТакой категории не нашлось!\n", TextColor.RED);
            }
        }

        return transactionCategories;
    }

    private void onDepositCommand() {
        handleTransaction(true);
    }

    private void onSpendCommand() {
        handleTransaction(false);
    }

    private void handleTransaction(boolean isDeposit) {
        if (isDeposit) {
            print("\nВведите категорию доходов: \n");
        } else {
            print("\nВведите категорию расходов: \n");
        }
        String categoryName = getInput();

        print("\nВведите сумму: \n");
        int amount = getIntInput();

        print("\nВведите описание к операции (опционально): \n");
        String description = getInput();

        TransactionCategory category = TransactionCategoriesStorage.getInstance().getOrCreateCategory(categoryName);
        Money money = MoneyImpl.of(Wallet.DEFAULT_CURRENCY, amount);

        if (isDeposit) {
            wallet.deposit(money, category, description);

        } else {
            wallet.withdraw(money, category, description);
            notifyUserIfLimitForCategoryIsExceeded(category);
        }

        print("\nТранзакция была успешно добавлена!\n", TextColor.GREEN);
        showBalance();
    }
    private void notifyUserIfLimitForCategoryIsExceeded(TransactionCategory category) {
        if (SpendingLimit.isLimitExceededForCategory(category, wallet)) {
            print("\nОбратите внимание, превышен установленный лимит по категории \"" + category + "\"\n", TextColor.RED);
        }
    }

    private void onLimitsCommand() {
        showCurrentLimits();
        if (yesNoInput("\nХотите установить новый лимит? (y/n)\n")) {
            SpendingLimit limit = addLimit();
            notifyUserIfLimitForCategoryIsExceeded(limit.getCategory());
            showCurrentLimits();
        }
    }

    private void showCurrentLimits() {
        List<SpendingLimit> walletLimits = SpendingLimit.walletLimits(wallet);
        if (walletLimits.isEmpty()) {
            print("\nПока лимиты отсутствуют...\n", TextColor.YELLOW);
            return;
        }
        print("\nТекущие лимиты: \n", TextColor.YELLOW);
        for (SpendingLimit limit : walletLimits) {
            print("\nКатегория: ");
            print(limit.getCategory() + "\n", TextColor.CYAN);
            print("Лимит: ");
            print(limit.getAmount() + "\n", TextColor.PURPLE);

            print("Суммарные расходы по категории: ");
            Money categorySpends = walletAnalyticsProcessor.totalExpensesForCategory(limit.getCategory());
            TextColor color;
            if (SpendingLimit.isLimitExceededForCategory(limit.getCategory(), wallet)) {
                color = TextColor.RED;
            } else {
                color = TextColor.PURPLE;
            }
            print(categorySpends + "\n", color);

            print("Оставшийся бюджет: ");
            Money canSpend = MoneyImpl.of(categorySpends.getCurrency(), limit.getAmount().value() - categorySpends.value());
            print(canSpend + "\n", color);
        }
    }

    private SpendingLimit addLimit() {
        print("\nВведите название категории:\n");
        String categoryName = getInput();
        TransactionCategory category = TransactionCategoriesStorage.getInstance().getOrCreateCategory(categoryName);

        print("\nВведите лимит: \n");
        int limit = getIntInput();
        Money money = MoneyImpl.of(Wallet.DEFAULT_CURRENCY, limit);

        SpendingLimit newLimit = SpendingLimit.create(wallet, money, category);
        print("\nЛимит был успешно добавлен!\n", TextColor.GREEN);
        return newLimit;
    }

    private void onTransferCommand() {
        print("\nВы можете перевести перевести деньги другому пользователю\n");
        print("Введите username получателя перевода\n");
        String username = getInput();
        Optional<User> receiver = UsersStorage.getInstance().find(u -> u.getUsername().equals(username));
        if (receiver.isEmpty()) {
            print("\nТакого пользователя не нашлось!\n", TextColor.RED);
            return;
        }

        Wallet receiverWaller = WalletsStorage.getInstance().find(w -> w.getOwner().equals(receiver.get())).get();

        print("\nВведите сумму перевода\n");
        Money amount = MoneyImpl.of(Wallet.DEFAULT_CURRENCY, getIntInput());

        try {
            new MoneyTransferImpl().transfer(wallet, receiverWaller, amount);
        } catch (MoneyTransfer.NotEnoughMoneyException e) {
            print("\nНа счете недостаточно средств!\n", TextColor.RED);
            return;
        }

        print("\nПеревод выполнен успешно!\n", TextColor.GREEN);
        showBalance();
    }

    private void onUnknownCommand() {
        print("\nКоманда не распознана :с\nДля возвращения в главное меню введите back\n", TextColor.RED);
    }
}
