package com.u3.lab2.huffman;

/**
 * Nodo del árbol de Huffman.
 * Las hojas representan símbolos; los nodos internos agrupan frecuencias.
 */
public record HuffmanNode(char symbol, int freq, HuffmanNode left, HuffmanNode right) implements Comparable<HuffmanNode> {
    /** Los nodos se ordenan por frecuencia ascendente en el min-heap. */
    @Override
    public int compareTo(HuffmanNode other) {
        return Integer.compare(this.freq, other.freq);
    }
    
    /** Retorna true si este nodo es una hoja (representa un símbolo). */
    public boolean isLeaf() { 
        return left == null && right == null; 
    }
}
