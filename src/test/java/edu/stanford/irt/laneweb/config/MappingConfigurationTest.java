package edu.stanford.irt.laneweb.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MappingConfigurationTest {

    private MappingConfiguration configuration;

    @BeforeEach
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
