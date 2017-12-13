package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class GrandRoundsConfigurationTest {

    private GrandRoundsConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new GrandRoundsConfiguration();
    }

    @Test
    public void testGrandRoundsGenerator() throws IOException {
        assertNotNull(this.configuration.grandRoundsGenerator(null));
    }

    @Test
    public void testPresentationSAXStrategy() {
        assertNotNull(this.configuration.presentationSAXStrategy());
    }
}
