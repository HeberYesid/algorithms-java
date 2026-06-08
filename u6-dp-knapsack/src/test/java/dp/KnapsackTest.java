package dp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class KnapsackTest {

    @Test
    void whenWIsZeroThenValueIsZero() {
        int[] weights = {1, 2, 3};
        int[] values = {10, 20, 30};
        assertEquals(0, Knapsack.solve01(weights, values, 0));
        assertEquals(0, Knapsack.solveMemOpt(weights, values, 0));
    }

    @Test
    void whenNoObjectFitsThenValueIsZero() {
        int[] weights = {5, 6, 7};
        int[] values = {50, 60, 70};
        assertEquals(0, Knapsack.solve01(weights, values, 4));
    }

    @Test
    void whenAllObjectsFitThenTakeAll() {
        int[] weights = {1, 2, 3};
        int[] values = {10, 20, 30};
        assertEquals(60, Knapsack.solve01(weights, values, 10));
    }

    @Test
    void optimalSolutionCanSkipHighestSingleValueItem() {
        int[] weights = {9, 6, 6};
        int[] values = {100, 70, 70};
        assertEquals(140, Knapsack.solve01(weights, values, 12));
    }

    @Test
    void memOptMatchesClassic() {
        int[] weights = {2, 3, 4, 5};
        int[] values = {3, 4, 5, 8};
        int w = 10;
        assertEquals(Knapsack.solve01(weights, values, w), Knapsack.solveMemOpt(weights, values, w));
    }

    @Test
    void reconstructionMatchesOptimalValue() {
        int[] weights = {2, 3, 4, 5};
        int[] values = {3, 4, 5, 8};
        int w = 9;

        boolean[] selected = Knapsack.reconstruct(weights, values, w);
        int totalWeight = 0;
        int totalValue = 0;
        for (int i = 0; i < selected.length; i++) {
            if (selected[i]) {
                totalWeight += weights[i];
                totalValue += values[i];
            }
        }

        assertTrue(totalWeight <= w);
        assertEquals(Knapsack.solve01(weights, values, w), totalValue);
    }

    @Test
    void unboundedUsesItemMultipleTimes() {
        int[] weights = {5, 10, 15};
        int[] values = {10, 30, 20};
        assertEquals(60, Knapsack.solveUnbounded(weights, values, 20));
    }
}
