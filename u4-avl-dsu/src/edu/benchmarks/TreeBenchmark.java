package edu.benchmarks;

import edu.trees.AVLTree;
import java.util.Arrays;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
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
 * Benchmarks JMH para comparar AVLTree contra TreeMap.
 */
@State(org.openjdk.jmh.annotations.Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 2)
@Fork(1)
public class TreeBenchmark {
    static final int N = 100_000;

    Integer[] data;
    AVLTree<Integer> avl;
    TreeMap<Integer, Integer> treeMap;

    /**
     * Prepara datos y estructuras antes de cada trial de benchmark.
     */
    @Setup(Level.Trial)
    public void setup() {
        data = new Integer[N];
        Random rng = new Random(42);
        for (int i = 0; i < N; i++) {
            data[i] = rng.nextInt(N * 10);
        }

        avl = new AVLTree<>();
        treeMap = new TreeMap<>();
        Arrays.stream(data).forEach(value -> {
            avl.insert(value);
            treeMap.put(value, value);
        });
    }

    /**
     * Inserta todos los valores en un AVL nuevo y retorna su altura final.
     *
     * @return altura del árbol construido durante la medición
     */
    @Benchmark
    public int avlInsert() {
        AVLTree<Integer> tree = new AVLTree<>();
        for (Integer value : data) {
            tree.insert(value);
        }
        return tree.height();
    }

    /**
     * Inserta todos los valores en un TreeMap nuevo y retorna su tamaño final.
     *
     * @return cantidad de elementos almacenados al final de la medición
     */
    @Benchmark
    public int treeMapInsert() {
        TreeMap<Integer, Integer> map = new TreeMap<>();
        for (Integer value : data) {
            map.put(value, value);
        }
        return map.size();
    }

    /**
     * Consulta aleatoriamente un valor en el AVL precargado.
     *
     * @return {@code true} si el valor consultado existe
     */
    @Benchmark
    public boolean avlContains() {
        return avl.contains(data[ThreadLocalRandom.current().nextInt(N)]);
    }

    /**
     * Consulta aleatoriamente un valor en el TreeMap precargado.
     *
     * @return {@code true} si el valor consultado existe
     */
    @Benchmark
    public boolean treeMapContains() {
        return treeMap.containsKey(data[ThreadLocalRandom.current().nextInt(N)]);
    }
}