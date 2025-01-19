package finances;

import java.io.Serializable;

public interface Money extends Serializable {
    Currency getCurrency();
    int value(Currency currency);
    default int value() {
        return value(getCurrency());
    }
}
