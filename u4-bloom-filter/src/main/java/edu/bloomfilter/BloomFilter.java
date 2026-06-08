package edu.bloomfilter;

import java.util.BitSet;
import java.util.function.ToIntFunction;

/**
 * Bloom filter parametrizable con doble hashing.
 *
 * <p>La estructura permite insertar elementos y consultar pertenencia probable con
 * una tasa controlada de falsos positivos.</p>
 *
 * @param <T> tipo de elementos a insertar
 */
public class BloomFilter<T> {
    private final BitSet bits;
    private final int m; // tamaño del arreglo de bits
    private final int k; // número de funciones hash
    private final ToIntFunction<T> hashFn; // función hash base

    /**
     * Construye un Bloom filter óptimo para {@code n} elementos y una tasa de
     * falsos positivos objetivo {@code epsilon}.
     *
     * <p>Las fórmulas empleadas son:</p>
     * <ul>
     *   <li>{@code m = ceil(-n * ln(epsilon) / (ln(2)^2))}</li>
     *   <li>{@code k = round((m / n) * ln(2))}</li>
     * </ul>
     *
     * @param n cantidad estimada de elementos a insertar
     * @param epsilon tasa de falsos positivos deseada
     * @param hashFn función hash base usada para derivar los hashes
     */
    public BloomFilter(int n, double epsilon, ToIntFunction<T> hashFn) {
        this.m = (int) Math.ceil(-n * Math.log(epsilon) / (Math.log(2) * Math.log(2)));
        this.k = (int) Math.max(1, Math.round((double) m / n * Math.log(2)));
        this.bits = new BitSet(m);
        this.hashFn = hashFn;
    }

    // Double hashing: h_i(x) = (h1(x) + i * h2(x)) mod m
    private int hash(int h1, int h2, int i) {
        return Math.floorMod(h1 + i * h2, m);
    }

    /**
     * Inserta un elemento en el filtro.
     *
     * @param element elemento a agregar
     */
    public void add(T element) {
        int h1 = hashFn.applyAsInt(element);
        int h2 = Integer.reverse(h1) | 1; // h2 impar para cubrir todas las posiciones
        for (int i = 0; i < k; i++) bits.set(hash(h1, h2, i));
    }

    /**
     * Indica si un elemento posiblemente pertenece al conjunto.
     *
     * <p>El resultado puede ser un falso positivo, pero nunca un falso negativo
     * para elementos previamente insertados.</p>
     *
     * @param element elemento a consultar
     * @return {@code true} si el elemento podría estar presente; {@code false} en caso contrario
     */
    public boolean mightContain(T element) {
        int h1 = hashFn.applyAsInt(element);
        int h2 = Integer.reverse(h1) | 1;
        for (int i = 0; i < k; i++) {
            if (!bits.get(hash(h1, h2, i))) return false;
        }
        return true;
    }

    /**
     * Devuelve la cantidad de bits reservados por el filtro.
     *
     * @return número total de bits
     */
    public int getBitCount() { return m; }

    /**
     * Devuelve la cantidad de funciones hash derivadas usadas por el filtro.
     *
     * @return número de hashes
     */
    public int getHashCount() { return k; }

    /**
     * Estima el consumo de memoria teórico del arreglo de bits.
     *
     * @return memoria aproximada en bytes
     */
    public long memoryBytes() { return (long) Math.ceil(m / 8.0); }
}
