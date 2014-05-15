package edu.stanford.irt.laneweb.cme;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class CMELinkSelectorTest {

    private Map<String, Object> model;

    private CMELinkSelector selector;

    @Before
    public void setUp() throws Exception {
        this.selector = new CMELinkSelector();
        this.model = new HashMap<String, Object>();
    }

    @Test
    public void testSelectEmptyEmrid() {
        this.model.put(Model.EMRID, "");
        assertFalse(this.selector.select(null, this.model, null));
    }

    @Test
    public void testSelectEmrid() {
        this.model.put(Model.EMRID, "emrid");
        assertTrue(this.selector.select(null, this.model, null));
    }

    @Test
    public void testSelectNoProxyLinks() {
        this.model.put(Model.PROXY_LINKS, false);
        assertFalse(this.selector.select(null, this.model, null));
    }

    @Test
    public void testSelectProxyLinks() {
        this.model.put(Model.PROXY_LINKS, true);
        assertTrue(this.selector.select(null, this.model, null));
    }
}
