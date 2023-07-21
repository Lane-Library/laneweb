package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class GCESessionConfigurationTest {

    @Test
    public void testConnectionFactory() {
        assertNotNull(new GCESessionConfiguration().connectionFactory("localhost", 6379));
    }
}
