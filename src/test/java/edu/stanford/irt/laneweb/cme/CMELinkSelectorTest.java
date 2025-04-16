package edu.stanford.irt.laneweb.cme;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import edu.stanford.irt.laneweb.model.Model;

public class CMELinkSelectorTest {

    private Map<String, Object> model;

    private CMELinkSelector selector;

    @BeforeEach
    public void setUp() throws Exception {
        this.selector = new CMELinkSelector();
        this.model = new HashMap<>();
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
        assertFalse(this.selector.select(null, Collections.emptyMap(), null));
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
    public void testSelectOTHERNoEmrid() {
        this.model.put(Model.PROXY_LINKS, true);
        this.model.put(Model.IPGROUP, IPGroup.OTHER);
        assertTrue(this.selector.select(null, this.model, null));
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
}
