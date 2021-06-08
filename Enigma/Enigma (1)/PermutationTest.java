package enigma;

import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.Timeout;
import static org.junit.Assert.*;

import static enigma.TestUtils.*;

/** The suite of all JUnit tests for the Permutation class.
 *  @author Bon Hee Koo
 */
public class PermutationTest {

    /** Testing time limit. */
    @Rule
    public Timeout globalTimeout = Timeout.seconds(5);

    /* ***** TESTING UTILITIES ***** */

    private Permutation perm;
    private String alpha = UPPER_STRING;

    /** Check that perm has an alphabet whose size is that of
     *  FROMALPHA and TOALPHA and that maps each character of
     *  FROMALPHA to the corresponding character of FROMALPHA, and
     *  vice-versa. TESTID is used in error messages. */
    private void checkPerm(String testId,
                           String fromAlpha, String toAlpha) {
        int N = fromAlpha.length();
        assertEquals(testId + " (wrong length)", N, perm.size());
        for (int i = 0; i < N; i += 1) {
            char c = fromAlpha.charAt(i), e = toAlpha.charAt(i);
            assertEquals(msg(testId, "wrong translation of '%c'", c),
                         e, perm.permute(c));
            assertEquals(msg(testId, "wrong inverse of '%c'", e),
                         c, perm.invert(e));
            int ci = alpha.indexOf(c), ei = alpha.indexOf(e);
            assertEquals(msg(testId, "wrong translation of %d", ci),
                         ei, perm.permute(ci));
            assertEquals(msg(testId, "wrong inverse of %d", ei),
                         ci, perm.invert(ei));
        }
    }

    /* ***** TESTS ***** */

    @Test
    public void checkIdTransform() {
        perm = new Permutation("", UPPER);
        checkPerm("identity", UPPER_STRING, UPPER_STRING);
    }

    @Test
    public void testPermute() {
        Permutation p = new Permutation("(AKD) (QWERTYUI) (ZX)", UPPER);
        assertEquals(p.permute('K'), 'D');
        assertEquals(p.permute('Q'), 'W');
        assertEquals(p.permute('Z'), 'X');
    }

    @Test
    public void testInvert() {
        Permutation p = new Permutation("(KNH) (ABDFYXW) (JC)", UPPER);
        assertEquals(p.invert('W'), 'X');
        assertEquals(p.invert('H'), 'N');
        assertEquals(p.invert('J'), 'C');
    }
    Alphabet uppeR = new Alphabet("AaBbCcDdEe._");
    @Test
    public void testAlpabet() {
        Permutation upP = new Permutation("(Aa)(Bb)(Cd)(De)(Ed)(._)", uppeR);
        assertEquals('a', upP.permute('A'));
        assertEquals('_', upP.permute('.'));
    }
    @Test
    public void checkPermute2() {
        perm = new Permutation("(ABC) (EGF)", UPPER);
        assertEquals('A', perm.permute('C'));
        assertEquals('B', perm.permute('A'));
    }

    @Test
    public void checkInvert2() {
        perm = new Permutation("(QWER)(TYU)", UPPER);
        assertEquals('W', perm.invert('E'));
        assertEquals('R', perm.invert('Q'));
    }


}
