package storage;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface Storage<T extends Storable> extends Serializable {
    List<T> all();
    boolean add(T item);
    Optional<T> get(String key);
    Optional<T> find(Predicate<T> predicate);
    boolean remove(T item);
    boolean contains(T item);

    void updateInstance(Storage<? extends Storable> storage);
}
