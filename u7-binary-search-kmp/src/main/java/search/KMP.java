package search;

import java.util.ArrayList;
import java.util.List;

public class KMP {

    private KMP() {
    }

    /** Construye la funcion de fallo en O(m). */
    public static int[] buildFailure(String pattern) {
        int m = pattern.length();
        if (m == 0) {
            return new int[0];
        }

        int[] f = new int[m];
        int k = 0;
        for (int i = 1; i < m; i++) {
            while (k > 0 && pattern.charAt(k) != pattern.charAt(i)) {
                k = f[k - 1];
            }
            if (pattern.charAt(k) == pattern.charAt(i)) {
                k++;
            }
            f[i] = k;
        }
        return f;
    }

    /** Retorna todas las posiciones donde pattern ocurre en text. */
    public static List<Integer> search(String text, String pattern) {
        List<Integer> results = new ArrayList<>();
        if (pattern.isEmpty()) {
            return results;
        }

        int[] f = buildFailure(pattern);
        int q = 0;
        for (int i = 0; i < text.length(); i++) {
            while (q > 0 && pattern.charAt(q) != text.charAt(i)) {
                q = f[q - 1];
            }
            if (pattern.charAt(q) == text.charAt(i)) {
                q++;
            }
            if (q == pattern.length()) {
                results.add(i - pattern.length() + 1);
                q = f[q - 1];
            }
        }
        return results;
    }
}
