package com.grafos;

import java.util.LinkedHashMap;
import java.util.Map;

/** DFS con tiempos de descubrimiento/finalizacion y clasificacion de aristas. */
public class DFS {

    public enum EdgeType {
        TREE,
        BACK,
        FORWARD,
        CROSS
    }

    public final int[] disc;
    public final int[] fin;
    public final boolean hasCycle;
    public final Map<String, EdgeType> edgeTypes = new LinkedHashMap<>();

    private final int[] color; // 0=white, 1=gray, 2=black
    private int timer;
    private boolean cycleFound;

    public DFS(Graph<?> g) {
        int v = g.size();
        this.disc = new int[v];
        this.fin = new int[v];
        this.color = new int[v];

        for (int u = 0; u < v; u++) {
            if (color[u] == 0) {
                visit(u, -1, g);
            }
        }
        this.hasCycle = cycleFound;
    }

    private void visit(int u, int parent, Graph<?> g) {
        color[u] = 1;
        disc[u] = ++timer;

        for (int v : g.neighbors(u)) {
            String key = u + "->" + v;
            if (color[v] == 0) {
                edgeTypes.put(key, EdgeType.TREE);
                visit(v, u, g);
            } else if (color[v] == 1) {
                if (v != parent) {
                    edgeTypes.put(key, EdgeType.BACK);
                    cycleFound = true;
                } else {
                    edgeTypes.putIfAbsent(key, EdgeType.CROSS);
                }
            } else {
                EdgeType t = disc[u] < disc[v] ? EdgeType.FORWARD : EdgeType.CROSS;
                edgeTypes.put(key, t);
            }
        }

        color[u] = 2;
        fin[u] = ++timer;
    }
}