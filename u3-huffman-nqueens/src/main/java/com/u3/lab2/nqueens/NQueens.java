package com.u3.lab2.nqueens;

/** N-Queens: variante naive vs bitmask para comparación de eficiencia.
*/
public class NQueens {
    // --- Variante naive: verifica conflictos iterando por reinas previas ---
    public static long solveNaive(int n) {
        return naiveHelper(n, 0, new int[n]);
    }
    private static long naiveHelper(int n, int row, int[] cols) {
        if (row == n) return 1;
        long count = 0;
        for (int col = 0; col < n; col++) {
            if (isValid(cols, row, col)) {
                cols[row] = col;
                count += naiveHelper(n, row + 1, cols);
            }
        }
        return count;
    }
    private static boolean isValid(int[] cols, int row, int col) {
        for (int r = 0; r < row; r++) {
            if (cols[r] == col) return false; // misma columna
            if (Math.abs(cols[r] - col) == row - r) return false; // diagonal
        }
        return true;
    }

    // --- Variante bitmask: O(1) verificación por bits ---
    public static long solveBitmask(int n) {
        return bitmaskHelper(n, 0, 0, 0, 0);
    }
    private static long bitmaskHelper(int n, int row, int cols, int diag1, int diag2) {
        if (row == n) return 1;
        long count = 0;
        int available = ((1 << n) - 1) & ~(cols | diag1 | diag2);
        while (available != 0) {
            int bit = available & (-available); // bit menos significativo
            available -= bit;
            count += bitmaskHelper(n, row + 1, cols | bit, (diag1 | bit) << 1, (diag2 | bit) >> 1);
        }
        return count;
    }
}
