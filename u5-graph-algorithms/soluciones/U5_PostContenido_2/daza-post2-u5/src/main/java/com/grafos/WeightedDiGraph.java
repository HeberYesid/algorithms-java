package com.grafos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/** Grafo dirigido ponderado con lista de adyacencia. */
public class WeightedDiGraph {

    public record Edge(int to, int weight) {
    }

    private final List<List<Edge>> adj;

    public WeightedDiGraph(int vertices) {
        if (vertices < 0) {
            throw new IllegalArgumentException("El numero de vertices no puede ser negativo");
        }
        this.adj = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public int size() {
        return adj.size();
    }

    public void addEdge(int u, int v, int w) {
        validate(u);
        validate(v);
        adj.get(u).add(new Edge(v, w));
    }

    public List<Edge> neighbors(int u) {
        validate(u);
        return Collections.unmodifiableList(adj.get(u));
    }

    /** Retorna el grafo transpuesto (aristas invertidas). */
    public WeightedDiGraph transpose() {
        WeightedDiGraph t = new WeightedDiGraph(size());
        for (int u = 0; u < size(); u++) {
            for (Edge e : adj.get(u)) {
                t.addEdge(e.to(), u, e.weight());
            }
        }
        return t;
    }

    private void validate(int u) {
        if (u < 0 || u >= size()) {
            throw new IllegalArgumentException("Vertice fuera de rango: " + u);
        }
    }
}