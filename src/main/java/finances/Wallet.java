package finances;

import users.User;

import java.util.UUID;

public class Wallet implements MoneyStorage<User> {
    public static final Currency DEFAULT_CURRENCY = Currency.RUB;

    private final String uuid;
    private User owner;
    private Money balance;

    public Wallet(User owner) {
        this(owner, MoneyImpl.of(DEFAULT_CURRENCY, 0));
    }

    public Wallet(User owner, Money balance) {
        this.owner = owner;
        this.balance = balance;
        this.uuid = generateUUID();
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }

    @Override
    public User getOwner() {
        return owner;
    }

    @Override
    public Money getBalance() {
        return balance;
    }

    @Override
    public void setBalance(Money balance) {
        this.balance = balance;
    }

    @Override
    public boolean deposit(Money amount) {
        int totalAmount = balance.getAmount() + amount.getAmount(balance.getCurrency());
        this.balance = MoneyImpl.of(balance.getCurrency(), totalAmount);
        return true;
    }

    @Override
    public boolean withdraw(Money amount) {
        int totalAmount = balance.getAmount() - amount.getAmount(balance.getCurrency());
        this.balance = MoneyImpl.of(balance.getCurrency(), totalAmount);
        return true;
    }

    @Override
    public String storageKey() {
        return uuid;
    }
}
