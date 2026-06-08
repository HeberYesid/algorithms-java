package com.grafos;

import java.util.Arrays;

/** Bellman-Ford con deteccion de ciclo negativo. */
public final class BellmanFord {

    private BellmanFord() {
    }

    public record Result(int[] dist, boolean hasNegCycle) {
    }

    /**
     * edges[i] = {u, v, w}.
     */
    public static Result run(int[][] edges, int vertices, int src) {
        if (vertices < 0) {
            throw new IllegalArgumentException("El numero de vertices no puede ser negativo");
        }
        if (src < 0 || src >= vertices) {
            throw new IllegalArgumentException("Vertice fuente invalido: " + src);
        }

        int[] dist = new int[vertices];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        for (int i = 1; i < vertices; i++) {
            boolean updated = false;
            for (int[] e : edges) {
                int u = e[0];
                int v = e[1];
                int w = e[2];
                if (dist[u] != Integer.MAX_VALUE && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                    updated = true;
                }
            }
            if (!updated) {
                break;
            }
        }

        boolean hasNeg = false;
        for (int[] e : edges) {
            int u = e[0];
            int v = e[1];
            int w = e[2];
            if (dist[u] != Integer.MAX_VALUE && dist[u] + w < dist[v]) {
                hasNeg = true;
                break;
            }
        }

        return new Result(dist, hasNeg);
    }
}