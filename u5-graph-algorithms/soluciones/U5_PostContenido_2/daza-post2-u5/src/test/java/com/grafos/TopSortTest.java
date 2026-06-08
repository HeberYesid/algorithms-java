package com.grafos;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class TopSortTest {

    @Test
    void kahnReturnsValidOrderForDag() {
        int v = 6;
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < v; i++) {
            adj.add(new ArrayList<>());
        }

        add(adj, 5, 2);
        add(adj, 5, 0);
        add(adj, 4, 0);
        add(adj, 4, 1);
        add(adj, 2, 3);
        add(adj, 3, 1);

        List<Integer> order = TopSort.kahn(adj, v);
        int[] pos = new int[v];
        for (int i = 0; i < order.size(); i++) {
            pos[order.get(i)] = i;
        }

        for (int u = 0; u < v; u++) {
            for (int to : adj.get(u)) {
                assertTrue(pos[u] < pos[to]);
            }
        }
    }

    @Test
    void kahnThrowsOnCycle() {
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            adj.add(new ArrayList<>());
        }
        add(adj, 0, 1);
        add(adj, 1, 2);
        add(adj, 2, 0);

        assertThrows(IllegalArgumentException.class, () -> TopSort.kahn(adj, 3));
    }

    private static void add(List<List<Integer>> adj, int u, int v) {
        adj.get(u).add(v);
    }
}