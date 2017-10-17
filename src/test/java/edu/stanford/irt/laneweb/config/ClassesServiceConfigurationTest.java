package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

public class ClassesServiceConfigurationTest {

    private ClassesServiceConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new ClassesServiceConfiguration();
    }

    @Test
    public void testClassesServiceURI() throws URISyntaxException {
        assertEquals(new URI("scheme", null, "host", 10, null, null, null),
                this.configuration.classesServiceURI("scheme", "host", 10));
    }
}
