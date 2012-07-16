package edu.stanford.irt.laneweb.cocoon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class NekoHTMLConfigurationTest {

    private NekoHTMLConfiguration configuration;

    @Before
    public void setUp() throws Exception {
        this.configuration = new NekoHTMLConfiguration();
    }

    @Test
    public void test() {
        assertEquals("UTF-8", this.configuration.getProperty("http://cyberneko.org/html/properties/default-encoding"));
        assertEquals("lower", this.configuration.getProperty("http://cyberneko.org/html/properties/names/elems"));
        assertEquals("http://www.w3.org/1999/xhtml",
                this.configuration.getProperty("http://cyberneko.org/html/properties/namespaces-uri"));
        assertTrue(this.configuration.getFeature("http://cyberneko.org/html/features/insert-namespaces"));
    }
}
