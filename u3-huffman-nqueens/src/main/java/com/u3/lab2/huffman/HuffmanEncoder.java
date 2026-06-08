package com.u3.lab2.huffman;

import java.util.*;

/** Construcción del árbol de Huffman y generación de códigos binarios. */
public class HuffmanEncoder {
    /**
     * Construye el árbol de Huffman a partir de las frecuencias.
     * Complejidad: O(n log n) donde n es el número de símbolos únicos.
     * @param freqs mapa de carácter → frecuencia (no nulo, no vacío)
     * @return raíz del árbol de Huffman
     */
    public static HuffmanNode buildTree(Map<Character, Integer> freqs) {
        PriorityQueue<HuffmanNode> heap = new PriorityQueue<>();
        freqs.forEach((c, f) -> heap.add(new HuffmanNode(c, f, null, null)));
        while (heap.size() > 1) {
            HuffmanNode a = heap.poll();
            HuffmanNode b = heap.poll();
            // Nodo interno: símbolo nulo, frecuencia = suma
            heap.add(new HuffmanNode('\0', a.freq() + b.freq(), a, b));
        }
        return heap.poll();
    }

    /**
     * Genera el mapa de carácter → código binario (String de 0s y 1s).
     * @param root raíz del árbol de Huffman
     * @return mapa con el código de prefijo de cada símbolo
     */
    public static Map<Character, String> generateCodes(HuffmanNode root) {
        Map<Character, String> codes = new HashMap<>();
        if (root.isLeaf()) { 
            codes.put(root.symbol(), "0"); 
            return codes; 
        }
        traverse(root, "", codes);
        return codes;
    }

    private static void traverse(HuffmanNode node, String prefix, Map<Character, String> codes) {
        if (node.isLeaf()) { 
            codes.put(node.symbol(), prefix); 
            return; 
        }
        traverse(node.left(), prefix + "0", codes);
        traverse(node.right(), prefix + "1", codes);
    }
}
