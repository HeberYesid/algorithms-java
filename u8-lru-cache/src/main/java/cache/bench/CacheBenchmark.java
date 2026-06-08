package cache.bench;

import cache.LRUCache;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
public class CacheBenchmark {

    @Param({"100", "1000", "10000"})
    private int cacheSize;

    private LRUCache<Integer, String> lruCache;
    private Map<Integer, String> directMap;
    private Random rng;

    @Setup
    public void setup() {
        lruCache = new LRUCache<>(cacheSize);
        directMap = new HashMap<>();
        rng = new Random(42);

        for (int i = 0; i < cacheSize * 10; i++) {
            directMap.put(i, "value-" + i);
        }
    }

    @Benchmark
    public String withCache() {
        int key = nextHotKey();
        return lruCache.get(key).orElseGet(() -> {
            String v = directMap.get(key);
            lruCache.put(key, v);
            return v;
        });
    }

    @Benchmark
    public String withoutCache() {
        return directMap.get(nextHotKey());
    }

    private int nextHotKey() {
        // Aproxima localidad temporal: 80% accesos en 20% de llaves.
        int universe = cacheSize * 10;
        if (rng.nextDouble() < 0.8) {
            return rng.nextInt(Math.max(1, universe / 5));
        }
        return rng.nextInt(universe);
    }
}
