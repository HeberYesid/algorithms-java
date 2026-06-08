package com.grafos.bench;

import com.grafos.BFSUnweighted;
import com.grafos.Dijkstra;
import com.grafos.GraphCaseFactory;
import com.grafos.WeightedDiGraph;
import java.util.List;
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

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class ShortestPathBenchmark {

    @Param({"1000", "5000"})
    public int vertices;

    private List<List<Integer>> unweighted;
    private WeightedDiGraph weighted;

    @Setup
    public void setup() {
        int edges = vertices * 6;
        GraphCaseFactory.GraphPair pair = GraphCaseFactory.randomUnweightedSparse(vertices, edges);
        unweighted = pair.unweighted();
        weighted = pair.weighted();
    }

    @Benchmark
    public int[] dijkstraBenchmark() {
        return Dijkstra.run(weighted, 0);
    }

    @Benchmark
    public int[] bfsBenchmark() {
        return BFSUnweighted.shortestDistances(unweighted, 0);
    }
}