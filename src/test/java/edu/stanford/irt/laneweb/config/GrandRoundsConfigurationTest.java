package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class GrandRoundsConfigurationTest {

    private GrandRoundsConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new GrandRoundsConfiguration(null);
    }

    @Test
    public void testGrandRoundsGenerator() throws IOException {
        assertNotNull(this.configuration.grandRoundsGenerator());
    }

    @Test
    public void testGrandRoundsManager() throws IOException {
        assertNotNull(this.configuration.grandRoundsService());
    }

    @Test
    public void testPresentationSAXStrategy() {
        assertNotNull(this.configuration.presentationSAXStrategy());
    }
}
