package cache;

import java.util.Optional;

public class WriteThroughCache<K, V> {

    private final LRUCache<K, V> cache;
    private final CacheRepository<K, V> repo;

    public WriteThroughCache(int capacity, CacheRepository<K, V> repo) {
        this.cache = new LRUCache<>(capacity);
        this.repo = repo;
    }

    public Optional<V> get(K key) {
        Optional<V> cached = cache.get(key);
        if (cached.isPresent()) {
            return cached;
        }

        Optional<V> fromRepo = repo.read(key);
        fromRepo.ifPresent(v -> cache.put(key, v));
        return fromRepo;
    }

    /** Write-through: persistencia sincronica + cache. */
    public void put(K key, V value) {
        repo.write(key, value);
        cache.put(key, value);
    }

    public void invalidate(K key) {
        cache.remove(key);
        repo.delete(key);
    }
}
