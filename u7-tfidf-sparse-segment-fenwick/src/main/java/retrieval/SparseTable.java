package retrieval;

public class SparseTable {

    private final int[][] table;
    private final int[] log2;
    private final int n;

    public SparseTable(int[] arr) {
        if (arr.length == 0) {
            throw new IllegalArgumentException("arr no puede estar vacio");
        }

        n = arr.length;
        int maxLog = 32 - Integer.numberOfLeadingZeros(n);
        table = new int[n][maxLog + 1];
        log2 = new int[n + 1];

        for (int i = 2; i <= n; i++) {
            log2[i] = log2[i / 2] + 1;
        }

        for (int i = 0; i < n; i++) {
            table[i][0] = arr[i];
        }

        for (int j = 1; (1 << j) <= n; j++) {
            for (int i = 0; i + (1 << j) <= n; i++) {
                table[i][j] = Math.min(table[i][j - 1], table[i + (1 << (j - 1))][j - 1]);
            }
        }
    }

    /** Minimo en arr[l..r] inclusive en O(1). */
    public int query(int l, int r) {
        if (l < 0 || r < l || r >= n) {
            throw new IllegalArgumentException("rango invalido");
        }
        int k = log2[r - l + 1];
        return Math.min(table[l][k], table[r - (1 << k) + 1][k]);
    }
}
