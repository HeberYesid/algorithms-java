package cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class LRUCacheTest {

    @Test
    void checkpointEvictionOrder() {
        LRUCache<String, String> cache = new LRUCache<>(3);
        cache.put("A", "1");
        cache.put("B", "2");
        cache.put("C", "3");

        assertEquals("1", cache.get("A").orElseThrow());
        cache.put("D", "4");

        assertFalse(cache.get("B").isPresent());
        assertTrue(cache.get("A").isPresent());
        assertEquals(3, cache.size());
    }
}
