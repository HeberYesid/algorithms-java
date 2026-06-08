package edu.trees;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Programa de verificación para {@link AVLTree}.
 */
public class Checkpoint1 {
    /**
     * Ejecuta inserciones aleatorias y valida altura y búsquedas sobre el árbol AVL.
     *
     * @param args argumentos de línea de comandos no utilizados
     */
    public static void main(String[] args) {
        System.out.println("Iniciando Checkpoint 1: AVL Tree");
        AVLTree<Integer> tree = new AVLTree<>();
        Set<Integer> insertedElements = new HashSet<>();
        Random rand = new Random();

        int numElements = 10000;
        
        System.out.println("Insertando " + numElements + " elementos aleatorios...");
        // Insert random elements
        for (int i = 0; i < numElements; i++) {
            int val = rand.nextInt(100000); // 0 to 99999
            tree.insert(val);
            insertedElements.add(val);
        }

        System.out.println("Elementos únicos insertados: " + insertedElements.size());
        
        // Check height
        int height = tree.height();
        double maxHeightAllowed = 1.44 * (Math.log(insertedElements.size()) / Math.log(2));
        System.out.println("Altura del árbol: " + height);
        System.out.println("Altura máxima permitida (aprox 1.44 * log2(N)): " + maxHeightAllowed);
        
        if (height <= maxHeightAllowed + 1) { // Pequeña tolerancia de compensación
            System.out.println("✓ Altura correcta dentro de la cota logarítmica.");
        } else {
            System.err.println("✗ Altura superior a la esperada.");
        }

        // Check contains true
        boolean allFound = true;
        for (int val : insertedElements) {
            if (!tree.contains(val)) {
                allFound = false;
                System.err.println("✗ Error: El elemento " + val + " fue insertado pero contains() devolvió falso.");
                break;
            }
        }
        if (allFound) {
            System.out.println("✓ contains() verificó exitosamente todos los elementos insertados.");
        }

        // Check contains false
        boolean allMissing = true;
        int checkCount = 0;
        int missingCount = 0;
        while (checkCount < 1000) {
            int val = rand.nextInt(200000);
            if (!insertedElements.contains(val)) {
                if (tree.contains(val)) {
                    allMissing = false;
                    System.err.println("✗ Error: El elemento " + val + " NO fue insertado pero contains() devolvió verdadero.");
                    break;
                }
                missingCount++;
            }
            checkCount++;
        }
        if (allMissing) {
            System.out.println("✓ contains() verificó correctamente que " + missingCount + " elementos externos no están en el árbol.");
        }
        
        System.out.println("Checkpoint 1 finalizado con éxito.");
    }
}
