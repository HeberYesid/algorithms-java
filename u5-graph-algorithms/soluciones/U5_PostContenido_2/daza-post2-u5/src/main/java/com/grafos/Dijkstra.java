package com.grafos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/** Dijkstra con lazy deletion y reconstruccion de camino. */
public final class Dijkstra {

    private Dijkstra() {
    }

    /** SSSP desde src. Retorna dist[] con distancias minimas. */
    public static int[] run(WeightedDiGraph g, int src) {
        int v = g.size();
        validateSource(v, src);

        int[] dist = new int[v];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[src] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, src});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int d = cur[0];
            int u = cur[1];
            if (d > dist[u]) {
                continue;
            }

            for (WeightedDiGraph.Edge e : g.neighbors(u)) {
                if (dist[u] == Integer.MAX_VALUE) {
                    continue;
                }
                int nd = dist[u] + e.weight();
                if (nd < dist[e.to()]) {
                    dist[e.to()] = nd;
                    pq.offer(new int[]{nd, e.to()});
                }
            }
        }
        return dist;
    }

    /** Camino mas corto de src a dst. Retorna vacio si dst no es alcanzable. */
    public static List<Integer> path(WeightedDiGraph g, int src, int dst) {
        int v = g.size();
        validateSource(v, src);
        validateSource(v, dst);

        int[] dist = new int[v];
        int[] parent = new int[v];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[src] = 0;

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.offer(new int[]{0, src});

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int d = cur[0];
            int u = cur[1];
            if (d > dist[u]) {
                continue;
            }
            if (u == dst) {
                break; // early termination
            }

            for (WeightedDiGraph.Edge e : g.neighbors(u)) {
                int nd = dist[u] + e.weight();
                if (nd < dist[e.to()]) {
                    dist[e.to()] = nd;
                    parent[e.to()] = u;
                    pq.offer(new int[]{nd, e.to()});
                }
            }
        }

        if (dist[dst] == Integer.MAX_VALUE) {
            return List.of();
        }
        List<Integer> p = new ArrayList<>();
        for (int cur = dst; cur != -1; cur = parent[cur]) {
            p.add(cur);
        }
        Collections.reverse(p);
        return p;
    }

    private static void validateSource(int v, int src) {
        if (src < 0 || src >= v) {
            throw new IllegalArgumentException("Vertice invalido: " + src);
        }
    }
}