package edu.bloomfilter;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Punto de entrada principal para verificar el funcionamiento del Bloom filter.
 */
public class Checkpoint1 {
    private static final Logger LOGGER = Logger.getLogger(Checkpoint1.class.getName());

    /**
     * Ejecuta una verificación básica del filtro: ausencia de falsos negativos
     * y medición de la tasa empírica de falsos positivos.
     *
     * @param args argumentos de línea de comandos no utilizados
     */
    public static void main(String[] args) {
        int n = 100_000;
        double epsilon = 0.01;
        
        LOGGER.info(() -> "Configurando BloomFilter con n=" + n + " y epsilon=" + epsilon);
        
        BloomFilter<String> filter = new BloomFilter<>(n, epsilon, String::hashCode);
        
        List<String> inserted = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            inserted.add("item_" + i);
        }
        
        // 1. Verificar falsos negativos
        inserted.forEach(filter::add);
        boolean hasFalseNegatives = false;
        for (String item : inserted) {
            if (!filter.mightContain(item)) {
                hasFalseNegatives = true;
                break;
            }
        }
        if (!hasFalseNegatives) {
            LOGGER.info("Check: Cero falsos negativos");
        } else {
            LOGGER.severe("Fallo: Hay falsos negativos.");
        }
        
        // 2. Medir tasa empírica de falsos positivos
        int queryCount = 200_000; // Queries
        BloomFilter<String> filter2 = new BloomFilter<>(n, epsilon, String::hashCode);
        double fpRate = BloomFilterValidator.measureFPRate(filter2, inserted, queryCount);
        
        LOGGER.info(() -> "Tasa de FP teórica esperada  : " + epsilon);
        LOGGER.info(() -> "Tasa de FP empírica obtenida : " + fpRate);
        
        if (fpRate <= 2 * epsilon) {
            LOGGER.info("Check: Falsos positivos comprobados con éxito (El margen medido es menor al doble del requerido).");
        } else {
            LOGGER.severe("Fallo: La tasa de FP excedió el límite.");
        }
    }
}