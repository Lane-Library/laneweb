package edu.stanford.irt.laneweb.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import edu.stanford.irt.laneweb.mapping.ResultDeserializer;
import edu.stanford.irt.laneweb.metasearch.MetaSearchService;
import edu.stanford.irt.search.impl.Result;

public class MetaSearchServiceTest {

    private ObjectMapper mapper;

    private int timeout;

    private URL url;

    @Before
    public void setUp() {
        this.mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule("lane model", new Version(1, 0, 0, null, null, null));
        module.addDeserializer(Result.class, new ResultDeserializer());
        this.mapper.registerModule(module);
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
        assertEquals("OK", service.clearCache("the query"));
    }

    @Test
    public void testDescribe() {
        MetaSearchService service = new MetaSearchService(this.url, this.mapper, this.timeout);
        Result result = service.describe("the query", Collections.singleton("pubmed"));
        assertNotNull(result);
        assertEquals("description", result.getDescription());
        assertEquals("description", result.getId());
        assertEquals("none", result.getURL());
        assertEquals(1, result.getChildren().size());
    }

    @Test
    public void testSearch() {
        MetaSearchService service = new MetaSearchService(this.url, this.mapper, this.timeout);
        Result result = service.search("the query", Collections.singleton("pubmed"), 0);
        assertNotNull(result);
        assertEquals("metasearch", result.getDescription());
        assertNotNull(result.getId());
        assertEquals("none", result.getURL());
        assertEquals(1, result.getChildren().size());
    }
}
