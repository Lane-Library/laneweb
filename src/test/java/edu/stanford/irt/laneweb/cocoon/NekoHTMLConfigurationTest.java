package edu.stanford.irt.laneweb.cocoon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NekoHTMLConfigurationTest {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    private NekoHTMLConfiguration configuration;

    @BeforeEach
    public void setUp() throws Exception {
        Map<String, String> properties = new HashMap<>();
        properties.put("http://cyberneko.org/html/properties/default-encoding", UTF_8);
        properties.put("http://cyberneko.org/html/properties/names/elems", "lower");
        properties.put("http://cyberneko.org/html/properties/namespaces-uri", "http://www.w3.org/1999/xhtml");
        Map<String, Boolean> features = new HashMap<>();
        features.put("http://cyberneko.org/html/features/insert-namespaces", Boolean.TRUE);
        this.configuration = new NekoHTMLConfiguration(properties, features);
    }

    @Test
    public void testConfiguration() {
        assertEquals(UTF_8, this.configuration.getProperty("http://cyberneko.org/html/properties/default-encoding"));
        assertEquals("lower", this.configuration.getProperty("http://cyberneko.org/html/properties/names/elems"));
        assertEquals("http://www.w3.org/1999/xhtml",
                this.configuration.getProperty("http://cyberneko.org/html/properties/namespaces-uri"));
        assertTrue(this.configuration.getFeature("http://cyberneko.org/html/features/insert-namespaces"));
    }
}
