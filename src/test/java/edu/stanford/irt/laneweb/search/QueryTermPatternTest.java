/**
 * 
 */
package edu.stanford.irt.laneweb.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author ryanmax
 */
public class QueryTermPatternTest {

    /**
     * Test method for {@link edu.stanford.irt.laneweb.search.QueryTermPattern#getPattern(java.lang.String)}.
     */
    @Test
    public final void testGetPattern() {
        assertEquals("lower\\Wcase", QueryTermPattern.getPattern("LOWER CASE").toString());
        assertEquals("replace\\Wmy\\W\\W?quotes\\W?", QueryTermPattern.getPattern("replace my \"quotes\"").toString());
        assertEquals("replace\\W\\W\\W\\W\\W", QueryTermPattern.getPattern("replace '+*& ").toString());
        assertEquals("heparin,\\Wlow\\Wmolecular\\Wweight|low\\Wmolecular\\Wweight\\Wheparin", QueryTermPattern
                .getPattern("Heparin, Low-Molecular-Weight").toString());
        assertTrue(QueryTermPattern.getPattern("Hypertension, Pulmonary").matcher("hypertension, pulmonary").find());
        assertTrue(QueryTermPattern.getPattern("Hypertension, Pulmonary").matcher("pulmonary hypertension").find());
        assertTrue(QueryTermPattern.getPattern("Hypertension, Pulmonary")
                .matcher("title with hypertension, pulmonary in it").find());
        assertTrue(QueryTermPattern.getPattern("(Hypertension, Pulmonary) AND (Bronchopulmonary Dysplasia)")
                .matcher("pulmonary hypertension in infants with bronchopulmonary dysplasia.").find());
        assertEquals(
                " in infants with",
                QueryTermPattern.getPattern("(Hypertension, Pulmonary) AND (Bronchopulmonary Dysplasia)")
                        .matcher("pulmonary hypertension in infants with bronchopulmonary dysplasia.").replaceAll(""));
    }
}
