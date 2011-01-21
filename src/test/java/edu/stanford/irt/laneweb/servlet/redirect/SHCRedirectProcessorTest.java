package edu.stanford.irt.laneweb.servlet.redirect;

import static org.junit.Assert.assertEquals;

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
        this.processor.setRedirectMap(Collections.<String, String> singletonMap("/shc/anesthesia.html(?:\\??)(.*)",
                "/portals/anesthesia.html?sourceid=shc&source=/portals/anesthesia.html&$1"));
        assertEquals("/portals/anesthesia.html?sourceid=shc&source=/portals/anesthesia.html&q=query&foo=bar",
                this.processor.getRedirectURL("/shc/anesthesia.html", "", "q=query&foo=bar"));
        assertEquals("/portals/anesthesia.html?sourceid=shc&source=/portals/anesthesia.html&",
                this.processor.getRedirectURL("/shc/anesthesia.html", "", null));
    }

    @Test
    public void testHttpRedirectWithURLEncodedQuery() {
        this.processor.setRedirectMap(Collections.<String, String> singletonMap("/shc/radiology.html(?:\\?q=?)(.*)",
                "http://www.guideline.gov/search/results.aspx?113=666&term=$1"));
        assertEquals("http://www.guideline.gov/search/results.aspx?113=666&term=femoral+fracture",
                this.processor.getRedirectURL("/shc/radiology.html", "", "q=femoral+fracture"));
    }
}
