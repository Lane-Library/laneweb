package edu.stanford.irt.laneweb.eresources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.eresources.QueryTranslator;

/**
 * @author ceyates
 */
public class QueryTranslatorTest {

    private QueryTranslator translator;

    @Before
    public void setUp() throws Exception {
        this.translator = new QueryTranslator();
    }

    @Test
    public void testMiscSearchTerms() {
        String[] terms = new String[] { "%", "*", "()", "{}", "-*", "-%", "#%" };
        for (String element : terms) {
            try {
                String translatedQuery = this.translator.translate(element);
                assertTrue(element + ": " + translatedQuery, translatedQuery.indexOf("{}") == -1);
                assertTrue(element + ": " + translatedQuery, translatedQuery.indexOf("()") == -1);
                assertTrue(element + ": " + translatedQuery, translatedQuery.indexOf(" NOT") != 0);
                fail(element + " " + this.translator.getQuery());
            } catch (IllegalArgumentException e) {
            }
        }
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.eresources.QueryTranslator#processString(java.lang.String)}
     * .
     */
    @Test
    public void testProcessString() {
        this.translator.processString("green  red");
        assertEquals("((${green} & ${red})) ", this.translator.getQuery());
        this.translator.processString("green red");
        assertEquals("((${green} & ${red})) ", this.translator.getQuery());
        this.translator.processString("green red -blue");
        assertEquals("((${green} & ${red}))  NOT ${blue}", this.translator.getQuery());
        this.translator.processString("green  red  -blue");
        assertEquals("((${green} & ${red}))  NOT ${blue}", this.translator.getQuery());
    }

    @Test
    public void testSearchTerms() throws IOException {
        InputStream stream = getClass().getResourceAsStream("search-terms.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "ASCII"));
        for (String query = reader.readLine(); null != query; query = reader.readLine()) {
            String decodedQuery = URLDecoder.decode(query, "UTF-8");
            try {
                String translatedQuery = this.translator.translate(decodedQuery);
                assertTrue(query + ": " + translatedQuery, translatedQuery.indexOf("{}") == -1);
                assertTrue(query + ": " + translatedQuery, translatedQuery.indexOf("()") == -1);
                assertTrue(query + ": " + translatedQuery, translatedQuery.indexOf(" NOT") != 0);
            } catch (IllegalArgumentException e) {
            }
        }
    }
    // public void testForOracleError() throws ClassNotFoundException,
    // SQLException, IOException {
    // Class.forName("oracle.jdbc.driver.OracleDriver");
    // Connection conn =
    // DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:pplane","","");
    // PreparedStatement stmt = conn.prepareStatement("SELECT count(*) FROM
    // ERESOURCE WHERE CONTAINS(TEXT,?) > 0");
    // InputStream stream = getClass().getResourceAsStream("search-terms.txt");
    // BufferedReader reader = new BufferedReader(new InputStreamReader(stream,
    // "ASCII"));
    // for (String query = reader.readLine(); null != query; query =
    // reader.readLine()) {
    // String decodedQuery = URLDecoder.decode(query,"UTF-8");
    // try {
    // String translatedQuery = this.translator.translate(decodedQuery);
    // stmt.setString(1, translatedQuery);
    // ResultSet rs = stmt.executeQuery();
    // if (rs.next()) {
    // System.out.println(rs.getInt(1) + " : " + decodedQuery + " : " +
    // translatedQuery);
    // }
    // } catch (IllegalArgumentException e) {
    // System.out.println(e.getMessage() + " : " + decodedQuery + " : " +
    // this.translator.getQuery());
    // } catch (SQLException e) {
    // System.out.println(e.getMessage() + " : " + decodedQuery + " : " +
    // this.translator.translate(decodedQuery));
    // fail(e.getMessage() + " : " + decodedQuery + " : " +
    // this.translator.translate(decodedQuery));
    // }
    // }
    // }
}
