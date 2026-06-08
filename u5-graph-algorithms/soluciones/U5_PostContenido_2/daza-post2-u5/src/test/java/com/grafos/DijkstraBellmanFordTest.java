package com.grafos;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class DijkstraBellmanFordTest {

    @Test
    void dijkstraRunProducesExpectedDistances() {
        WeightedDiGraph g = new WeightedDiGraph(5);
        g.addEdge(0, 1, 10);
        g.addEdge(0, 4, 5);
        g.addEdge(1, 2, 1);
        g.addEdge(1, 4, 2);
        g.addEdge(2, 3, 4);
        g.addEdge(3, 0, 7);
        g.addEdge(3, 2, 6);
        g.addEdge(4, 1, 3);
        g.addEdge(4, 2, 9);
        g.addEdge(4, 3, 2);

        int[] dist = Dijkstra.run(g, 0);
        assertArrayEquals(new int[]{0, 8, 9, 7, 5}, dist);
    }

    @Test
    void dijkstraPathReturnsEmptyWhenUnreachable() {
        WeightedDiGraph g = new WeightedDiGraph(3);
        g.addEdge(0, 1, 4);

        List<Integer> path = Dijkstra.path(g, 0, 2);
        assertTrue(path.isEmpty());
    }

    @Test
    void bellmanFordDetectsNegativeCycle() {
        int[][] edges = {
            {0, 1, 1},
            {1, 2, -1},
            {2, 1, -1}
        };
        BellmanFord.Result r = BellmanFord.run(edges, 3, 0);
        assertTrue(r.hasNegCycle());
    }

    @Test
    void bellmanFordMatchesDijkstraWithoutNegativeWeights() {
        WeightedDiGraph g = new WeightedDiGraph(5);
        int[][] edges = {
            {0, 1, 10},
            {0, 4, 5},
            {1, 2, 1},
            {1, 4, 2},
            {2, 3, 4},
            {3, 0, 7},
            {3, 2, 6},
            {4, 1, 3},
            {4, 2, 9},
            {4, 3, 2}
        };
        for (int[] e : edges) {
            g.addEdge(e[0], e[1], e[2]);
        }

        int[] dijkstra = Dijkstra.run(g, 0);
        BellmanFord.Result bellman = BellmanFord.run(edges, 5, 0);
        assertEquals(false, bellman.hasNegCycle());
        assertArrayEquals(dijkstra, bellman.dist());
    }
}