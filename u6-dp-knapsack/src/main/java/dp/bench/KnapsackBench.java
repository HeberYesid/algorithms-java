package dp.bench;

import dp.Knapsack;
import java.util.Random;
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
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@Warmup(iterations = 3, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@State(Scope.Benchmark)
public class KnapsackBench {

    @Param({"100", "500", "1000"})
    public int n;

    @Param({"1000", "5000", "10000"})
    public int wMax;

    private int[] weights;
    private int[] values;

    @Setup
    public void setup() {
        Random rng = new Random(42);
        weights = new int[n];
        values = new int[n];
        for (int i = 0; i < n; i++) {
            weights[i] = rng.nextInt(Math.max(2, wMax / 4)) + 1;
            values[i] = rng.nextInt(100) + 1;
        }
    }

    @Benchmark
    public int bench01() {
        return Knapsack.solve01(weights, values, wMax);
    }

    @Benchmark
    public int benchMemOpt() {
        return Knapsack.solveMemOpt(weights, values, wMax);
    }
}
