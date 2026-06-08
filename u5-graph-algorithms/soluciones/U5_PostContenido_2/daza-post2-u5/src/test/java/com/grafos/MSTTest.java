package com.grafos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class MSTTest {

    @Test
    void kruskalAndPrimReturnSameCost() {
        int vertices = 4;
        int[][] edges = {
            {0, 1, 10},
            {0, 2, 6},
            {0, 3, 5},
            {1, 3, 15},
            {2, 3, 4}
        };

        List<List<int[]>> adj = new ArrayList<>();
        for (int i = 0; i < vertices; i++) {
            adj.add(new ArrayList<>());
        }
        for (int[] e : edges) {
            int u = e[0];
            int v = e[1];
            int w = e[2];
            adj.get(u).add(new int[]{v, w});
            adj.get(v).add(new int[]{u, w});
        }

        int kruskal = MST.kruskal(edges, vertices);
        int prim = MST.prim(adj, vertices);

        assertEquals(19, kruskal);
        assertEquals(kruskal, prim);
    }
}