package dp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class FloydWarshallTest {

    @Test
    void shortestPathsKnownGraph() {
        long i = FloydWarshall.INF;
        long[][] w = {
            {0, 3, i, 7},
            {8, 0, 2, i},
            {5, i, 0, 1},
            {2, i, i, 0}
        };

        long[][] d = FloydWarshall.solve(w);

        assertEquals(0, d[0][0]);
        assertEquals(3, d[0][1]);
        assertEquals(5, d[0][2]);
        assertEquals(6, d[0][3]);
        assertEquals(3, d[2][0]);
    }

    @Test
    void negativeCycleThrowsException() {
        long i = FloydWarshall.INF;
        long[][] w = {
            {0, 1, i},
            {i, 0, -2},
            {-2, i, 0}
        };

        assertThrows(IllegalStateException.class, () -> FloydWarshall.solve(w));
        assertTrue(FloydWarshall.hasNegativeCycle(w));
    }

    @Test
    void unreachableVerticesStayInf() {
        long i = FloydWarshall.INF;
        long[][] w = {
            {0, 4, i},
            {i, 0, i},
            {i, i, 0}
        };

        long[][] d = FloydWarshall.solve(w);
        assertEquals(i, d[0][2]);
        assertEquals(i, d[1][2]);
    }

    @Test
    void reconstructedPathMustBeValidAndMatchDistance() {
        long i = FloydWarshall.INF;
        long[][] w = {
            {0, 3, i, 7},
            {8, 0, 2, i},
            {5, i, 0, 1},
            {2, i, i, 0}
        };

        long[][] d = FloydWarshall.solve(w);
        List<Integer> path = FloydWarshall.reconstructPath(w, 0, 3);

        assertIterableEquals(List.of(0, 1, 2, 3), path);

        long cost = 0;
        for (int idx = 1; idx < path.size(); idx++) {
            cost += w[path.get(idx - 1)][path.get(idx)];
        }

        assertEquals(d[0][3], cost);
    }
}
