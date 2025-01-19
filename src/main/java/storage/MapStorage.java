package storage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

public class MapStorage<T extends Storable> implements Storage<T> {
    private final Map<String, T> map = new HashMap<>();

    @Override
    public List<T> all() {
        return (List<T>) map.values();
    }

    @Override
    public boolean add(T item) {
        return map.put(item.storageKey(), item) != null;
    }

    @Override
    public Optional<T> get(String key) {
        return Optional.ofNullable(map.get(key));
    }

    @Override
    public Optional<T> find(Predicate<T> predicate) {
        for (T item : map.values()) {
            if (predicate.test(item)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean remove(T item) {
        return map.remove(item.storageKey()) != null;
    }

    @Override
    public boolean contains(T item) {
        return map.containsKey(item.storageKey());
    }
}
