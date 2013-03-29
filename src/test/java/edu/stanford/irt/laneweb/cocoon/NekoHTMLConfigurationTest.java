package edu.stanford.irt.laneweb.cocoon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

public class NekoHTMLConfigurationTest {

    private NekoHTMLConfiguration configuration;

    @Before
    public void setUp() throws Exception {
        // TODO: set up with the features/properties we actually use
        this.configuration = new NekoHTMLConfiguration(Collections.<String, String> emptyMap(),
                Collections.<String, Boolean> emptyMap());
    }

    @Test
    public void test() {
        assertEquals("Windows-1252", this.configuration.getProperty("http://cyberneko.org/html/properties/default-encoding"));
        assertEquals("upper", this.configuration.getProperty("http://cyberneko.org/html/properties/names/elems"));
        assertEquals("http://www.w3.org/1999/xhtml",
                this.configuration.getProperty("http://cyberneko.org/html/properties/namespaces-uri"));
        assertFalse(this.configuration.getFeature("http://cyberneko.org/html/features/insert-namespaces"));
    }
}
