package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class ProxyConfigurationTest {

    private ProxyConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new ProxyConfiguration();
    }

    @Test
    public void testElementProxyLinkTransformer() {
        assertNotNull(this.configuration.elementProxyLinkTransformer());
    }

    @Test
    public void testHtmlProxyLinkTransformer() throws IOException {
        assertNotNull(this.configuration.htmlProxyLinkTransformer(null));
    }

    @Test
    public void testProxyHostManager() throws IOException {
        assertNotNull(this.configuration.proxyHostManager(null));
    }

    @Test
    public void testProxyLinkSelector() {
        assertNotNull(this.configuration.proxyLinkSelector());
    }

    @Test
    public void testRestProxyServersService() {
        assertNotNull(this.configuration.proxyServersService(null, null));
    }
}
