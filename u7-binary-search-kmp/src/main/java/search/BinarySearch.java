package search;

import java.util.function.LongPredicate;

public class BinarySearch {

    private BinarySearch() {
    }

    /** Retorna el indice del primer elemento >= key. */
    public static int lowerBound(int[] arr, int key) {
        int lo = 0;
        int hi = arr.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] < key) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    /** Retorna el indice del primer elemento > key. */
    public static int upperBound(int[] arr, int key) {
        int lo = 0;
        int hi = arr.length;
        while (lo < hi) {
            int mid = lo + (hi - lo) / 2;
            if (arr[mid] <= key) {
                lo = mid + 1;
            } else {
                hi = mid;
            }
        }
        return lo;
    }

    public static int countOccurrences(int[] arr, int key) {
        return upperBound(arr, key) - lowerBound(arr, key);
    }

    /**
     * Minimo x en [lo, hi] tal que predicate(x)=true.
     * Precondicion: predicate monotona false...false, true...true.
     */
    public static long bisectAnswer(long lo, long hi, LongPredicate predicate) {
        while (lo < hi) {
            long mid = lo + (hi - lo) / 2;
            if (predicate.test(mid)) {
                hi = mid;
            } else {
                lo = mid + 1;
            }
        }
        return lo;
    }
}
