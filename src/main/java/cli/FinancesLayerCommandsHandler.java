package cli;

import finances.Money;
import finances.MoneyImpl;
import finances.TransactionCategory;
import finances.Wallet;
import storage.TransactionCategoriesStorage;
import storage.WalletsStorage;
import users.User;

public class FinancesLayerCommandsHandler extends CLI {
    private Wallet wallet;

    protected FinancesLayerCommandsHandler(User user) {
        wallet = WalletsStorage.getInstance().find(w -> w.getOwner().getUUID().equals(user.getUUID())).get();
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
        // todo проверять что число положительное
        int amount = getIntInput();

        print("\nВведите описание к операции (опционально): \n");
        String description = getInput();

        TransactionCategory category = TransactionCategoriesStorage.getInstance().getOrCreate(categoryName);
        Money money = MoneyImpl.of(Wallet.DEFAULT_CURRENCY, amount);

        if (isDeposit) {
            wallet.deposit(money, category, description);
        } else {
            wallet.withdraw(money, category, description);
        }

        print("\nТранзакция была успешно добавлена!\n", TextColor.GREEN);
        showBalance();
    }

    private void onTransferCommand() {

    }

    private void onUnknownCommand() {
        print("\nКоманда не распознана :с\nДля возвращения в главное меню введите back\n", TextColor.RED);
    }
}
