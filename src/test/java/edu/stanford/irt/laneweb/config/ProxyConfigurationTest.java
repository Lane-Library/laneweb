package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class ProxyConfigurationTest {

    private ProxyConfiguration configuration;

    private DataSource dataSource;

    @Before
    public void setUp() {
        this.dataSource = createMock(DataSource.class);
        this.configuration = new ProxyConfiguration(this.dataSource);
    }

    @Test
    public void testElementProxyLinkTransformer() {
        assertNotNull(this.configuration.elementProxyLinkTransformer());
    }

    @Test
    public void testEzproxyServersWriter() throws IOException {
        assertNotNull(this.configuration.proxyServersService());
    }

    @Test
    public void testHtmlProxyLinkTransformer() throws IOException {
        assertNotNull(this.configuration.htmlProxyLinkTransformer());
    }

    @Test
    public void testProxyHostManager() throws IOException {
        assertNotNull(this.configuration.proxyHostManager());
    }

    @Test
    public void testProxyLinkSelector() {
        assertNotNull(this.configuration.proxyLinkSelector());
    }
}
