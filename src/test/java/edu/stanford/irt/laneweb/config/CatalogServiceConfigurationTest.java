package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Test;

public class CatalogServiceConfigurationTest {

    @Test
    public void testCatalogServiceURI() throws URISyntaxException {
        URI serviceURI = new CatalogServiceConfiguration().catalogServiceURI("scheme", "host", 100, "/path",
                "user:info");
        assertEquals(new URI("scheme://user:info@host:100/path"), serviceURI);
    }

    @Test
    public void testCatalogStatusService() throws URISyntaxException {
        assertNotNull(new CatalogServiceConfiguration().catalogStatusService(new URI("/"), null));
    }
}
