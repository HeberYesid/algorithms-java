package edu.benchmarks;

import edu.dsu.DSU;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

/**
 * Benchmarks JMH para comparar DSU contra una implementación ingenua basada en HashMap.
 */
@State(org.openjdk.jmh.annotations.Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 2)
@Fork(1)
public class UnionFindBenchmark {
    static final int N = 100_000;
    static final int EDGE_COUNT = 250_000;

    int[][] edges;

    /**
     * Genera un conjunto fijo de aristas aleatorias para cada trial.
     */
    @Setup(Level.Trial)
    public void setup() {
        edges = new int[EDGE_COUNT][2];
        Random rng = new Random(42);
        for (int i = 0; i < EDGE_COUNT; i++) {
            edges[i][0] = rng.nextInt(N);
            edges[i][1] = rng.nextInt(N);
        }
    }

    /**
     * Ejecuta uniones con la estructura DSU sobre el conjunto de aristas generado.
     *
     * @return cantidad de uniones efectivas realizadas
     */
    @Benchmark
    public int dsuUnionOnRandomGraph() {
        DSU dsu = new DSU(N);
        int merged = 0;
        for (int[] edge : edges) {
            if (dsu.union(edge[0], edge[1])) {
                merged++;
            }
        }
        return merged;
    }

    /**
     * Ejecuta la misma secuencia de uniones sobre una implementación ingenua con HashMap.
     *
     * @return cantidad de uniones efectivas realizadas
     */
    @Benchmark
    public int hashMapUnionOnRandomGraph() {
        NaiveHashMapUnionFind unionFind = new NaiveHashMapUnionFind(N);
        int merged = 0;
        for (int[] edge : edges) {
            if (unionFind.union(edge[0], edge[1])) {
                merged++;
            }
        }
        return merged;
    }

    private static final class NaiveHashMapUnionFind {
        private final Map<Integer, Integer> parent = new HashMap<>();

        NaiveHashMapUnionFind(int n) {
            for (int i = 0; i < n; i++) {
                parent.put(i, i);
            }
        }

        int find(int x) {
            Integer current = parent.get(x);
            while (current != null && current != x) {
                x = current;
                current = parent.get(x);
            }
            return x;
        }

        boolean union(int x, int y) {
            int rx = find(x);
            int ry = find(y);
            if (rx == ry) {
                return false;
            }
            parent.put(ry, rx);
            return true;
        }
    }
}