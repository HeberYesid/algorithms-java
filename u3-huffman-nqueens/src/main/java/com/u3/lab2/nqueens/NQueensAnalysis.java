package com.u3.lab2.nqueens;

import java.util.concurrent.atomic.AtomicLong;

/** Análisis comparativo de nodos explorados: naive vs bitmask. */
public class NQueensAnalysis {
    public record Result(int n, long solutions, long nodesNaive, long nodesBitmask) {
        public double reductionRatio() {
            return nodesNaive == 0 ? 1.0 : (double) nodesBitmask / nodesNaive;
        }
    }

    public static Result analyze(int n) {
        AtomicLong nodesN = new AtomicLong(0);
        AtomicLong nodesB = new AtomicLong(0);
        
        long sols = countNaive(n, 0, new int[n], nodesN);
        countBitmask(n, 0, 0, 0, 0, nodesB);
        
        return new Result(n, sols, nodesN.get(), nodesB.get());
    }

    private static long countNaive(int n, int row, int[] cols, AtomicLong nodes) {
        nodes.incrementAndGet();
        if (row == n) return 1;
        long count = 0;
        for (int col = 0; col < n; col++) {
            if (isValid(cols, row, col)) {
                cols[row] = col;
                count += countNaive(n, row + 1, cols, nodes);
            }
        }
        return count;
    }

    private static boolean isValid(int[] cols, int row, int col) {
        for (int r = 0; r < row; r++) {
            if (cols[r] == col || Math.abs(cols[r]-col) == row-r) return false;
        }
        return true;
    }

    private static long countBitmask(int n, int row, int cols, int diag1, int diag2, AtomicLong nodes) {
        nodes.incrementAndGet();
        if (row == n) return 1;
        long count = 0;
        int available = ((1 << n) - 1) & ~(cols | diag1 | diag2);
        while (available != 0) {
            int bit = available & (-available);
            available -= bit;
            count += countBitmask(n, row+1, cols|bit, (diag1|bit)<<1, (diag2|bit)>>1, nodes);
        }
        return count;
    }
}
