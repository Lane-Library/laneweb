package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class CodecConfigurationTest {

    private CodecConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new CodecConfiguration();
    }

    @Test
    public void testShcCodec() {
        assertNotNull(this.configuration.shcCodec("key", "vector"));
    }
}
