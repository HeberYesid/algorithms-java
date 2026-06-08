package cache;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class WriteBehindCache<K, V> implements AutoCloseable {

    private final LRUCache<K, V> cache;
    private final CacheRepository<K, V> repo;
    private final BlockingQueue<Entry<K, V>> writeQueue = new LinkedBlockingQueue<>();
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final Thread writerThread;

    public WriteBehindCache(int capacity, CacheRepository<K, V> repo) {
        this.cache = new LRUCache<>(capacity);
        this.repo = repo;
        this.writerThread = new Thread(this::writerLoop, "write-behind-writer");
        this.writerThread.setDaemon(true);
        this.writerThread.start();
    }

    public void put(K key, V value) {
        cache.put(key, value);
        writeQueue.offer(new Entry<>(key, value));
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

    public void flush() {
        while (!writeQueue.isEmpty()) {
            try {
                TimeUnit.MILLISECONDS.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    @Override
    public void close() {
        running.set(false);
        writerThread.interrupt();
        flush();
    }

    private void writerLoop() {
        while (running.get() || !writeQueue.isEmpty()) {
            try {
                Entry<K, V> entry = writeQueue.poll(100, TimeUnit.MILLISECONDS);
                if (entry != null) {
                    repo.write(entry.key(), entry.value());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private record Entry<K, V>(K key, V value) {
    }
}
