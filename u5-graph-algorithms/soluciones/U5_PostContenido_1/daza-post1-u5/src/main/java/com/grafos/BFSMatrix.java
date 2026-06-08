package com.grafos;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Queue;

/** BFS para la representacion MatrixGraph. */
public final class BFSMatrix {

    private BFSMatrix() {
    }

    public static int[] run(MatrixGraph g, int src) {
        int v = g.size();
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
            for (int nxt : g.neighbors(u)) {
                if (dist[nxt] == -1) {
                    dist[nxt] = dist[u] + 1;
                    q.offer(nxt);
                }
            }
        }
        return dist;
    }
}