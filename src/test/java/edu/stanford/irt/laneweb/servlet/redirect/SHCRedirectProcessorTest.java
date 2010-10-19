package edu.stanford.irt.laneweb.servlet.redirect;

import static org.junit.Assert.*;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;


public class SHCRedirectProcessorTest {
    
    private SHCRedirectProcessor processor;

    @Before
    public void setUp() throws Exception {
        this.processor = new SHCRedirectProcessor();
    }

    @Test
    public void testGetRedirectURL() {
        this.processor.setRedirectMap(Collections.<String, String>singletonMap("/shc/anesthesia.html(?:\\??)(.*)", "/portals/anesthesia.html?sourceid=shc&source=/portals/anesthesia.html&$1"));
        assertEquals("/portals/anesthesia.html?sourceid=shc&source=/portals/anesthesia.html&q=query&foo=bar", this.processor.getRedirectURL("/shc/anesthesia.html?q=query&foo=bar"));
        assertEquals("/portals/anesthesia.html?sourceid=shc&source=/portals/anesthesia.html&", this.processor.getRedirectURL("/shc/anesthesia.html"));
    }
}
