package cache;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class OptimisticStore<K, V> {

    public record Versioned<V>(V value, long version) {
    }

    private final ConcurrentHashMap<K, Versioned<V>> store = new ConcurrentHashMap<>();

    public Optional<Versioned<V>> get(K key) {
        return Optional.ofNullable(store.get(key));
    }

    /** Escritura inicial sin condicion. */
    public Versioned<V> put(K key, V value) {
        Versioned<V> versioned = new Versioned<>(value, 1L);
        store.put(key, versioned);
        return versioned;
    }

    /** Escritura condicional por version esperada (If-Match). */
    public Versioned<V> updateIfMatch(K key, V newValue, long expectedVersion) {
        return store.compute(key, (k, current) -> {
            if (current == null) {
                throw new IllegalStateException("Key not found: " + k);
            }
            if (current.version() != expectedVersion) {
                throw new OptimisticLockException(
                    "Expected v" + expectedVersion + " but found v" + current.version()
                );
            }
            return new Versioned<>(newValue, current.version() + 1);
        });
    }
}
