package cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.jupiter.api.Test;

class WriteThroughCacheTest {

    @Test
    void afterPutDataIsInRepository() {
        InMemoryRepo repo = new InMemoryRepo();
        WriteThroughCache<String, String> cache = new WriteThroughCache<>(2, repo);

        cache.put("k", "v");
        assertEquals(Optional.of("v"), repo.read("k"));
    }

    @Test
    void cacheMissLoadsFromRepositoryAfterEviction() {
        InMemoryRepo repo = new InMemoryRepo();
        WriteThroughCache<String, String> cache = new WriteThroughCache<>(1, repo);

        cache.put("k1", "v1");
        cache.put("k2", "v2");

        assertTrue(cache.get("k1").isPresent());
        assertEquals("v1", cache.get("k1").orElseThrow());
    }

    private static final class InMemoryRepo implements CacheRepository<String, String> {
        private final Map<String, String> store = new ConcurrentHashMap<>();

        @Override
        public void write(String key, String value) {
            store.put(key, value);
        }

        @Override
        public Optional<String> read(String key) {
            return Optional.ofNullable(store.get(key));
        }

        @Override
        public void delete(String key) {
            store.remove(key);
        }
    }
}
