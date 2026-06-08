package dp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StringDP {

    private StringDP() {
    }

    /** LCS completo - O(n*m) tiempo y espacio. */
    public static int lcsLength(String x, String y) {
        int m = x.length();
        int n = y.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = x.charAt(i - 1) == y.charAt(j - 1)
                    ? dp[i - 1][j - 1] + 1
                    : Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }
        return dp[m][n];
    }

    /** Reconstruye la cadena LCS por backtracking. */
    public static String lcsString(String x, String y) {
        int m = x.length();
        int n = y.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                dp[i][j] = x.charAt(i - 1) == y.charAt(j - 1)
                    ? dp[i - 1][j - 1] + 1
                    : Math.max(dp[i - 1][j], dp[i][j - 1]);
            }
        }

        StringBuilder sb = new StringBuilder();
        int i = m;
        int j = n;
        while (i > 0 && j > 0) {
            if (x.charAt(i - 1) == y.charAt(j - 1)) {
                sb.append(x.charAt(i - 1));
                i--;
                j--;
            } else if (dp[i - 1][j] >= dp[i][j - 1]) {
                i--;
            } else {
                j--;
            }
        }
        return sb.reverse().toString();
    }

    /** LCS con O(min(n,m)) espacio - dos filas rotatorias. */
    public static int lcsMemOpt(String x, String y) {
        if (x.length() < y.length()) {
            String tmp = x;
            x = y;
            y = tmp;
        }

        int m = x.length();
        int n = y.length();
        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                curr[j] = x.charAt(i - 1) == y.charAt(j - 1)
                    ? prev[j - 1] + 1
                    : Math.max(prev[j], curr[j - 1]);
            }
            int[] swap = prev;
            prev = curr;
            curr = swap;
            Arrays.fill(curr, 0);
        }

        return prev[n];
    }

    /** Edit Distance - O(n*m) tiempo y espacio. */
    public static int editDistance(String x, String y) {
        int m = x.length();
        int n = y.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (x.charAt(i - 1) == y.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }

        return dp[m][n];
    }

    public enum Op {
        MATCH,
        SUBSTITUTE,
        INSERT,
        DELETE
    }

    /** Reconstruye la secuencia de operaciones del alineamiento optimo. */
    public static List<Op> align(String x, String y) {
        int m = x.length();
        int n = y.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (x.charAt(i - 1) == y.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }

        List<Op> ops = new ArrayList<>();
        int i = m;
        int j = n;
        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && x.charAt(i - 1) == y.charAt(j - 1)) {
                ops.add(Op.MATCH);
                i--;
                j--;
            } else if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                ops.add(Op.SUBSTITUTE);
                i--;
                j--;
            } else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                ops.add(Op.DELETE);
                i--;
            } else {
                ops.add(Op.INSERT);
                j--;
            }
        }

        Collections.reverse(ops);
        return ops;
    }

    /** Edit Distance con O(min(n,m)) espacio. */
    public static int editDistanceMemOpt(String x, String y) {
        if (x.length() < y.length()) {
            String tmp = x;
            x = y;
            y = tmp;
        }

        int m = x.length();
        int n = y.length();
        int[] prev = new int[n + 1];
        int[] curr = new int[n + 1];

        for (int j = 0; j <= n; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= m; i++) {
            curr[0] = i;
            for (int j = 1; j <= n; j++) {
                if (x.charAt(i - 1) == y.charAt(j - 1)) {
                    curr[j] = prev[j - 1];
                } else {
                    curr[j] = 1 + Math.min(prev[j - 1], Math.min(prev[j], curr[j - 1]));
                }
            }
            int[] swap = prev;
            prev = curr;
            curr = swap;
        }

        return prev[n];
    }
}
