package com.u3.lab1;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FibonacciTest {

    @Test
    void testFibonacciSmallValues() {
        int[] nValues = {0, 1, 2, 5, 10, 20, 30};
        for (int n : nValues) {
            long expected = Fibonacci.fibTabla(n);
            assertEquals(expected, Fibonacci.fibRecursivo(n), "Error en fibRecursivo para n=" + n);
            assertEquals(expected, Fibonacci.fibMemo(n), "Error en fibMemo para n=" + n);
        }
    }

    @Test
    void testFibTablaLargeValue() {
        long result = Fibonacci.fibTabla(92);
        // Fibonacci.fibTabla(92) debe retornar 7540113804746346429 sin desbordamiento
        assertEquals(7540113804746346429L, result, "Error en fibTabla para n=92");
    }
}
