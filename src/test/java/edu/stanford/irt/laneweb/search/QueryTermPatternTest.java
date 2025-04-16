/**
 *
 */
package edu.stanford.irt.laneweb.search;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;

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
        assertEquals("replace\\W\\W\\W\\W&", QueryTermPattern.getPattern("replace '+*& ").toString());
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
        assertTrue(QueryTermPattern.getPattern("Hypertension, Pulmonary")
                .matcher("title with hypertension, pulmonary in it").find());
    }

    @Test
    public final void testGetPattern8() {
        assertTrue(QueryTermPattern.getPattern("(Hypertension, Pulmonary) AND (Bronchopulmonary Dysplasia)")
                .matcher("pulmonary hypertension in infants with bronchopulmonary dysplasia.").find());
    }

    @Test
    public final void testGetPattern9() {
        assertEquals("MATCH in infants with MATCH.",
                QueryTermPattern.getPattern("(Hypertension, Pulmonary) AND (Bronchopulmonary Dysplasia)")
                        .matcher("pulmonary hypertension in infants with bronchopulmonary dysplasia.")
                        .replaceAll("MATCH"));
    }

    @Test
    public final void testGetPatternParens() {
        assertEquals("MATCH blah blah", QueryTermPattern.getPattern("(simulation) AND (laprascopy)")
                .matcher("simulation blah blah").replaceAll("MATCH"));
    }

    @Test
    public void testNullQuery() {
        assertEquals("MATCH", QueryTermPattern.getPattern(null).matcher("").replaceAll("MATCH"));
    }

    @Test
    public void testOpenSquareBracket() {
        String openSquareBracket = "atyp[ical meningiomas";
        Pattern pattern = QueryTermPattern.getPattern(openSquareBracket);
        assertTrue(pattern.matcher(openSquareBracket).matches());
    }

    @Test
    public final void testPegCPU() {
        assertEquals(
                "a_g_e_\\W_a_t_\\W_n_a_t_u_r_a_l_\\W_m_e_n_o_p_a_u_s_e_\\W_a_n_d_\\W_r_i_s_k_\\W_o_f_\\W_i_s_c_h_e_m_i_c_\\W_s_t_r_o_k_e_\\W_\\W_t_h_e_\\W_f_r_a_m_i_n_g_h_a_m_\\W_h_e_a_r_t_\\W_s_t_u_d_y_\\W_",
                QueryTermPattern.getPattern(
                        "A_g_e_ _a_t_ _n_a_t_u_r_a_l_ _m_e_n_o_p_a_u_s_e_ _a_n_d_ _r_i_s_k_ _o_f_ _i_s_c_h_e_m_i_c_ _s_t_r_o_k_e_:_ _t_h_e_ _F_r_a_m_i_n_g_h_a_m_ _h_e_a_r_t_ _s_t_u_d_y_._")
                        .toString());
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
