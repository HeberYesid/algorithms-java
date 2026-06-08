package com.grafos;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class BFSTest {

    @Test
    void bfsDistancesLinearGraph() {
        Graph<Integer> g = new Graph<>();
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 3);

        int[] dist = BFS.shortestDistances(g, 0);
        assertArrayEquals(new int[]{0, 1, 2, 3}, dist);
    }

    @Test
    void bfsDistancesDisconnectedGraph() {
        Graph<Integer> g = new Graph<>();
        g.addEdge(0, 1);
        g.addVertex(2);

        int[] dist = BFS.shortestDistances(g, 0);
        assertArrayEquals(new int[]{0, 1, -1}, dist);
    }

    @Test
    void bfsConnectedComponentsThreeSubgraphs() {
        Graph<Integer> g = new Graph<>();
        g.addEdge(0, 1);
        g.addEdge(2, 3);
        g.addVertex(4);

        int[] comp = BFS.connectedComponents(g);
        int components = Arrays.stream(comp).max().orElse(-1) + 1;
        assertEquals(3, components);
    }

    @Test
    void bfsPathReconstruction() {
        Graph<Integer> g = new Graph<>();
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 3);
        g.addEdge(2, 3);

        int[] parent = BFS.parentTree(g, 0);
        List<Integer> path = BFS.path(parent, 3);

        assertTrue(path.size() >= 2);
        assertEquals(0, path.get(0));
        assertEquals(3, path.get(path.size() - 1));
    }

    @Test
    void bfsPathUnreachableReturnsEmpty() {
        Graph<Integer> g = new Graph<>();
        g.addEdge(0, 1);
        g.addVertex(2);

        int[] parent = BFS.parentTree(g, 0);
        List<Integer> path = BFS.path(parent, 0, 2);
        assertTrue(path.isEmpty());
    }
}