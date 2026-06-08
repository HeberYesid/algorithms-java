package edu.bloomfilter;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Utilidades para validar el comportamiento empírico de un Bloom filter.
 */
public class BloomFilterValidator {
    private BloomFilterValidator() {
    }

    /**
     * Mide la tasa empírica de falsos positivos del filtro para una colección
     * de elementos insertados y un número dado de consultas.
     *
     * @param filter filtro a evaluar
     * @param inserted elementos que se insertarán antes de medir
     * @param queryCount cantidad de consultas negativas a generar
     * @return tasa de falsos positivos observada
     */
    public static double measureFPRate(BloomFilter<String> filter, List<String> inserted, int queryCount) {
        // Insertar todos los elementos
        inserted.forEach(filter::add);

        // Generar queries que definitivamente NO están en el filtro
        Set<String> insertedSet = new HashSet<>(inserted);
        long fpCount = 0;
        long total = 0;
        Random rng = new Random(42);

        while (total < queryCount) {
            String query = "query_" + rng.nextLong();
            if (!insertedSet.contains(query)) {
                if (filter.mightContain(query)) fpCount++;
                total++;
            }
        }
        if (total == 0) {
            return 0.0;
        }
        return (double) fpCount / total;
    }
}
