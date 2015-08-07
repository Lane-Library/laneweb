package edu.stanford.irt.laneweb.solr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SolrQueryParserTest {

    @Test
    public final void test() {
        assertEquals("The biology of cancer \\[electronic resource\\]",
                SolrQueryParser.parse("The biology of cancer [electronic resource]"));
        assertEquals("Prostate cancer principles and practice. \\[1st ed.\\]",
                SolrQueryParser.parse("Prostate cancer principles and practice. [1st ed.]"));
        assertEquals(
                "Synthesis of carboxymethyl\\-cellulose based super\\-absorbent hydrogels using Co\\{sup 60\\} gamma radiation",
                SolrQueryParser
                .parse("Synthesis of carboxymethyl-cellulose based super-absorbent hydrogels using Co{sup 60} gamma radiation"));
        assertEquals("string year:[1990 TO 2000]", SolrQueryParser.parse("string year:[1990 TO 2000] advanced:true"));
        assertEquals("string year:{1990 TO 2000} string",
                SolrQueryParser.parse("advanced:true string year:{1990 TO 2000} string"));
        assertEquals("Whose life is it anyway\\?", SolrQueryParser.parse("Whose life is it anyway?"));
    }
}
