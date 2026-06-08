package com.grafos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class SCCTest {

    @Test
    void kosarajuFindsThreeComponents() {
        WeightedDiGraph g = new WeightedDiGraph(5);

        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 2, 1);
        g.addEdge(1, 2, 1);
        // Nodo 4 aislado.

        List<List<Integer>> sccs = SCC.kosaraju(g);
        Set<Set<Integer>> asSets = sccs.stream()
            .map(HashSet::new)
            .collect(Collectors.toSet());

        assertEquals(3, sccs.size());
        assertTrue(asSets.contains(Set.of(0, 1)));
        assertTrue(asSets.contains(Set.of(2, 3)));
        assertTrue(asSets.contains(Set.of(4)));
    }
}