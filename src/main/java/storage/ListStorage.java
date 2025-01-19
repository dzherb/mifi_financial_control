package storage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ListStorage<T extends Storable> implements Storage<T> {
    private final List<T> items = new ArrayList<>();

    @Override
    public List<T> all() {
        return items;
    }

    @Override
    public boolean add(T item) {
        return items.add(item);
    }

    @Override
    public Optional<T> get(String key) {
        return find(u -> u.storageKey().equals(key));
    }

    @Override
    public Optional<T> find(Predicate<T> predicate) {
        for (T item : items) {
            if (predicate.test(item)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean remove(T item) {
        return items.removeIf(i -> i.storageKey().equals(item.storageKey()));
    }

    @Override
    public boolean contains(T item) {
        for (T i : items) {
            if (i.storageKey().equals(item.storageKey())) {
                return true;
            }
        }
        return false;
    }
}
