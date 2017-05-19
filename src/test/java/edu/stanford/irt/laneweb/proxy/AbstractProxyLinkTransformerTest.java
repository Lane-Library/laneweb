package edu.stanford.irt.laneweb.proxy;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class AbstractProxyLinkTransformerTest {

    private Map<String, Object> model;

    private AbstractProxyLinkTransformer transformer;

    @Before
    public void setUp() throws Exception {
        this.transformer = new AbstractProxyLinkTransformer() {
        };
        this.model = new HashMap<>();
    }

    @Test
    public void testCreateProxyLink() {
        this.model.put(Model.BASE_PROXY_URL, "foo=");
        this.transformer.setModel(this.model);
        assertEquals("foo=bar", this.transformer.createProxyLink("bar"));
    }
}
