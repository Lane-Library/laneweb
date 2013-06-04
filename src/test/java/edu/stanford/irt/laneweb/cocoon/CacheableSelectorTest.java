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
        this.model = new HashMap<String, Object>();
    }

    @Test
    public void testBasePathNotStage() {
        this.model.put(Model.BASE_PATH, "/");
        assertTrue(this.selector.select(null, this.model, null));
    }

    @Test
    public void testBasePathStage() {
        this.model.put(Model.BASE_PATH, "/stage");
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
    public void testSunetid() {
        this.model.put(Model.SUNETID, "ditenus");
        assertFalse(this.selector.select(null, this.model, null));
    }
}
