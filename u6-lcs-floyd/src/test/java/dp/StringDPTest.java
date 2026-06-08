package dp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;

class StringDPTest {

    @Test
    void emptyStringsForLcs() {
        assertEquals(0, StringDP.lcsLength("", "ABC"));
        assertEquals("", StringDP.lcsString("", "ABC"));
    }

    @Test
    void identicalStringsForLcs() {
        assertEquals(5, StringDP.lcsLength("HELLO", "HELLO"));
        assertEquals("HELLO", StringDP.lcsString("HELLO", "HELLO"));
    }

    @Test
    void disjointStringsForLcs() {
        assertEquals(0, StringDP.lcsLength("ABC", "DEF"));
        assertEquals("", StringDP.lcsString("ABC", "DEF"));
    }

    @Test
    void lcsStringMustBeSubsequenceOfBoth() {
        String x = "AGCAT";
        String y = "GAC";
        String lcs = StringDP.lcsString(x, y);
        assertTrue(isSubsequence(lcs, x));
        assertTrue(isSubsequence(lcs, y));
        assertEquals(StringDP.lcsLength(x, y), lcs.length());
    }

    @Test
    void lcsMemOptMatchesClassicInRandomInputs() {
        Random random = new Random(123);
        for (int t = 0; t < 5; t++) {
            String a = randomString(random, 10 + random.nextInt(191));
            String b = randomString(random, 10 + random.nextInt(191));
            assertEquals(StringDP.lcsLength(a, b), StringDP.lcsMemOpt(a, b));
        }
    }

    @Test
    void editDistanceBasicCases() {
        assertEquals(1, StringDP.editDistance("", "X"));
        assertEquals(0, StringDP.editDistance("abc", "abc"));
        assertEquals(3, StringDP.editDistance("kitten", "sitting"));
    }

    @Test
    void alignOperationCountMatchesEditDistance() {
        String x = "kitten";
        String y = "sitting";
        List<StringDP.Op> ops = StringDP.align(x, y);
        long edits = ops.stream().filter(op -> op != StringDP.Op.MATCH).count();
        assertEquals(StringDP.editDistance(x, y), edits);
    }

    @Test
    void editDistanceMemOptMatchesClassic() {
        Random random = new Random(321);
        for (int t = 0; t < 5; t++) {
            String a = randomString(random, 20 + random.nextInt(281));
            String b = randomString(random, 20 + random.nextInt(281));
            assertEquals(StringDP.editDistance(a, b), StringDP.editDistanceMemOpt(a, b));
        }
    }

    private static boolean isSubsequence(String sub, String text) {
        int i = 0;
        int j = 0;
        while (i < sub.length() && j < text.length()) {
            if (sub.charAt(i) == text.charAt(j)) {
                i++;
            }
            j++;
        }
        return i == sub.length();
    }

    private static String randomString(Random random, int len) {
        String alphabet = "ACGTXYZ";
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        }
        return sb.toString();
    }
}
