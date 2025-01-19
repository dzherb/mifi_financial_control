package finances;

import users.User;

public interface MoneyTransfer {
    public static class NotEnoughMoneyException extends RuntimeException {}

    void transfer(Wallet from, Wallet to, Money amount) throws NotEnoughMoneyException;
}
