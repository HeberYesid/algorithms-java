package edu.graphs;

/**
 * Programa de verificación para {@link GraphComponents} y {@link edu.dsu.DSU}.
 */
public class Checkpoint2 {
    /**
     * Ejecuta una batería de comprobaciones sobre componentes conexas y detección de ciclos.
     *
     * @param args argumentos de línea de comandos no utilizados
     */
    public static void main(String[] args) {
        System.out.println("Iniciando Checkpoint 2: GraphComponents + DSU");

        int n1 = 7;
        int[][] noEdges = new int[0][2];
        assertEquals("grafo sin aristas", n1, GraphComponents.countComponents(n1, noEdges));

        int n2 = 6;
        int[][] completeLike = {
            {0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5},
            {1, 2}, {1, 3}, {1, 4}, {1, 5},
            {2, 3}, {2, 4}, {2, 5},
            {3, 4}, {3, 5},
            {4, 5}
        };
        assertEquals("grafo completo", 1, GraphComponents.countComponents(n2, completeLike));

        int n3 = 10;
        int[][] knownGraph = {
            {0, 1}, {1, 2},
            {3, 4},
            {5, 6}, {6, 7}, {7, 8}
        };
        assertEquals("grafo de 10 vértices con componentes conocidas", 4, GraphComponents.countComponents(n3, knownGraph));

        System.out.println("Verificando createsCycle con DSU...");
        edu.dsu.DSU dsu = new edu.dsu.DSU(4);
        if (GraphComponents.createsCycle(dsu, 0, 1)) {
            throw new IllegalStateException("No debería crear ciclo en la primera arista");
        }
        if (GraphComponents.createsCycle(dsu, 1, 2)) {
            throw new IllegalStateException("No debería crear ciclo en la segunda arista");
        }
        if (!GraphComponents.createsCycle(dsu, 0, 2)) {
            throw new IllegalStateException("Debería detectar ciclo al cerrar el triángulo");
        }

        System.out.println("Checkpoint 2 finalizado con éxito.");
    }

    /**
     * Verifica que el valor obtenido coincida con el esperado.
     *
     * @param label etiqueta descriptiva del caso
     * @param expected valor esperado
     * @param actual valor obtenido
     */
    private static void assertEquals(String label, int expected, int actual) {
        if (expected != actual) {
            throw new IllegalStateException(
                "Fallo en " + label + ": esperado " + expected + ", obtenido " + actual
            );
        }
        System.out.println("✓ " + label + " -> " + actual);
    }
}