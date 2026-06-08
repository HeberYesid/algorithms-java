package search;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class SuffixArrayTest {

    @Test
    void bananaCheckpointArrays() {
        SuffixArray sa = new SuffixArray("banana");
        assertArrayEquals(new int[] {5, 3, 1, 0, 4, 2}, sa.sa);
        assertArrayEquals(new int[] {0, 1, 3, 0, 0, 2}, sa.lcp);
    }

    @Test
    void containsCheckpoint() {
        SuffixArray sa = new SuffixArray("banana");
        assertTrue(sa.contains("ana"));
        assertFalse(sa.contains("xyz"));
    }

    @Test
    void emptyTextBehavior() {
        SuffixArray sa = new SuffixArray("");
        assertTrue(sa.contains(""));
        assertFalse(sa.contains("a"));
    }
}
