package edu.bloomfilter.bench;

import edu.bloomfilter.BloomFilter;
import org.openjdk.jmh.annotations.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Benchmark JMH para comparar consultas de pertenencia entre Bloom filter y HashSet.
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 2)
@Fork(1)
public class MembershipBenchmark {
    static final int N = 1_000_000;
    List<String> data;
    BloomFilter<String> bloom;
    HashSet<String> hashSet;
    String[] queries;

    /**
     * Inicializa los datos, el Bloom filter y el HashSet para el benchmark.
     */
    @Setup(Level.Trial)
    public void setup() {
        data = new ArrayList<>(N);
        Random rng = new Random(0);
        for (int i = 0; i < N; i++) data.add("element-" + rng.nextLong());
        
        // Bloom filter: epsilon = 1%
        bloom = new BloomFilter<>(N, 0.01, Object::hashCode);
        data.forEach(bloom::add);
        
        hashSet = new HashSet<>(data);
        
        // Queries: mitad presentes, mitad ausentes
        queries = new String[1000];
        for (int i = 0; i < 500; i++) queries[i] = data.get(i);
        for (int i = 500; i < 1000; i++) queries[i] = "absent-" + rng.nextLong();
    }

    /**
     * Ejecuta una consulta de pertenencia sobre el Bloom filter.
     *
     * @return resultado de la consulta
     */
    @Benchmark
    public boolean bloomQuery() {
        return bloom.mightContain(queries[(int)(System.nanoTime() % 1000)]);
    }

    /**
     * Ejecuta una consulta de pertenencia sobre el HashSet.
     *
     * @return resultado de la consulta
     */
    @Benchmark
    public boolean hashSetQuery() {
        return hashSet.contains(queries[(int)(System.nanoTime() % 1000)]);
    }
}
