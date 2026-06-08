package search;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class KMPTest {

    @Test
    void failureFunctionCheckpoint() {
        assertArrayEquals(
            new int[] {0, 0, 1, 2, 0, 1, 2, 3, 4},
            KMP.buildFailure("ABABCABAB")
        );
    }

    @Test
    void overlappingOccurrences() {
        assertEquals(List.of(0, 1, 2), KMP.search("AAAAA", "AAA"));
    }

    @Test
    void sampleSearch() {
        assertEquals(List.of(0, 3, 6), KMP.search("AABAABAABAAB", "AABA"));
    }
}
