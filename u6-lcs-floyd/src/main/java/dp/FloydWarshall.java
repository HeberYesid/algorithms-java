package dp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FloydWarshall {

    public static final long INF = Long.MAX_VALUE / 2;

    private FloydWarshall() {
    }

    /**
     * All-pairs shortest paths - O(V^3) tiempo, O(V^2) espacio.
     */
    public static long[][] solve(long[][] w) {
        validateMatrix(w);
        int v = w.length;
        long[][] dist = new long[v][v];
        for (int i = 0; i < v; i++) {
            dist[i] = w[i].clone();
        }

        for (int k = 0; k < v; k++) {
            for (int i = 0; i < v; i++) {
                if (dist[i][k] >= INF) {
                    continue;
                }
                for (int j = 0; j < v; j++) {
                    if (dist[k][j] >= INF) {
                        continue;
                    }
                    long candidate = dist[i][k] + dist[k][j];
                    if (candidate < dist[i][j]) {
                        dist[i][j] = candidate;
                    }
                }
            }
        }

        for (int i = 0; i < v; i++) {
            if (dist[i][i] < 0) {
                throw new IllegalStateException("Ciclo negativo detectado alcanzable desde el vertice " + i);
            }
        }

        return dist;
    }

    public static boolean hasNegativeCycle(long[][] w) {
        try {
            solve(w);
            return false;
        } catch (IllegalStateException ex) {
            return true;
        }
    }

    /**
     * Retorna camino minimo desde s hasta t (lista de vertices).
     */
    public static List<Integer> reconstructPath(long[][] w, int s, int t) {
        validateMatrix(w);
        int n = w.length;
        validateVertex(s, n);
        validateVertex(t, n);

        long[][] dist = new long[n][n];
        int[][] next = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                dist[i][j] = w[i][j];
                if (w[i][j] < INF) {
                    next[i][j] = j;
                } else {
                    next[i][j] = -1;
                }
            }
            next[i][i] = i;
        }

        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (dist[i][k] >= INF) {
                    continue;
                }
                for (int j = 0; j < n; j++) {
                    if (dist[k][j] >= INF) {
                        continue;
                    }
                    long candidate = dist[i][k] + dist[k][j];
                    if (candidate < dist[i][j]) {
                        dist[i][j] = candidate;
                        next[i][j] = next[i][k];
                    }
                }
            }
        }

        for (int i = 0; i < n; i++) {
            if (dist[i][i] < 0) {
                throw new IllegalStateException("Ciclo negativo detectado alcanzable desde el vertice " + i);
            }
        }

        if (next[s][t] == -1) {
            return Collections.emptyList();
        }

        List<Integer> path = new ArrayList<>();
        int current = s;
        path.add(current);

        while (current != t) {
            current = next[current][t];
            if (current == -1) {
                return Collections.emptyList();
            }
            path.add(current);
            if (path.size() > n + 1) {
                throw new IllegalStateException("Camino invalido detectado");
            }
        }

        return path;
    }

    private static void validateMatrix(long[][] w) {
        if (w == null || w.length == 0) {
            throw new IllegalArgumentException("matriz vacia");
        }
        int n = w.length;
        for (long[] row : w) {
            if (row == null || row.length != n) {
                throw new IllegalArgumentException("la matriz debe ser cuadrada");
            }
        }
    }

    private static void validateVertex(int v, int n) {
        if (v < 0 || v >= n) {
            throw new IllegalArgumentException("vertice fuera de rango");
        }
    }
}
