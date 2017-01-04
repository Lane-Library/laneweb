package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class VoyagerConfigurationTest {

    private VoyagerConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new VoyagerConfiguration(null);
    }

    @Test
    public void testMarcXMLReader() {
        assertNotNull(this.configuration.marcXMLReader());
    }

    @Test
    public void testVoyagerLogin() {
        assertNotNull(this.configuration.voyagerLogin());
    }
}
