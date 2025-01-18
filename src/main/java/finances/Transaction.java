package finances;

import storage.Storable;

public abstract class Transaction implements Storable {
    protected MoneyBank<?> bank;
    protected TransactionType type;
    protected TransactionCategory category;
    protected Money amount;
    protected String description;

    public MoneyBank<?> getBank() {
        return bank;
    }

    public TransactionType getType(){
        return type;
    }
    public TransactionCategory getCategory(){
        return category;
    }
    public Money getAmount(){
        return amount;
    }
    public String getDescription(){
        return description;
    }
}
