package edu.stanford.irt.laneweb.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ImagesConfigurationTest {

    private ImagesConfiguration configuration;

    @BeforeEach
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
