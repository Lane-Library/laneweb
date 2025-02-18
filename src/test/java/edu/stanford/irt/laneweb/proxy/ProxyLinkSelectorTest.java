package edu.stanford.irt.laneweb.proxy;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.model.Model;

public class ProxyLinkSelectorTest {

    private ProxyLinkSelector selector;

    @BeforeEach
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
