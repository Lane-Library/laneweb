package edu.stanford.irt.laneweb.proxy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class ProxyLinkSelectorTest {

    private ProxyLinkSelector selector;

    @Before
    public void setUp() {
        this.selector = new ProxyLinkSelector();
    }

    @Test
    public void testSelectFalse() {
        Map<String, Object> model = Collections.singletonMap(Model.PROXY_LINKS, Boolean.FALSE);
        assertFalse(this.selector.select(null, model, null));
    }

    @Test
    public void testSelectNull() {
        Map<String, Object> model = Collections.emptyMap();
        assertFalse(this.selector.select(null, model, null));
    }

    @Test
    public void testSelectTrue() {
        Map<String, Object> model = Collections.singletonMap(Model.PROXY_LINKS, Boolean.TRUE);
        assertTrue(this.selector.select(null, model, null));
    }
}
