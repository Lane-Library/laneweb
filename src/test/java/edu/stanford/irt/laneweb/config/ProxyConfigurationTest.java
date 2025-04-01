package edu.stanford.irt.laneweb.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProxyConfigurationTest {

    private ProxyConfiguration configuration;

    @BeforeEach
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
        assertNotNull(this.configuration.proxyServersService(null));
    }
}
