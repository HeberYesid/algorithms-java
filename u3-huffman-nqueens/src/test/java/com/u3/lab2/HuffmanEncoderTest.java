package com.u3.lab2;

import com.u3.lab2.huffman.HuffmanEncoder;
import com.u3.lab2.huffman.HuffmanNode;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HuffmanEncoderTest {

    @Test
    void testHuffmanCodes() {
        Map<Character, Integer> freqs = new HashMap<>();
        freqs.put('a', 2);
        freqs.put('b', 3);
        freqs.put('c', 4);

        HuffmanNode root = HuffmanEncoder.buildTree(freqs);
        assertNotNull(root);
        Map<Character, String> codes = HuffmanEncoder.generateCodes(root);

        // a) Verificar longitudes esperadas
        int totalLength = 0;
        for (Map.Entry<Character, Integer> entry : freqs.entrySet()) {
            totalLength += entry.getValue() * codes.get(entry.getKey()).length();
        }
        assertEquals(14, totalLength, "La longitud total óptima debe ser 14 bits");

        // b) Ningún código es prefijo de otro
        for (String c1 : codes.values()) {
            for (String c2 : codes.values()) {
                if (!c1.equals(c2)) {
                    assertFalse(c1.startsWith(c2), "Código de prefijo inválido: " + c2 + " es prefijo de " + c1);
                }
            }
        }

        // c) Longitud total mucho menor que en ASCII
        int asciiLength = 9 * 8; // 9 caracteres en aabbbcccc
        assertTrue(totalLength < asciiLength, "Debe ser menor que longitud ASCII");
    }
}
