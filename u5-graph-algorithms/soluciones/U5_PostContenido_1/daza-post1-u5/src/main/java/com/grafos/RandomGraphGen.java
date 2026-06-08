package com.grafos;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/** Generador de grafos no dirigidos aleatorios para benchmarks. */
public final class RandomGraphGen {

    private RandomGraphGen() {
    }

    public static Graph<Integer> buildList(int vertices, int edges) {
        Graph<Integer> g = new Graph<>();
        for (int i = 0; i < vertices; i++) {
            g.addVertex(i);
        }
        for (int[] e : generateEdges(vertices, edges)) {
            g.addEdge(e[0], e[1]);
        }
        return g;
    }

    public static MatrixGraph buildMatrix(int vertices, int edges) {
        MatrixGraph g = new MatrixGraph(vertices);
        for (int[] e : generateEdges(vertices, edges)) {
            g.addEdge(e[0], e[1]);
        }
        return g;
    }

    private static List<int[]> generateEdges(int vertices, int edges) {
        int maxEdges = vertices * (vertices - 1) / 2;
        int target = Math.max(0, Math.min(edges, maxEdges));
        Random rng = new Random(1_337L + vertices * 31L + target);

        List<int[]> list = new ArrayList<>(target);
        Set<Long> seen = new HashSet<>(target * 2);

        while (list.size() < target) {
            int u = rng.nextInt(vertices);
            int v = rng.nextInt(vertices);
            if (u == v) {
                continue;
            }
            int a = Math.min(u, v);
            int b = Math.max(u, v);
            long key = (((long) a) << 32) | (b & 0xffffffffL);
            if (seen.add(key)) {
                list.add(new int[]{a, b});
            }
        }
        return list;
    }
}