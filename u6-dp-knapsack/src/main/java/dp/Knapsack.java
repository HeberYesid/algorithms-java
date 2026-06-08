package dp;

import java.util.Objects;

public class Knapsack {

    private Knapsack() {
    }

    /**
     * Knapsack 0/1 - O(n*W) tiempo, O(n*W) espacio.
     */
    public static int solve01(int[] weights, int[] values, int wMax) {
        validateInputs(weights, values, wMax);
        int n = weights.length;
        int[][] dp = new int[n + 1][wMax + 1];

        for (int i = 1; i <= n; i++) {
            int wi = weights[i - 1];
            int vi = values[i - 1];
            for (int w = 0; w <= wMax; w++) {
                dp[i][w] = dp[i - 1][w];
                if (wi <= w) {
                    dp[i][w] = Math.max(dp[i][w], vi + dp[i - 1][w - wi]);
                }
            }
        }
        return dp[n][wMax];
    }

    /**
     * Unbounded Knapsack con arreglo 1D.
     */
    public static int solveUnbounded(int[] weights, int[] values, int wMax) {
        validateInputs(weights, values, wMax);
        int[] dp = new int[wMax + 1];

        for (int w = 0; w <= wMax; w++) {
            for (int i = 0; i < weights.length; i++) {
                if (weights[i] <= w) {
                    dp[w] = Math.max(dp[w], values[i] + dp[w - weights[i]]);
                }
            }
        }
        return dp[wMax];
    }

    /**
     * Knapsack 0/1 con O(W) espacio, iterando de derecha a izquierda.
     */
    public static int solveMemOpt(int[] weights, int[] values, int wMax) {
        validateInputs(weights, values, wMax);
        int[] dp = new int[wMax + 1];

        for (int i = 0; i < weights.length; i++) {
            int wi = weights[i];
            int vi = values[i];
            for (int w = wMax; w >= wi; w--) {
                dp[w] = Math.max(dp[w], vi + dp[w - wi]);
            }
        }
        return dp[wMax];
    }

    /**
     * Reconstruccion O(n+W): determina que objetos fueron seleccionados.
     */
    public static boolean[] reconstruct(int[] weights, int[] values, int wMax) {
        validateInputs(weights, values, wMax);
        int n = weights.length;
        int[][] dp = new int[n + 1][wMax + 1];

        for (int i = 1; i <= n; i++) {
            int wi = weights[i - 1];
            int vi = values[i - 1];
            for (int w = 0; w <= wMax; w++) {
                dp[i][w] = dp[i - 1][w];
                if (wi <= w) {
                    dp[i][w] = Math.max(dp[i][w], vi + dp[i - 1][w - wi]);
                }
            }
        }

        boolean[] selected = new boolean[n];
        int w = wMax;
        for (int i = n; i >= 1; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                selected[i - 1] = true;
                w -= weights[i - 1];
            }
        }
        return selected;
    }

    private static void validateInputs(int[] weights, int[] values, int wMax) {
        Objects.requireNonNull(weights, "weights no puede ser null");
        Objects.requireNonNull(values, "values no puede ser null");
        if (weights.length != values.length) {
            throw new IllegalArgumentException("weights y values deben tener el mismo tamano");
        }
        if (wMax < 0) {
            throw new IllegalArgumentException("W no puede ser negativo");
        }
        for (int w : weights) {
            if (w <= 0) {
                throw new IllegalArgumentException("los pesos deben ser positivos");
            }
        }
    }
}
