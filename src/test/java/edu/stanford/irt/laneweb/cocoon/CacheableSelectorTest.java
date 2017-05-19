package edu.stanford.irt.laneweb.cocoon;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class CacheableSelectorTest {

    private Map<String, Object> model;

    private CacheableSelector selector;

    @Before
    public void setUp() throws Exception {
        this.selector = new CacheableSelector();
        this.model = new HashMap<>();
    }

    @Test
    public void testBasePath() {
        this.model.put(Model.BASE_PATH, "/path");
        assertTrue(this.selector.select(null, this.model, null));
    }

    @Test
    public void testBassett() {
        this.model.put(Model.SITEMAP_URI, "/biomed-resources/bassett/foo");
        assertFalse(this.selector.select(null, this.model, null));
    }

    @Test
    public void testDebug() {
        this.model.put(Model.DEBUG, Boolean.FALSE);
        assertFalse(this.selector.select(null, this.model, null));
    }

    @Test
    public void testEmrid() {
        this.model.put(Model.EMRID, "emrid");
        assertFalse(this.selector.select(null, this.model, null));
    }

    @Test
    public void testErrorDotHtml() {
        this.model.put(Model.SITEMAP_URI, "/error.html");
        assertFalse(this.selector.select(null, this.model, null));
    }

    @Test
    public void testQuery() {
        this.model.put(Model.QUERY, "query");
        assertFalse(this.selector.select(null, this.model, null));
    }

    @Test
    public void testUserId() {
        this.model.put(Model.USER_ID, "ditenus");
        assertFalse(this.selector.select(null, this.model, null));
    }
}
