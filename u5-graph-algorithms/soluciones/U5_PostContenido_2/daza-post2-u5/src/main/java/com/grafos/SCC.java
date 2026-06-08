package com.grafos;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/** Componentes fuertemente conexas por Kosaraju. */
public final class SCC {

    private SCC() {
    }

    /** Kosaraju - O(V+E). Retorna lista de SCCs. */
    public static List<List<Integer>> kosaraju(WeightedDiGraph g) {
        int v = g.size();
        boolean[] vis = new boolean[v];
        Deque<Integer> stack = new ArrayDeque<>();

        for (int u = 0; u < v; u++) {
            if (!vis[u]) {
                dfs1(u, g, vis, stack);
            }
        }

        WeightedDiGraph gt = g.transpose();
        Arrays.fill(vis, false);
        List<List<Integer>> sccs = new ArrayList<>();

        while (!stack.isEmpty()) {
            int u = stack.pop();
            if (!vis[u]) {
                List<Integer> scc = new ArrayList<>();
                dfs2(u, gt, vis, scc);
                sccs.add(scc);
            }
        }
        return sccs;
    }

    private static void dfs1(int u, WeightedDiGraph g, boolean[] vis, Deque<Integer> stk) {
        vis[u] = true;
        for (WeightedDiGraph.Edge e : g.neighbors(u)) {
            if (!vis[e.to()]) {
                dfs1(e.to(), g, vis, stk);
            }
        }
        stk.push(u);
    }

    private static void dfs2(int u, WeightedDiGraph g, boolean[] vis, List<Integer> scc) {
        vis[u] = true;
        scc.add(u);
        for (WeightedDiGraph.Edge e : g.neighbors(u)) {
            if (!vis[e.to()]) {
                dfs2(e.to(), g, vis, scc);
            }
        }
    }
}