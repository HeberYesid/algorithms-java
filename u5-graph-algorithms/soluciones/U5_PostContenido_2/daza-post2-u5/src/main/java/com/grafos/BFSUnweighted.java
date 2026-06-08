package com.grafos;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

/** BFS para grafos no ponderados representados por listas de enteros. */
public final class BFSUnweighted {

    private BFSUnweighted() {
    }

    public static int[] shortestDistances(List<List<Integer>> adj, int src) {
        int v = adj.size();
        if (src < 0 || src >= v) {
            throw new IllegalArgumentException("Vertice fuente invalido: " + src);
        }

        int[] dist = new int[v];
        Arrays.fill(dist, -1);
        Queue<Integer> q = new ArrayDeque<>();

        dist[src] = 0;
        q.offer(src);

        while (!q.isEmpty()) {
            int u = q.poll();
            for (int nxt : adj.get(u)) {
                if (dist[nxt] == -1) {
                    dist[nxt] = dist[u] + 1;
                    q.offer(nxt);
                }
            }
        }
        return dist;
    }
}