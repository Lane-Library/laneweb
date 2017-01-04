package edu.stanford.irt.laneweb.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.mapping.LanewebObjectMapper;
import edu.stanford.irt.laneweb.metasearch.MetaSearchService;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class MetaSearchServiceTest {

    private ObjectMapper mapper;

    private int timeout;

    private URL url;

    @Before
    public void setUp() {
        this.mapper = new LanewebObjectMapper();
        this.url = getClass().getResource("metasearchservicetest/");
    }

    @Test
    public void testClearAllCaches() throws JsonParseException, JsonMappingException, IOException {
        MetaSearchService service = new MetaSearchService(this.url, this.mapper, this.timeout);
        assertEquals("OK", service.clearAllCaches());
    }

    @Test
    public void testClearCache() throws JsonParseException, JsonMappingException, IOException {
        MetaSearchService service = new MetaSearchService(this.url, this.mapper, this.timeout);
        assertEquals("OK", service.clearCache(new SimpleQuery("the query")));
    }

    @Test
    public void testDescribe() {
        MetaSearchService service = new MetaSearchService(this.url, this.mapper, this.timeout);
        Result result = service.describe(new SimpleQuery("the query"), Collections.singleton("pubmed"));
        assertNotNull(result);
        assertEquals("description", result.getDescription());
        assertEquals("description", result.getId());
        assertEquals("none", result.getURL());
        assertEquals(1, result.getChildren().size());
    }

    @Test
    public void testSearch() {
        MetaSearchService service = new MetaSearchService(this.url, this.mapper, this.timeout);
        Result result = service.search(new SimpleQuery("the query"), Collections.singleton("pubmed"), 0);
        assertNotNull(result);
        assertEquals("metasearch", result.getDescription());
        assertNotNull(result.getId());
        assertEquals("none", result.getURL());
        assertEquals(1, result.getChildren().size());
    }
}
