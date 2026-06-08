package edu.dsu;

/**
 * Disjoint Set Union con path splitting y union by size.
 *
 * <p>La estructura mantiene componentes disjuntas de forma eficiente para consultas de
 * conectividad y uniones incrementales.</p>
 */
public class DSU {
    private final int[] parent;
    private final int[] size;
    private int components;

    /**
     * Crea una estructura DSU con {@code n} elementos inicialmente separados.
     *
     * @param n cantidad de elementos
     */
    public DSU(int n) {
        parent = new int[n];
        size = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            size[i] = 1;
        }
        components = n;
    }

    /**
     * Busca el representante del conjunto de {@code x} usando path splitting.
     *
     * @param x elemento consultado
     * @return representante del conjunto al que pertenece {@code x}
     */
    public int find(int x) {
        while (parent[x] != x) {
            int next = parent[x];
            parent[x] = parent[next];
            x = next;
        }
        return x;
    }

    /**
     * Une los conjuntos de {@code x} e {@code y} usando union by size.
     *
     * @param x primer elemento
     * @param y segundo elemento
     * @return {@code true} si los conjuntos eran distintos y se fusionaron
     */
    public boolean union(int x, int y) {
        int rx = find(x);
        int ry = find(y);
        if (rx == ry) return false;
        if (size[rx] < size[ry]) {
            int t = rx;
            rx = ry;
            ry = t;
        }
        parent[ry] = rx;
        size[rx] += size[ry];
        components--;
        return true;
    }

    /**
     * Indica si {@code x} e {@code y} pertenecen al mismo conjunto.
     *
     * @param x primer elemento
     * @param y segundo elemento
     * @return {@code true} si ambos elementos están conectados
     */
    public boolean connected(int x, int y) {
        return find(x) == find(y);
    }

    /**
     * Devuelve la cantidad de componentes disjuntas actuales.
     *
     * @return número de componentes
     */
    public int components() {
        return components;
    }
}
