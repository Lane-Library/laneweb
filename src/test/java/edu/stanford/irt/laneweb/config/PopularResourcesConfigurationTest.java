package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

public class PopularResourcesConfigurationTest {

    private PopularResourcesConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new PopularResourcesConfiguration();
    }

    @Test
    public void testBookcoverServiceURI() throws URISyntaxException {
        URI uri = this.configuration.bigqueryServiceURI("http", "localhost", 80, "/");
        assertEquals("http://localhost:80/", uri.toString());
    }

    @Test
    public void testRestBookCoverService() {
        assertNotNull(this.configuration.restBigqueryService(null, null));
    }
}
