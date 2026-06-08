package dp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class WeightedScheduling {

    public record Job(int start, int finish, int value) {
        public Job {
            if (finish < start) {
                throw new IllegalArgumentException("finish debe ser >= start");
            }
        }
    }

    private WeightedScheduling() {
    }

    public static int solve(List<Job> jobs) {
        List<Job> sorted = sortJobs(jobs);
        int n = sorted.size();
        int[] p = computeP(sorted);
        int[] dp = new int[n + 1];

        for (int j = 1; j <= n; j++) {
            int include = sorted.get(j - 1).value() + dp[p[j]];
            int exclude = dp[j - 1];
            dp[j] = Math.max(include, exclude);
        }
        return dp[n];
    }

    /**
     * p[j] = ultimo trabajo compatible con j (termina <= start_j).
     */
    static int[] computeP(List<Job> sortedJobs) {
        int n = sortedJobs.size();
        int[] finishes = sortedJobs.stream().mapToInt(Job::finish).toArray();
        int[] p = new int[n + 1];

        for (int j = 1; j <= n; j++) {
            int start = sortedJobs.get(j - 1).start();
            int lo = 0;
            int hi = j - 1;
            while (lo < hi) {
                int mid = (lo + hi + 1) >>> 1;
                if (finishes[mid - 1] <= start) {
                    lo = mid;
                } else {
                    hi = mid - 1;
                }
            }
            p[j] = lo;
        }
        return p;
    }

    public static List<Job> reconstructJobs(List<Job> jobs) {
        List<Job> sorted = sortJobs(jobs);
        int n = sorted.size();
        int[] p = computeP(sorted);
        int[] dp = new int[n + 1];

        for (int j = 1; j <= n; j++) {
            int include = sorted.get(j - 1).value() + dp[p[j]];
            int exclude = dp[j - 1];
            dp[j] = Math.max(include, exclude);
        }

        List<Job> chosen = new ArrayList<>();
        int j = n;
        while (j > 0) {
            int include = sorted.get(j - 1).value() + dp[p[j]];
            int exclude = dp[j - 1];
            if (include >= exclude) {
                chosen.add(sorted.get(j - 1));
                j = p[j];
            } else {
                j--;
            }
        }

        chosen.sort(Comparator.comparingInt(Job::finish));
        return chosen;
    }

    private static List<Job> sortJobs(List<Job> jobs) {
        Objects.requireNonNull(jobs, "jobs no puede ser null");
        List<Job> sorted = new ArrayList<>(jobs);
        sorted.sort(Comparator.comparingInt(Job::finish));
        return sorted;
    }
}
