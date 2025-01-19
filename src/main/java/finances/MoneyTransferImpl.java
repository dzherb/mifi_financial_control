package finances;

import storage.TransactionCategoriesStorage;
import users.User;

public class MoneyTransferImpl implements MoneyTransfer {
    @Override
    public void transfer(Wallet from, Wallet to, Money amount) throws NotEnoughMoneyException {
        if (from.getBalance().value() < amount.value()) {
            throw new NotEnoughMoneyException();
        }

        from.withdraw(amount, getMoneyTransferCategory(), getMoneyTransferToDescription(from, to));
        to.deposit(amount, getMoneyTransferCategory(), getMoneyTransferFromDescription(from, to));
    }

    private String getMoneyTransferToDescription(MoneyBank<User> from, MoneyBank<User> to) {
        return "Перевод пользователю " + to.getOwner().getUsername();
    }

    private String getMoneyTransferFromDescription(MoneyBank<User> from, MoneyBank<User> to) {
        return "Перевод от пользователя " + from.getOwner().getUsername();
    }

    private TransactionCategory getMoneyTransferCategory() {
        return TransactionCategoriesStorage.getInstance().getOrCreateCategory("Переводы");
    }
}
