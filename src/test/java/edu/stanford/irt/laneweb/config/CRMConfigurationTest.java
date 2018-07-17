package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class CRMConfigurationTest {

    private CRMConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new CRMConfiguration();
    }

    @Test
    public void testHttpCrmService() {
        assertNotNull(this.configuration.httpCrmService(null));
    }
}
