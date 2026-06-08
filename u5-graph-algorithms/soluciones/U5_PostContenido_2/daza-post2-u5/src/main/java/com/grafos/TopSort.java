package com.grafos;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/** Ordenamiento topologico por algoritmo de Kahn. */
public final class TopSort {

    private TopSort() {
    }

    /** Kahn - O(V+E). Lanza IllegalArgumentException si hay ciclo. */
    public static List<Integer> kahn(List<List<Integer>> adj, int vertices) {
        int[] in = new int[vertices];
        for (int u = 0; u < vertices; u++) {
            for (int v : adj.get(u)) {
                in[v]++;
            }
        }

        Queue<Integer> q = new ArrayDeque<>();
        for (int u = 0; u < vertices; u++) {
            if (in[u] == 0) {
                q.offer(u);
            }
        }

        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (int v : adj.get(u)) {
                if (--in[v] == 0) {
                    q.offer(v);
                }
            }
        }

        if (order.size() < vertices) {
            throw new IllegalArgumentException("Ciclo detectado en el grafo");
        }
        return order;
    }
}