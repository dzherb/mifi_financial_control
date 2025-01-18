package storage;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface Storage<T extends Storable> {
    List<T> all();
    boolean add(T item);
    Optional<T> get(String key);
    Optional<T> find(Predicate<? super T> predicate);
    boolean remove(T item);
    boolean contains(T item);
}
