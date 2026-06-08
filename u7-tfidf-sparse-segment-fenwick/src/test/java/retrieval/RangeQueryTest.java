package retrieval;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class RangeQueryTest {

    @Test
    void sparseTableCheckpoint() {
        SparseTable st = new SparseTable(new int[] {3, 1, 4, 1, 5, 9, 2, 6});
        assertEquals(1, st.query(1, 5));
    }

    @Test
    void segmentTreeCheckpoint() {
        SegmentTree seg = new SegmentTree(new int[] {1, 2, 3, 4, 5});
        assertEquals(9, seg.query(1, 3));
        seg.update(2, 10);
        assertEquals(19, seg.query(1, 3));
    }

    @Test
    void fenwickCheckpoint() {
        FenwickTree bit = new FenwickTree(new int[] {1, 2, 3, 4, 5});
        assertEquals(9, bit.rangeSum(2, 4));
        bit.add(3, 10);
        assertEquals(19, bit.rangeSum(2, 4));
    }
}
