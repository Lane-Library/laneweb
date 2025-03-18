package edu.stanford.irt.laneweb.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PopularResourcesConfigurationTest {

    private PopularResourcesConfiguration configuration;

    @BeforeEach
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
