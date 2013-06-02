package edu.stanford.irt.laneweb.cocoon;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;


public class NotCacheableSelectorTest {
    
    private NotCacheableSelector selector;
    
    private Map<String, Object> model;

    @Before
    public void setUp() throws Exception {
        this.selector = new NotCacheableSelector();
        this.model = new HashMap<String, Object>();
    }

    @Test
    public void testSunetid() {
        this.model.put(Model.SUNETID, "ditenus");
        assertTrue(this.selector.select(null, this.model, null));
    }

    @Test
    public void testQuery() {
        this.model.put(Model.QUERY, "query");
        assertTrue(this.selector.select(null, this.model, null));
    }

    @Test
    public void testEmrid() {
        this.model.put(Model.EMRID, "emrid");
        assertTrue(this.selector.select(null, this.model, null));
    }

    @Test
    public void testBasePathStage() {
        this.model.put(Model.BASE_PATH, "/stage");
        assertTrue(this.selector.select(null, this.model, null));
    }

    @Test
    public void testDebug() {
        this.model.put(Model.DEBUG, Boolean.FALSE);
        assertTrue(this.selector.select(null, this.model, null));
    }

    @Test
    public void testBasePathNotStage() {
        this.model.put(Model.BASE_PATH, "/");
        assertFalse(this.selector.select(null, this.model, null));
    }
}
