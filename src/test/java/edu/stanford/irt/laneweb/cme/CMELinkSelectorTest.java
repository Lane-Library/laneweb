package edu.stanford.irt.laneweb.cme;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;
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
    public void testSelectEmrid() {
        this.model.put(Model.EMRID, "emrid");
        assertTrue(this.selector.select(null, this.model, null));
    }

    @Test
    public void testSelectEmridEmpty() {
        this.model.put(Model.EMRID, "");
        assertFalse(this.selector.select(null, this.model, null));
    }

    @Test
    public void testSelectEmridNull() {
        assertFalse(this.selector.select(null, Collections.<String, Object> emptyMap(), null));
    }

    @Test
    public void testSelectLPCHEmrid() {
        this.model.put(Model.PROXY_LINKS, true);
        this.model.put(Model.IPGROUP, IPGroup.LPCH);
        this.model.put(Model.EMRID, "emrid");
        assertTrue(this.selector.select(null, this.model, null));
    }

    @Test
    public void testSelectLPCHNoEmrid() {
        this.model.put(Model.PROXY_LINKS, true);
        this.model.put(Model.IPGROUP, IPGroup.LPCH);
        assertFalse(this.selector.select(null, this.model, null));
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

    @Test
    public void testSelectSHCEmrid() {
        this.model.put(Model.PROXY_LINKS, true);
        this.model.put(Model.IPGROUP, IPGroup.SHC);
        this.model.put(Model.EMRID, "emrid");
        assertTrue(this.selector.select(null, this.model, null));
    }

    @Test
    public void testSelectSHCNoEmrid() {
        this.model.put(Model.PROXY_LINKS, true);
        this.model.put(Model.IPGROUP, IPGroup.SHC);
        assertFalse(this.selector.select(null, this.model, null));
    }

    @Test
    public void testSelectOTHERNoEmrid() {
        this.model.put(Model.PROXY_LINKS, true);
        this.model.put(Model.IPGROUP, IPGroup.OTHER);
        assertTrue(this.selector.select(null, this.model, null));
    }
}
