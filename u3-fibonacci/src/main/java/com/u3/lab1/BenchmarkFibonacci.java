package com.u3.lab1; 

import org.openjdk.jmh.annotations.*; 
import java.util.concurrent.TimeUnit; 

@State(Scope.Benchmark) 
@BenchmarkMode(Mode.AverageTime) 
@OutputTimeUnit(TimeUnit.MICROSECONDS) 
@Warmup(iterations = 3, time = 1) 
@Measurement(iterations = 5, time = 1) 
@Fork(1) 
public class BenchmarkFibonacci { 

    @Param({"10", "20", "30", "35"}) 
    public int n; 

    @Benchmark 
    public long recursivo() { return Fibonacci.fibRecursivo(n); } 

    @Benchmark 
    public long memoizacion() { return Fibonacci.fibMemo(n); } 

    @Benchmark 
    public long tabulacion() { return Fibonacci.fibTabla(n); } 
}