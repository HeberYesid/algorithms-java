package cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

class OptimisticStoreTest {

    @Test
    void twoThreadsConflictThenRetry() throws Exception {
        OptimisticStore<String, String> store = new OptimisticStore<>();
        store.put("k", "v0");

        var v1 = store.get("k").orElseThrow();
        var v2 = store.get("k").orElseThrow();

        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        AtomicBoolean secondThreadRetried = new AtomicBoolean(false);

        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<?> f1 = pool.submit(() -> {
            ready.countDown();
            await(start);
            store.updateIfMatch("k", "v1", v1.version());
        });

        Future<?> f2 = pool.submit(() -> {
            ready.countDown();
            await(start);
            try {
                store.updateIfMatch("k", "v2", v2.version());
            } catch (OptimisticLockException ex) {
                var latest = store.get("k").orElseThrow();
                store.updateIfMatch("k", "v2", latest.version());
                secondThreadRetried.set(true);
            }
        });

        ready.await(2, TimeUnit.SECONDS);
        start.countDown();

        f1.get();
        f2.get();

        pool.shutdownNow();

        var end = store.get("k").orElseThrow();
        assertTrue(secondThreadRetried.get());
        assertEquals("v2", end.value());
        assertEquals(3L, end.version());
    }

    private static void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
    }
}
