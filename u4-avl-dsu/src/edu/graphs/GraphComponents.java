package edu.graphs;

import edu.dsu.DSU;

/**
 * Utilidades para componentes conexas sobre un grafo no dirigido representado como lista de aristas.
 *
 * Con DSU, el costo amortizado por arista es casi constante, mientras que BFS/DFS requiere
 * construir la lista de adyacencia y recorrer explícitamente todos los vértices y aristas.
 */
public class GraphComponents {
    /**
     * Cuenta componentes conexas de un grafo no dirigido.
     * @param n número de vértices (0..n-1)
     * @param edges lista de aristas [u, v]
     * @return número de componentes conexas
     */
    public static int countComponents(int n, int[][] edges) {
        DSU dsu = new DSU(n);
        for (int[] edge : edges) {
            dsu.union(edge[0], edge[1]);
        }
        return dsu.components();
    }

    /**
     * Verifica si agregar la arista (u, v) crea un ciclo.
        *
        * <p>Útil para el algoritmo de Kruskal.</p>
        *
        * @param dsu estructura DSU que modela las conexiones actuales
        * @param u primer vértice
        * @param v segundo vértice
        * @return {@code true} si la arista cerraría un ciclo
     */
    public static boolean createsCycle(DSU dsu, int u, int v) {
        if (dsu.connected(u, v)) {
            return true;
        }
        dsu.union(u, v);
        return false;
    }
}