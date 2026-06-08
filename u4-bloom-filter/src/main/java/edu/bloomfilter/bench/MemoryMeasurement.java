package edu.bloomfilter.bench;

import edu.bloomfilter.BloomFilter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Programa auxiliar para comparar el consumo de memoria entre Bloom filter y HashSet.
 */
public class MemoryMeasurement {
    private static final Logger LOGGER = Logger.getLogger(MemoryMeasurement.class.getName());
    private static final int N = 1_000_000;

    /**
     * Ejecuta una medición aproximada de memoria usando un conjunto de datos fijo.
     *
     * @param args argumentos de línea de comandos no utilizados
     */
    public static void main(String[] args) {
        LOGGER.info("=== MEDICIÓN DE MEMORIA ===");
        
        // Generar datos
        List<String> data = new ArrayList<>(N);
        Random rng = new Random(0);
        for (int i = 0; i < N; i++) {
            data.add("element-" + rng.nextLong());
        }
        
        long memBeforeBloom = getUsedMemory();
        BloomFilter<String> bloom = new BloomFilter<>(N, 0.01, String::hashCode);
        for (String s : data) {
            bloom.add(s);
        }
        long memAfterBloom = getUsedMemory();
        long bloomMemoryUsed = memAfterBloom - memBeforeBloom;
        
        long memBeforeHash = getUsedMemory();
        HashSet<String> hashSet = new HashSet<>(N);
        hashSet.addAll(data);
        long memAfterHash = getUsedMemory();
        long hashSetMemoryUsed = memAfterHash - memBeforeHash;

        LOGGER.info(() -> "Memoria BloomFilter (bytes) aprox: " + bloomMemoryUsed);
        LOGGER.info(() -> "Memoria HashSet (bytes) aprox    : " + hashSetMemoryUsed);
        LOGGER.info(() -> "Elementos en HashSet              : " + hashSet.size());
        LOGGER.info(() -> "Memoria teórica Bloom (bytes)    : " + bloom.memoryBytes());
        LOGGER.info("-----------------------------------");
    }

    /**
     * Calcula la memoria actualmente utilizada por la JVM.
     *
     * @return memoria usada en bytes
     */
    private static long getUsedMemory() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }
}
