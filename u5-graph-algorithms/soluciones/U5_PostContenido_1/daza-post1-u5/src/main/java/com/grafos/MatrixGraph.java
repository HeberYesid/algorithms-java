package com.grafos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Grafo no dirigido representado con matriz de adyacencia. */
public class MatrixGraph {

    private final boolean[][] adj;

    public MatrixGraph(int vertices) {
        if (vertices < 0) {
            throw new IllegalArgumentException("El numero de vertices no puede ser negativo");
        }
        this.adj = new boolean[vertices][vertices];
    }

    public int size() {
        return adj.length;
    }

    public void addEdge(int u, int v) {
        validate(u);
        validate(v);
        adj[u][v] = true;
        adj[v][u] = true;
    }

    public boolean hasEdge(int u, int v) {
        validate(u);
        validate(v);
        return adj[u][v];
    }

    public List<Integer> neighbors(int u) {
        validate(u);
        List<Integer> result = new ArrayList<>();
        for (int v = 0; v < adj.length; v++) {
            if (adj[u][v]) {
                result.add(v);
            }
        }
        return Collections.unmodifiableList(result);
    }

    private void validate(int v) {
        if (v < 0 || v >= adj.length) {
            throw new IllegalArgumentException("Vertice fuera de rango: " + v);
        }
    }
}