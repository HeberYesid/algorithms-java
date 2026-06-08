package search;

import java.util.Arrays;

public class SuffixArray {

    private final String s;
    private final int n;
    public final int[] sa;
    public final int[] lcp;

    public SuffixArray(String s) {
        this.s = s;
        this.n = s.length();
        this.sa = buildSA();
        this.lcp = buildLCP();
    }

    private int[] buildSA() {
        if (n == 0) {
            return new int[0];
        }

        Integer[] idx = new Integer[n];
        for (int i = 0; i < n; i++) {
            idx[i] = i;
        }

        Arrays.sort(idx, (a, b) -> s.substring(a).compareTo(s.substring(b)));

        int[] out = new int[n];
        for (int i = 0; i < n; i++) {
            out[i] = idx[i];
        }
        return out;
    }

    /** Algoritmo de Kasai para construir LCP en O(n). */
    private int[] buildLCP() {
        int[] rank = new int[n];
        int[] out = new int[n];

        for (int i = 0; i < n; i++) {
            rank[sa[i]] = i;
        }

        int h = 0;
        for (int i = 0; i < n; i++) {
            int r = rank[i];
            if (r == 0) {
                continue;
            }
            int j = sa[r - 1];
            while (i + h < n && j + h < n && s.charAt(i + h) == s.charAt(j + h)) {
                h++;
            }
            out[r] = h;
            if (h > 0) {
                h--;
            }
        }

        return out;
    }

    /** Verifica si pattern ocurre en el texto en O(m log n). */
    public boolean contains(String pattern) {
        if (pattern.isEmpty()) {
            return true;
        }
        if (n == 0) {
            return false;
        }

        int lo = 0;
        int hi = n - 1;
        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            int cmp = compareSuffixWithPattern(sa[mid], pattern);
            if (cmp == 0) {
                return true;
            }
            if (cmp < 0) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return false;
    }

    private int compareSuffixWithPattern(int start, String pattern) {
        int max = Math.min(n - start, pattern.length());
        for (int i = 0; i < max; i++) {
            char a = s.charAt(start + i);
            char b = pattern.charAt(i);
            if (a != b) {
                return Character.compare(a, b);
            }
        }

        if (pattern.length() <= n - start) {
            return 0;
        }
        return -1;
    }
}
