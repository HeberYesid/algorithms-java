package retrieval;

public class FenwickTree {

    private final long[] bit;
    private final int n;

    public FenwickTree(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n debe ser positivo");
        }
        this.n = n;
        this.bit = new long[n + 1];
    }

    public FenwickTree(int[] arr) {
        this(arr.length);
        for (int i = 0; i < arr.length; i++) {
            add(i + 1, arr[i]);
        }
    }

    /** Suma arr[1..i] (1-based). */
    public long prefixSum(int i) {
        long s = 0;
        for (; i > 0; i -= i & (-i)) {
            s += bit[i];
        }
        return s;
    }

    /** Suma arr[l..r] (1-based). */
    public long rangeSum(int l, int r) {
        if (l < 1 || r < l || r > n) {
            throw new IllegalArgumentException("rango invalido");
        }
        return prefixSum(r) - prefixSum(l - 1);
    }

    /** Suma delta a la posicion i (1-based). */
    public void add(int i, long delta) {
        if (i < 1 || i > n) {
            throw new IllegalArgumentException("indice fuera de rango");
        }
        for (; i <= n; i += i & (-i)) {
            bit[i] += delta;
        }
    }
}
