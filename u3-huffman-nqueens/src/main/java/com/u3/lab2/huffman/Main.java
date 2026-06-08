package com.u3.lab2.huffman;

import com.u3.lab2.nqueens.NQueensAnalysis;

public class Main {
    public static void main(String[] args) {
        System.out.printf("%-4s %-12s %-15s %-15s %-10s%n", "N", "Solutions", "Naive Nodes", "Bitmask Nodes", "Ratio");
        System.out.println("----------------------------------------------------------------------");
        
        for (int n = 6; n <= 14; n++) {
            NQueensAnalysis.Result result = NQueensAnalysis.analyze(n);
            System.out.printf("%-4d %-12d %-15d %-15d %-10.4f%n", 
                result.n(), result.solutions(), result.nodesNaive(), result.nodesBitmask(), result.reductionRatio());
        }
    }
}
