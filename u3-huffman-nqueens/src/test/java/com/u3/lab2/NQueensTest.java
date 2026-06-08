package com.u3.lab2;

import com.u3.lab2.nqueens.NQueens;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NQueensTest {

    @Test
    void testNQueensAllVariations() {
        int[] expectedSolutions = {
            0, 1, 0, 0, 2, 10, 4, 40, 92, 352, 724, 2680, 14200
        };

        for (int n = 1; n <= 12; n++) {
            long naiveRes = NQueens.solveNaive(n);
            long bitmaskRes = NQueens.solveBitmask(n);

            assertEquals(expectedSolutions[n], naiveRes, "Naive method failed for n=" + n);
            assertEquals(expectedSolutions[n], bitmaskRes, "Bitmask method failed for n=" + n);
        }
    }
}
