package edu.stanford.irt.laneweb.eresources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class EresourceTest {

    @Test
    public void testSetLinks1() {
        Eresource eresource = new Eresource();
        eresource.setVersionsJson(
                "[{\"dates\":null,\"additionalText\":null,\"hasGetPasswordLink\":false,\"links\":[{\"label\":null,\"url\":\"foo\",\"additionalText\":null,\"linkText\":\"null\"}],\"publisher\":null,\"subsets\":[],\"summaryHoldings\":null,\"proxy\":true,\"holdingsAndDates\":null}]");
        assertEquals(1, eresource.getLinks().size());
        assertEquals("foo", eresource.getLinks().iterator().next().getUrl());
        assertEquals("title:null score:0.0 updated: versions:[url:foo]", eresource.toString());
    }

    @Test
    public void testSetters() {
        Eresource eresource = new Eresource();
        eresource.setId("id");
        eresource.setDescription("description");
        eresource.setPublicationAuthorsText("publicationAuthorsText");
        eresource.setPublicationText("publicationText");
        eresource.setRecordId(0);
        eresource.setScore(0);
        eresource.setTitle("title");
        eresource.setVersionsJson(
                "[{\"dates\":\"1996-2005.\",\"additionalText\":null,\"hasGetPasswordLink\":true,\"links\":[{\"label\":\"Sci Med\",\"url\":\"http://www.sciandmed.com/sm/backissues.aspx\",\"linkText\":\"v. 3-10, 1996-2005.\",\"additionalText\":\"Sci Med\"}],\"publisher\":\"Sci Med\",\"subsets\":[],\"summaryHoldings\":\"v. 3-10\",\"proxy\":true,\"holdingsAndDates\":\"v. 3-10, 1996-2005.\"},{\"dates\":\"1996-2004, [2005].\",\"additionalText\":null,\"hasGetPasswordLink\":false,\"links\":[{\"label\":\"Lane Catalog Record\",\"url\":\"http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=88164\",\"linkText\":\"v. 3-9, [10], 1996-2004, [2005].\",\"additionalText\":null}],\"publisher\":null,\"subsets\":[],\"summaryHoldings\":\"v. 3-9, [10]\",\"proxy\":true,\"holdingsAndDates\":\"v. 3-9, [10], 1996-2004, [2005].\"}]");
        eresource.setYear(0);
        assertEquals("id", eresource.getId());
        assertEquals("description", eresource.getDescription());
        assertEquals("publicationAuthorsText", eresource.getPublicationAuthorsText());
        assertEquals("publicationText", eresource.getPublicationText());
        assertEquals(0, eresource.getRecordId());
        assertEquals(0, eresource.getScore(), 0);
        assertEquals("title", eresource.getTitle());
        assertEquals(0, eresource.getYear());
        assertEquals(2, eresource.getLinks().size());
    }
}
