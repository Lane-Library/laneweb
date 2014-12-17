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

import edu.stanford.irt.laneweb.LanewebException;

public class QueryTranslatorTest {

    private QueryTranslator translator;

    @Before
    public void setUp() {
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
            } catch (LanewebException e) {
            }
        }
    }

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
        this.translator.processString("green \"red -blue\"");
        assertEquals("((${green} & ${\"red -blue\"})) ", this.translator.getQuery());
        this.translator.processString("\"blue and +orange\" green -purple \"blue +tan\"" );
        assertEquals("((${\"blue and +orange\"} & ${green} & ${\"blue +tan\"}))  NOT ${purple}", this.translator.getQuery());
        this.translator.processString("\"blue and +orange\" green -purple \"blue +tan" );
        assertEquals("((${\"blue} & ${and} & ${orange\" green -purple \"blue} & ${tan})) ", this.translator.getQuery());
        this.translator.processString("green - red");
        assertEquals("((${green} & ${-} & ${red})) ", this.translator.getQuery());
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
            } catch (LanewebException e) {
            }
        }
    }
}
