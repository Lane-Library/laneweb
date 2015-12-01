package edu.stanford.irt.laneweb.solr;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SolrQueryParserTest {

    List<QueryInspector> inspectors;

    SolrQueryParser parser;

    @Before
    public void setUp() throws Exception {
        this.inspectors = new ArrayList<>();
        this.inspectors.add(new AdvancedQueryInspector());
        this.inspectors.add(new DoiQueryInspector());
        this.inspectors.add(new PmidQueryInspector());
        this.inspectors.add(new NumberQueryInspector());
        this.inspectors.add(new EscapingQueryInspector());
        this.parser = new SolrQueryParser(this.inspectors);
    }

    @Test
    public final void test() {
        assertEquals("The biology of cancer \\[electronic resource\\]",
                this.parser.parse("The biology of cancer [electronic resource]"));
        assertEquals("Prostate cancer principles and practice. \\[1st ed.\\]",
                this.parser.parse("Prostate cancer principles and practice. [1st ed.]"));
        assertEquals(
                "Synthesis of carboxymethyl\\-cellulose based super\\-absorbent hydrogels using Co\\{sup 60\\} gamma radiation",
                this.parser.parse(
                        "Synthesis of carboxymethyl-cellulose based super-absorbent hydrogels using Co{sup 60} gamma radiation"));
        assertEquals("string year:[1990 TO 2000]", this.parser.parse("string year:[1990 TO 2000] advanced:true"));
        assertEquals("string year:{1990 TO 2000} string",
                this.parser.parse("advanced:true string year:{1990 TO 2000} string"));
        assertEquals("Whose life is it anyway\\?", this.parser.parse("Whose life is it anyway?"));
        assertEquals("pmid\\:12345", this.parser.parse("pmid 12345"));
        assertEquals("pmid\\:12345", this.parser.parse("pmid:12345"));
        assertEquals("pmid\\:12345", this.parser.parse("pmid : 12345"));
        assertEquals("pmid\\:12345", this.parser.parse("pmid12345"));
        assertEquals("pmid\\:12345678", this.parser.parse("pubmed id 12345678"));
        assertEquals("pubmed id 12345678", this.parser.parse("advanced:true pubmed id 12345678"));
        assertEquals("id:pubmed-12345678 OR id:bib-12345678", this.parser.parse("12345678"));
        assertEquals("id:pubmed-12345 OR id:bib-12345", this.parser.parse("12345"));
        assertEquals("12345 54321", this.parser.parse("12345 54321"));
        assertEquals("12345\\-54321", this.parser.parse("12345-54321"));
        assertEquals("10.1016/j.it.2015.02.003", this.parser.parse("http://dx.doi.org/10.1016/j.it.2015.02.003"));
        assertEquals("10.1016/j.it.2015.02.003", this.parser.parse("dx.doi.org/10.1016/j.it.2015.02.003"));
        assertEquals("10.1016/j.it.2015.02.003", this.parser.parse("doi.org/10.1016/j.it.2015.02.003"));
        assertEquals("BMJ 2015; 351 doi: 10.1136/bmj.h5942",
                this.parser.parse("BMJ 2015; 351 doi: http://dx.doi.org/10.1136/bmj.h5942"));
        assertEquals("10.1016/j.it.2015.02.003 10.1136/bmj.h5942",
                this.parser.parse("doi.org/10.1016/j.it.2015.02.003 http://dx.doi.org/10.1136/bmj.h5942"));
    }
}
