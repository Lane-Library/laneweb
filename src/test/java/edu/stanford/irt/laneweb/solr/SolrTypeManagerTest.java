package edu.stanford.irt.laneweb.solr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SolrTypeManagerTest {

    @Test
    public final void testConvertToNewType() {
        assertEquals("Journal", SolrTypeManager.convertToNewType("ej"));
        assertEquals("Unknown Type", SolrTypeManager.convertToNewType("unknown type"));
    }

    @Test
    public final void testConvertToOldType() {
        assertEquals("ej", SolrTypeManager.convertToOldType("Journal"));
        assertEquals("journaldigital", SolrTypeManager.convertToOldType("Journal Digital"));
        assertEquals("unknown type", SolrTypeManager.convertToOldType("Unknown Type"));
    }
}
