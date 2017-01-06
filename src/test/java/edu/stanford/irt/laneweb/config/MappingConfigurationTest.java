package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class MappingConfigurationTest {

    private MappingConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new MappingConfiguration();
    }

    @Test
    public void testJacksonSerializationConfig() {
        assertNotNull(this.configuration.jacksonSerializationConfig());
    }

    @Test
    public void testLanewebObjectMapper() {
        assertNotNull(this.configuration.lanewebObjectMapper());
    }
}
