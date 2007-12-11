/**
 * 
 */
package edu.stanford.irt.laneweb.eresources;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.TestCase;

/**
 * @author ceyates
 * 
 */
public class QueryTranslatorTest extends TestCase {

    private QueryTranslator translator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.translator = new QueryTranslator();
    }

    /**
     * Test method for
     * {@link edu.stanford.irt.laneweb.eresources.QueryTranslator#processString(java.lang.String)}.
     */
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
    
    public void testSearchTerms() throws IOException {
        InputStream stream = getClass().getResourceAsStream("search-terms.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "ASCII"));
        for (String query = reader.readLine(); null != query; query = reader.readLine()) {
            String decodedQuery = URLDecoder.decode(query,"UTF-8");
            try {
            String translatedQuery = this.translator.translate(decodedQuery);
            assertTrue(query + ": " + translatedQuery, translatedQuery.indexOf("{}") == -1);
            assertTrue(query + ": " + translatedQuery, translatedQuery.indexOf("()") == -1);
            assertTrue(query + ": " + translatedQuery, translatedQuery.indexOf(" NOT") != 0);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " " + this.translator.getQuery());
            }
        }
    }
    
    public void testMiscSearchTerms() {
        String[] terms = new String[] {"%","++++","*","-","()","{}","-*","-%"};
        for (int i = 0; i < terms.length; i++) {
            try {
            String translatedQuery = this.translator.translate(terms[i]);
            assertTrue(terms[i] + ": " + translatedQuery, translatedQuery.indexOf("{}") == -1);
            assertTrue(terms[i] + ": " + translatedQuery, translatedQuery.indexOf("()") == -1);
            assertTrue(terms[i] + ": " + translatedQuery, translatedQuery.indexOf(" NOT") != 0);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " " + this.translator.getQuery());
            }
        }
    }
    
//    public void testForOracleError() throws ClassNotFoundException, SQLException, IOException {
//        Class.forName("oracle.jdbc.driver.OracleDriver");
//        Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@171.65.56.227:1521:orcl","ceyates","phydeaux");
//        PreparedStatement stmt = conn.prepareStatement("SELECT count(*) FROM ERESOURCE2 WHERE CONTAINS(TEXT,?) > 0");
//        InputStream stream = getClass().getResourceAsStream("search-terms.txt");
//        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "ASCII"));
//        for (String query = reader.readLine(); null != query; query = reader.readLine()) {
//            String decodedQuery = URLDecoder.decode(query,"UTF-8");
//            try {
//            String translatedQuery = this.translator.translate(decodedQuery);
//            stmt.setString(1, translatedQuery);
//            ResultSet rs = stmt.executeQuery();
////            if (rs.next()) {
////                System.out.println(rs.getInt(1)  + " : " + decodedQuery + " : " + translatedQuery);
////            }
//            } catch (IllegalArgumentException e) {
//                System.out.println(e.getMessage() + " : " +  decodedQuery + " : " + this.translator.getQuery());
//            } catch (SQLException e) {
//                System.out.println(e.getMessage() + " : " +  decodedQuery + " : " + this.translator.translate(decodedQuery));
//                fail(e.getMessage() + " : " +  decodedQuery + " : " + this.translator.translate(decodedQuery));
//            }
//        }
//    }
}
