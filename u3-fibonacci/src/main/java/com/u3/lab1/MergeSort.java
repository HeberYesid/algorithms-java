package com.u3.lab1;

/** 
 * Implementación de MergeSort — Divide y Vencerás. 
 * Complejidad temporal: Θ(n log n) en todos los casos. 
 * Complejidad espacial: O(n) espacio auxiliar. 
 */ 
public class MergeSort { 

    /** 
     * Ordena el arreglo in-place usando MergeSort. 
     * @param arr arreglo de enteros (no nulo) 
     * @post arr queda ordenado en forma no decreciente 
     */ 
    public static void sort(int[] arr) { 
        if (arr == null || arr.length <= 1) return; 
        mergeSort(arr, 0, arr.length - 1); 
    } 

    private static void mergeSort(int[] arr, int lo, int hi) { 
        if (lo >= hi) return;                     // caso base 
        int mid = lo + (hi - lo) / 2; 
        mergeSort(arr, lo, mid);                  // T(n/2) 
        mergeSort(arr, mid + 1, hi);              // T(n/2) 
        merge(arr, lo, mid, hi);                  // O(n) 
    } 

    private static void merge(int[] arr, int lo, int mid, int hi) { 
        int[] temp = new int[hi - lo + 1]; 
        int i = lo, j = mid + 1, k = 0; 
        while (i <= mid && j <= hi) {
            temp[k++] = arr[i] <= arr[j] ? arr[i++] : arr[j++]; 
        }
        while (i <= mid) temp[k++] = arr[i++]; 
        while (j <= hi)  temp[k++] = arr[j++]; 
        System.arraycopy(temp, 0, arr, lo, temp.length); 
    }
}
