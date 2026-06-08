package edu.sistemas.u2.bench; 

import org.openjdk.jmh.annotations.*; 
import org.openjdk.jmh.infra.Blackhole; 

import java.util.concurrent.TimeUnit; 

@BenchmarkMode(Mode.AverageTime) 
@OutputTimeUnit(TimeUnit.MILLISECONDS) 
@Warmup(iterations = 5, time = 1) 
@Measurement(iterations = 10, time = 1) 
@Fork(2) 
@State(Scope.Benchmark) 
public class MatrixBenchmark { 

    @Param({"256", "512", "1024", "2048"}) 
    public int n; 

    private int[][] matrix; 

    @Setup(Level.Trial) 
    public void setup() { 
        matrix = new int[n][n]; 
        for (int i = 0; i < n; i++) 
            for (int j = 0; j < n; j++) 
                matrix[i][j] = i * n + j; 
    } 

    /** Acceso por filas — localidad espacial óptima. */ 
    @Benchmark 
    public long rowMajorSum(Blackhole bh) { 
        long sum = 0; 
        for (int i = 0; i < n; i++) 
            for (int j = 0; j < n; j++) 
                sum += matrix[i][j];   // stride = 1 int = 4 bytes 
        bh.consume(sum); 
        return sum; 
    } 

    /** Acceso por columnas — localidad espacial deficiente (cache misses). */ 
    @Benchmark 
    public long columnMajorSum(Blackhole bh) { 
        long sum = 0; 
        for (int j = 0; j < n; j++) 
            for (int i = 0; i < n; i++) 
                sum += matrix[i][j];   // stride = n ints = n*4 bytes 
        bh.consume(sum); 
        return sum; 
    } 
}
