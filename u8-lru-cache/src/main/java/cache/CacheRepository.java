package cache;

import java.util.Optional;

public interface CacheRepository<K, V> {
    void write(K key, V value);
    Optional<V> read(K key);
    void delete(K key);
}
