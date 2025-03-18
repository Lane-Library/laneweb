package edu.stanford.irt.laneweb.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;

public class CatalogServiceConfigurationTest {

    @Test
    public void testCatalogServiceURI() throws URISyntaxException {
        URI serviceURI = new CatalogServiceConfiguration().catalogServiceURI("scheme", "host", 100, "/path");
        assertEquals(new URI("scheme://host:100/path"), serviceURI);
    }

    @Test
    public void testCatalogStatusService() throws URISyntaxException {
        assertNotNull(new CatalogServiceConfiguration().catalogStatusService(new URI("/"), null));
    }
}
