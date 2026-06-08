package com.grafos.bench;

import com.grafos.BFS;
import com.grafos.BFSMatrix;
import com.grafos.Graph;
import com.grafos.MatrixGraph;
import com.grafos.RandomGraphGen;
import java.util.concurrent.TimeUnit;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class GraphBenchmark {

    @Param({"sparse", "medium", "dense"})
    public String density;

    private Graph<Integer> listGraph;
    private MatrixGraph matGraph;

    private static final int V = 500;

    @Setup
    public void setup() {
        int e = switch (density) {
            case "sparse" -> V * 2;
            case "medium" -> V * 10;
            default -> V * V / 4;
        };
        listGraph = RandomGraphGen.buildList(V, e);
        matGraph = RandomGraphGen.buildMatrix(V, e);
    }

    @Benchmark
    public int[] bfsList() {
        return BFS.shortestDistances(listGraph, 0);
    }

    @Benchmark
    public int[] bfsMatrix() {
        return BFSMatrix.run(matGraph, 0);
    }
}