package storage;

import java.io.Serializable;

public interface Storable extends Serializable {
    String storageKey();
}
