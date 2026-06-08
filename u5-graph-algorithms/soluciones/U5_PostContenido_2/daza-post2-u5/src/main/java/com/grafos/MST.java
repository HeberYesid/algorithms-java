package com.grafos;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/** Arboles de expansion minima por Kruskal y Prim. */
public final class MST {

    private MST() {
    }

    /** Kruskal - O(E log E). edges[i]={u,v,w}. */
    public static int kruskal(int[][] edges, int vertices) {
        int[][] copy = Arrays.stream(edges).map(int[]::clone).toArray(int[][]::new);
        Arrays.sort(copy, Comparator.comparingInt(e -> e[2]));

        UnionFind uf = new UnionFind(vertices);
        int total = 0;
        int used = 0;

        for (int[] e : copy) {
            if (uf.union(e[0], e[1])) {
                total += e[2];
                used++;
                if (used == vertices - 1) {
                    break;
                }
            }
        }
        return total;
    }

    /** Prim - O((V+E) log V). adj[u]={{v,w},...}. */
    public static int prim(List<List<int[]>> adj, int vertices) {
        boolean[] in = new boolean[vertices];
        int[] key = new int[vertices];
        Arrays.fill(key, Integer.MAX_VALUE);
        key[0] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, 0});

        int total = 0;
        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int w = cur[0];
            int u = cur[1];
            if (in[u]) {
                continue;
            }

            in[u] = true;
            total += w;
            for (int[] e : adj.get(u)) {
                int v = e[0];
                int vw = e[1];
                if (!in[v] && vw < key[v]) {
                    key[v] = vw;
                    pq.offer(new int[]{vw, v});
                }
            }
        }
        return total;
    }
}