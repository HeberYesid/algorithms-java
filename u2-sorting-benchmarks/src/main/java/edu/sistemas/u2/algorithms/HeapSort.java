package edu.sistemas.u2.algorithms;

/**
 * Ordenamiento por heap — complejidad Θ(n log n) todos los casos, O(1) espacio.
 * Localidad de caché inferior a MergeSort por accesos no secuenciales al heap.
 */
public final class HeapSort {
    private HeapSort() {}

    public static int[] sort(int[] arr) {
        int[] a = arr.clone();
        int n = a.length;
        
        // Fase 1: construir max-heap — O(n)
        for (int i = n / 2 - 1; i >= 0; i--)
            siftDown(a, n, i);
            
        // Fase 2: extraer elementos del heap — O(n log n)
        for (int i = n - 1; i > 0; i--) {
            int tmp = a[0]; 
            a[0] = a[i]; 
            a[i] = tmp;  // swap raíz con último
            siftDown(a, i, 0);
        }
        return a;
    }

    private static void siftDown(int[] a, int n, int root) {
        while (true) {
            int largest = root;
            int left = 2 * root + 1, right = 2 * root + 2;
            if (left  < n && a[left]  > a[largest]) largest = left;
            if (right < n && a[right] > a[largest]) largest = right;
            if (largest == root) break;
            
            int tmp = a[root]; 
            a[root] = a[largest]; 
            a[largest] = tmp;
            root = largest;
        }
    }
}

