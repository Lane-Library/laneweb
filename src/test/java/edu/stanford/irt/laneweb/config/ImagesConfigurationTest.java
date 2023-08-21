package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class ImagesConfigurationTest {

    private ImagesConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new ImagesConfiguration();
    }

    @Test
    public void testBassettAccordionGenerator() {
        assertNotNull(this.configuration.bassettAccordionGenerator());
    }

    @Test
    public void testBassettGenerator() {
        assertNotNull(this.configuration.bassettGenerator());
    }

    @Test
    public void testCountSAXStrategy() {
        assertNotNull(this.configuration.countSAXStrategy());
    }

    @Test
    public void testPageSAXStrategy() {
        assertNotNull(this.configuration.pageSAXStrategy());
    }
     
}
