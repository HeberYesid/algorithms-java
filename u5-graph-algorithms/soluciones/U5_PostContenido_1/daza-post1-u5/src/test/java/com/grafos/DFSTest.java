package com.grafos;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class DFSTest {

    @Test
    void dfsDetectsCycle() {
        Graph<Integer> g = new Graph<>();
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 0);

        DFS dfs = new DFS(g);
        assertTrue(dfs.hasCycle);
    }

    @Test
    void dfsNoCycleInTree() {
        Graph<Integer> g = new Graph<>();
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 3);

        DFS dfs = new DFS(g);
        assertFalse(dfs.hasCycle);
        for (int i = 0; i < g.size(); i++) {
            assertTrue(dfs.disc[i] < dfs.fin[i]);
        }
    }

    @Test
    void dfsClassifiesEdges() {
        Graph<Integer> g = new Graph<>();
        g.addEdge(0, 1);
        g.addEdge(1, 2);
        g.addEdge(2, 3);

        DFS dfs = new DFS(g);
        assertTrue(dfs.edgeTypes.containsValue(DFS.EdgeType.TREE));
    }
}