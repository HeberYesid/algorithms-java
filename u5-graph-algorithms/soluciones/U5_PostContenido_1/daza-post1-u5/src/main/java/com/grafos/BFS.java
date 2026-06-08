package com.grafos;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

/** Algoritmos BFS sobre grafo no dirigido. */
public final class BFS {

    private BFS() {
    }

    /**
     * BFS desde src. Retorna dist[v] con distancia minima en aristas desde src,
     * o -1 si v no es alcanzable.
     */
    public static int[] shortestDistances(Graph<?> g, int src) {
        int v = g.size();
        validateSource(v, src);

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

    /**
     * BFS desde src. Retorna parent[v] para reconstruir caminos.
     * parent[src] = -1.
     */
    public static int[] parentTree(Graph<?> g, int src) {
        int v = g.size();
        validateSource(v, src);

        int[] parent = new int[v];
        boolean[] vis = new boolean[v];
        Arrays.fill(parent, -1);

        Queue<Integer> q = new ArrayDeque<>();
        vis[src] = true;
        q.offer(src);

        while (!q.isEmpty()) {
            int u = q.poll();
            for (int nxt : g.neighbors(u)) {
                if (!vis[nxt]) {
                    vis[nxt] = true;
                    parent[nxt] = u;
                    q.offer(nxt);
                }
            }
        }
        return parent;
    }

    /**
     * Reconstruye camino desde el origen 0 al destino dst usando parent[].
     * Este overload se mantiene por compatibilidad con el enunciado.
     */
    public static List<Integer> path(int[] parent, int dst) {
        return path(parent, 0, dst);
    }

    /** Reconstruye camino minimo desde src hasta dst usando parent[] de BFS. */
    public static List<Integer> path(int[] parent, int src, int dst) {
        if (parent == null || parent.length == 0) {
            return List.of();
        }
        if (src < 0 || src >= parent.length || dst < 0 || dst >= parent.length) {
            return List.of();
        }
        if (src == dst) {
            return List.of(src);
        }
        if (parent[dst] == -1) {
            return List.of();
        }

        List<Integer> rev = new ArrayList<>();
        int cur = dst;
        while (cur != -1) {
            rev.add(cur);
            if (cur == src) {
                break;
            }
            cur = parent[cur];
            if (cur < -1 || cur >= parent.length) {
                return List.of();
            }
        }

        if (rev.get(rev.size() - 1) != src) {
            return List.of();
        }

        Collections.reverse(rev);
        return rev;
    }

    /** Etiqueta componentes conexas y retorna comp[v] con id de componente. */
    public static int[] connectedComponents(Graph<?> g) {
        int v = g.size();
        int[] comp = new int[v];
        Arrays.fill(comp, -1);

        int id = 0;
        for (int s = 0; s < v; s++) {
            if (comp[s] != -1) {
                continue;
            }
            Queue<Integer> q = new ArrayDeque<>();
            comp[s] = id;
            q.offer(s);

            while (!q.isEmpty()) {
                int u = q.poll();
                for (int nxt : g.neighbors(u)) {
                    if (comp[nxt] == -1) {
                        comp[nxt] = id;
                        q.offer(nxt);
                    }
                }
            }
            id++;
        }
        return comp;
    }

    private static void validateSource(int v, int src) {
        if (src < 0 || src >= v) {
            throw new IllegalArgumentException("Vertice fuente invalido: " + src);
        }
    }
}