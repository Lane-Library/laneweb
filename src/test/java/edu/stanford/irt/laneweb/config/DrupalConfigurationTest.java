package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class DrupalConfigurationTest {

    private DrupalConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new DrupalConfiguration();
    }

    @Test
    public void testDrupalAPIService() {
        assertNotNull(this.configuration.drupalAPIService(null, null));
    }

    @Test
    public void testDrupalTransformer() {
        assertNotNull(this.configuration.drupalTransformer(null));
    }
}
