package edu.stanford.irt.laneweb.metasearch;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import edu.stanford.irt.laneweb.mapping.ResultDeserializer;
import edu.stanford.irt.search.impl.Result;

public class HTTPMetaSearchServiceTest {

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
    public void testDescribe() {
        MetaSearchService service = new HTTPMetaSearchService(this.url, this.mapper, this.timeout);
        Result result = service.describe("the query", Collections.singleton("pubmed"));
        assertNotNull(result);
        assertEquals("description", result.getDescription());
        assertEquals("description", result.getId());
        assertEquals("none", result.getURL());
        assertEquals(1, result.getChildren().size());
    }

    @Test
    public void testSearch() {
        MetaSearchService service = new HTTPMetaSearchService(this.url, this.mapper, this.timeout);
        Result result = service.search("the query", Collections.singleton("pubmed"), 0);
        assertNotNull(result);
        assertEquals("metasearch", result.getDescription());
        assertNotNull(result.getId());
        assertEquals("none", result.getURL());
        assertEquals(1, result.getChildren().size());
    }

    @Test
    public void testTestURL() {
        MetaSearchService service = new HTTPMetaSearchService(this.url, this.mapper, this.timeout);
        assertArrayEquals("test".getBytes(), service.testURL("url"));
    }
}
