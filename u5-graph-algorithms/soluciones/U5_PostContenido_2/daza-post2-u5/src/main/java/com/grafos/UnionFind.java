package com.grafos;

/** Union-Find con path splitting y union by size. */
public class UnionFind {

    private final int[] parent;
    private final int[] size;

    public UnionFind(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n no puede ser negativo");
        }
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
    }

    public int find(int x) {
        validate(x);
        while (x != parent[x]) {
            parent[x] = parent[parent[x]]; // path splitting
            x = parent[x];
        }
        return x;
    }

    public boolean union(int a, int b) {
        int ra = find(a);
        int rb = find(b);
        if (ra == rb) {
            return false;
        }
        if (size[ra] < size[rb]) {
            int tmp = ra;
            ra = rb;
            rb = tmp;
        }
        parent[rb] = ra;
        size[ra] += size[rb];
        return true;
    }

    private void validate(int x) {
        if (x < 0 || x >= parent.length) {
            throw new IllegalArgumentException("Indice fuera de rango: " + x);
        }
    }
}