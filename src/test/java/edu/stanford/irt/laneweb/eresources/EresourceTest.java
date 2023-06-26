package edu.stanford.irt.laneweb.eresources;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.stanford.irt.laneweb.eresources.model.Eresource;

public class EresourceTest {

    @Test
    public void testSetLinks1() {
        Eresource eresource = new Eresource();
        eresource.setVersionsJson(
                "[{\"dates\":null,\"additionalText\":null,\"links\":[{\"label\":null,\"url\":\"foo\",\"additionalText\":null,\"linkText\":\"null\"}],\"publisher\":null,\"subsets\":[],\"summaryHoldings\":null,\"proxy\":true,\"holdingsAndDates\":null}]");
        assertEquals(1, eresource.getLinks().size());
        assertEquals("foo", eresource.getLinks().iterator().next().getUrl());
        assertEquals("title:null versions:[url:foo]", eresource.toString());
    }

    @Test
    public void testSetters() {
        Eresource eresource = new Eresource();
        eresource.setId("id");
        eresource.setDescription("description");
        eresource.setPublicationAuthorsText("publicationAuthorsText");
        eresource.setPublicationText("publicationText");
        eresource.setRecordId("0");
        eresource.setTitle("title");
        eresource.setVersionsJson(
                "[{\"dates\":\"1996-2005.\",\"additionalText\":null,\"links\":[{\"label\":\"Sci Med\",\"url\":\"http://www.sciandmed.com/sm/backissues.aspx\",\"linkText\":\"v. 3-10, 1996-2005.\",\"additionalText\":\"Sci Med\"}],\"publisher\":\"Sci Med\",\"subsets\":[],\"summaryHoldings\":\"v. 3-10\",\"proxy\":true,\"holdingsAndDates\":\"v. 3-10, 1996-2005.\"},{\"dates\":\"1996-2004, [2005].\",\"additionalText\":null,\"links\":[{\"label\":\"Lane Catalog Record\",\"url\":\"http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=88164\",\"linkText\":\"v. 3-9, [10], 1996-2004, [2005].\",\"additionalText\":null}],\"publisher\":null,\"subsets\":[],\"summaryHoldings\":\"v. 3-9, [10]\",\"proxy\":true,\"holdingsAndDates\":\"v. 3-9, [10], 1996-2004, [2005].\"}]");
        assertEquals("id", eresource.getId());
        assertEquals("description", eresource.getDescription());
        assertEquals("publicationAuthorsText", eresource.getPublicationAuthorsText());
        assertEquals("publicationText", eresource.getPublicationText());
        assertEquals("0", eresource.getRecordId());
        assertEquals("title", eresource.getTitle());
        assertEquals(2, eresource.getLinks().size());
    }
}
