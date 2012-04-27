/**
 * 
 */
package edu.stanford.irt.laneweb.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.regex.Pattern;

import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;

/**
 * @author ryanmax
 */
public class QueryTermPatternTest {

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.search.QueryTermPattern#getPattern(java.lang.String)}
     * .
     */
    @Test
    public final void testGetPattern1() {
        assertEquals("lower\\Wcase", QueryTermPattern.getPattern("LOWER CASE").toString());
    }

    @Test
    public final void testGetPattern2() {
        assertEquals("replace\\Wmy\\W\\W?quotes\\W?", QueryTermPattern.getPattern("replace my \"quotes\"").toString());
    }

    @Test
    public final void testGetPattern3() {
        assertEquals("replace\\W\\W\\W\\W\\W", QueryTermPattern.getPattern("replace '+*& ").toString());
    }

    @Test
    public final void testGetPattern4() {
        assertEquals("heparin,\\Wlow\\Wmolecular\\Wweight|low\\Wmolecular\\Wweight\\Wheparin",
                QueryTermPattern.getPattern("Heparin, Low-Molecular-Weight").toString());
    }

    @Test
    public final void testGetPattern5() {
        assertTrue(QueryTermPattern.getPattern("Hypertension, Pulmonary").matcher("hypertension, pulmonary").find());
    }

    @Test
    public final void testGetPattern6() {
        assertTrue(QueryTermPattern.getPattern("Hypertension, Pulmonary").matcher("pulmonary hypertension").find());
    }

    @Test
    public final void testGetPattern7() {
        assertTrue(QueryTermPattern.getPattern("Hypertension, Pulmonary").matcher("title with hypertension, pulmonary in it")
                .find());
    }

    @Test
    public final void testGetPattern8() {
        assertTrue(QueryTermPattern.getPattern("(Hypertension, Pulmonary) AND (Bronchopulmonary Dysplasia)")
                .matcher("pulmonary hypertension in infants with bronchopulmonary dysplasia.").find());
    }

    @Test
    public final void testGetPattern9() {
        assertEquals(" in infants with", QueryTermPattern.getPattern("(Hypertension, Pulmonary) AND (Bronchopulmonary Dysplasia)")
                .matcher("pulmonary hypertension in infants with bronchopulmonary dysplasia.").replaceAll(""));
    }

    @Test
    public void testOpenSquareBracket() {
        String openSquareBracket = "atyp[ical meningiomas";
        try {
            Pattern pattern = QueryTermPattern.getPattern(openSquareBracket);
            assertTrue(pattern.matcher(openSquareBracket).matches());
        } catch (LanewebException e) {
            fail("getPattern() should handle a String an open square bracket");
        }
    }

    @Test
    public void testQuestionMark() {
        String questionMark = "?Br J R";
        Pattern pattern = QueryTermPattern.getPattern(questionMark);
        assertTrue(pattern.matcher(questionMark).matches());
    }

    @Test
    public void testQuotesWithComma() {
        Pattern pattern = QueryTermPattern.getPattern("education, medical AND \"cognitive load\"");
        assertEquals("education,\\Wmedical|medical|\\Weducation\\W?cognitive\\Wload\\W?", pattern.toString());
    }

    @Test
    public void testTrailingBackslash() {
        String trailingBackslash = "foo\\ bar\\";
        Pattern pattern = QueryTermPattern.getPattern(trailingBackslash);
        assertTrue(pattern.matcher(trailingBackslash).matches());
    }
}
