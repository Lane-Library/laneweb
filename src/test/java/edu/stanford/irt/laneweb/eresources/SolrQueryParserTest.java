package edu.stanford.irt.laneweb.eresources;

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
        this.inspectors.add(new ORQueryInspector());
        this.inspectors.add(new EscapingQueryInspector());
        this.parser = new SolrQueryParser(this.inspectors);
    }

    @Test
    public final void testParser() {
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
        assertEquals("pmid\\:12345", this.parser.parse("pubmed:12345"));
        assertEquals("pmid\\:12345", this.parser.parse("pubmed: 12345"));
        assertEquals("pmid\\:12345", this.parser.parse("pubmed : 12345"));
        assertEquals("pmid\\:12345", this.parser.parse("pmid12345"));
        assertEquals("pmid\\:12345678", this.parser.parse("pubmed id 12345678"));
        assertEquals("pubmed id 12345678", this.parser.parse("advanced:true pubmed id 12345678"));
        assertEquals("recordId:12345678", this.parser.parse("12345678"));
        assertEquals("recordId:12345", this.parser.parse("12345"));
        assertEquals("12345 54321", this.parser.parse("12345 54321"));
        assertEquals("12345\\-54321", this.parser.parse("12345-54321"));
        assertEquals("10.1016 j.it.2015.02.003", this.parser.parse("http://dx.doi.org/10.1016/j.it.2015.02.003"));
        assertEquals("10.1016 j.it.2015.02.003", this.parser.parse("dx.doi.org/10.1016/j.it.2015.02.003"));
        assertEquals("10.1016 j.it.2015.02.003", this.parser.parse("doi.org/10.1016/j.it.2015.02.003"));
        assertEquals("BMJ 2015; 351 doi\\: 10.1136 bmj.h5942",
                this.parser.parse("BMJ 2015; 351 doi: http://dx.doi.org/10.1136/bmj.h5942"));
        assertEquals("10.1016 j.it.2015.02.003 10.1136 bmj.h5942",
                this.parser.parse("doi.org/10.1016/j.it.2015.02.003 http://dx.doi.org/10.1136/bmj.h5942"));
        assertEquals("(id:12345 OR id:123456) OR (id:12345 OR id:123456)", this.parser.parse("id:12345 OR id:123456"));
        assertEquals("(recordId:12345 OR pmid\\:123456) OR (recordId:12345 OR pmid\\:123456)",
                this.parser.parse("recordId:12345 OR pmid:123456"));
        assertEquals(
                "Journal of Antimicrobial Chemotherapy, Volume 72, Issue 4, 1 April 2017, 1147–1151, 10.1093 jac dkw537",
                this.parser.parse(
                        "Journal of Antimicrobial Chemotherapy, Volume 72, Issue 4, 1 April 2017, 1147–1151, https://doi.org/10.1093/jac/dkw537"));
    }
}
