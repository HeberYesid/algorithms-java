package com.grafos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/** Fabrica de casos aleatorios para benchmarks y pruebas comparativas. */
public final class GraphCaseFactory {

    private GraphCaseFactory() {
    }

    public static GraphPair randomUnweightedSparse(int vertices, int edges) {
        if (vertices < 0) {
            throw new IllegalArgumentException("vertices no puede ser negativo");
        }
        int maxEdges = vertices * (vertices - 1) / 2;
        int target = Math.max(0, Math.min(edges, maxEdges));

        List<List<Integer>> adj = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            adj.add(new ArrayList<>());
        }

        WeightedDiGraph weighted = new WeightedDiGraph(vertices);
        Random rng = new Random(2026L + vertices * 13L + target);
        Set<Long> seenUndirected = new HashSet<>(target * 2);

        while (seenUndirected.size() < target) {
            int u = rng.nextInt(vertices);
            int v = rng.nextInt(vertices);
            if (u == v) {
                continue;
            }
            int a = Math.min(u, v);
            int b = Math.max(u, v);
            long key = (((long) a) << 32) | (b & 0xffffffffL);
            if (seenUndirected.add(key)) {
                // Se agrega en ambos sentidos para que BFS y Dijkstra comparen en el mismo grafo.
                adj.get(a).add(b);
                adj.get(b).add(a);
                weighted.addEdge(a, b, 1);
                weighted.addEdge(b, a, 1);
            }
        }

        return new GraphPair(adj, weighted);
    }

    public record GraphPair(List<List<Integer>> unweighted, WeightedDiGraph weighted) {
    }
}