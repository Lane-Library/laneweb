package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

public class LanewebConfigurationTest {

    private LanewebConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new LanewebConfiguration();
    }

    @Test
    public void testComponentFactory() {
        assertNotNull(this.configuration.componentFactory());
    }

    @Test
    public void testJCacheManagerFactoryBean() throws URISyntaxException {
        assertNotNull(this.configuration.jCacheManagerFactoryBean());
    }

    @Test
    public void testMarshaller() {
        assertNotNull(this.configuration.marshaller());
    }

    @Test
    public void testModel() {
        assertNotNull(this.configuration.model());
    }
}
