package com.grafos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Grafo no dirigido generico con lista de adyacencia. */
public class Graph<T> {

    private final Map<T, Integer> index = new LinkedHashMap<>();
    private final List<T> vertices = new ArrayList<>();
    private final List<List<Integer>> adj = new ArrayList<>();

    /** Agrega un vertice al grafo; sin efecto si ya existe. */
    public void addVertex(T v) {
        if (!index.containsKey(v)) {
            index.put(v, vertices.size());
            vertices.add(v);
            adj.add(new ArrayList<>());
        }
    }

    /** Agrega arista no dirigida entre u y v. */
    public void addEdge(T u, T v) {
        addVertex(u);
        addVertex(v);

        int ui = index.get(u);
        int vi = index.get(v);
        if (!adj.get(ui).contains(vi)) {
            adj.get(ui).add(vi);
        }
        if (ui != vi && !adj.get(vi).contains(ui)) {
            adj.get(vi).add(ui);
        }
    }

    public int size() {
        return vertices.size();
    }

    public int indexOf(T v) {
        return index.getOrDefault(v, -1);
    }

    public T vertexAt(int idx) {
        validateIndex(idx);
        return vertices.get(idx);
    }

    public List<Integer> neighbors(int u) {
        validateIndex(u);
        return Collections.unmodifiableList(adj.get(u));
    }

    public List<T> vertices() {
        return Collections.unmodifiableList(vertices);
    }

    private void validateIndex(int idx) {
        if (idx < 0 || idx >= size()) {
            throw new IllegalArgumentException("Indice de vertice fuera de rango: " + idx);
        }
    }
}