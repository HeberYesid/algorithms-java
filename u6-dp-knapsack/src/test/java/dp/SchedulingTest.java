package dp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dp.WeightedScheduling.Job;
import java.util.List;
import org.junit.jupiter.api.Test;

class SchedulingTest {

    @Test
    void singleJobCase() {
        List<Job> jobs = List.of(new Job(1, 3, 10));
        assertEquals(10, WeightedScheduling.solve(jobs));
    }

    @Test
    void allJobsCompatibleCase() {
        List<Job> jobs = List.of(
            new Job(1, 2, 5),
            new Job(2, 3, 7),
            new Job(3, 5, 11)
        );
        assertEquals(23, WeightedScheduling.solve(jobs));
    }

    @Test
    void weightedCanBeatGreedyByEarliestFinish() {
        List<Job> jobs = List.of(
            new Job(1, 2, 50),
            new Job(3, 4, 50),
            new Job(1, 4, 120)
        );
        assertEquals(120, WeightedScheduling.solve(jobs));
    }

    @Test
    void reconstructedJobsMatchOptimalAndAreCompatible() {
        List<Job> jobs = List.of(
            new Job(1, 3, 5),
            new Job(2, 5, 6),
            new Job(4, 6, 5),
            new Job(6, 7, 4),
            new Job(5, 8, 11),
            new Job(7, 9, 2)
        );

        List<Job> selected = WeightedScheduling.reconstructJobs(jobs);
        int value = selected.stream().mapToInt(Job::value).sum();

        for (int i = 1; i < selected.size(); i++) {
            assertTrue(selected.get(i - 1).finish() <= selected.get(i).start());
        }
        assertEquals(WeightedScheduling.solve(jobs), value);
    }
}
