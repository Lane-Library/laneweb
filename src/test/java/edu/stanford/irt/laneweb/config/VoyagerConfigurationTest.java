package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class VoyagerConfigurationTest {

    private VoyagerConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new VoyagerConfiguration();
    }

    @Test
    public void testMarcXMLReader() {
        assertNotNull(this.configuration.marcXMLReader());
    }

    @Test
    public void testRESTLoginService() {
        assertNotNull(this.configuration.loginService(null, null));
    }

    @Test
    public void testVoyagerLogin() {
        assertNotNull(this.configuration.voyagerLogin(null));
    }
}
