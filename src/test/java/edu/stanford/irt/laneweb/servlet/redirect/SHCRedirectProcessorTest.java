package edu.stanford.irt.laneweb.servlet.redirect;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Collections;

import org.junit.jupiter.api.Test;

public class SHCRedirectProcessorTest {

    @Test
    public void testGetRedirectURL() {
        SHCRedirectProcessor processor = new SHCRedirectProcessor(
                Collections.singletonMap("/shc/anesthesia.html(?:\\??)(.*)",
                        "/portals/anesthesia.html?sourceid=shc&source=/portals/anesthesia.html&$1"));
        assertEquals("/portals/anesthesia.html?sourceid=shc&source=/portals/anesthesia.html&q=query&foo=bar",
                processor.getRedirectURL("/shc/anesthesia.html", "", "q=query&foo=bar"));
        assertEquals("/portals/anesthesia.html?sourceid=shc&source=/portals/anesthesia.html&",
                processor.getRedirectURL("/shc/anesthesia.html", "", null));
    }

    @Test
    public void testHttpRedirectWithURLEncodedQuery() {
        SHCRedirectProcessor processor = new SHCRedirectProcessor(Collections.singletonMap(
                "/shc/radiology.html(?:\\?q=?)(.*)",
                "http://www.guideline.gov/search/results.aspx?113=666&term=$1"));
        assertEquals("http://www.guideline.gov/search/results.aspx?113=666&term=femoral+fracture",
                processor.getRedirectURL("/shc/radiology.html", "", "q=femoral+fracture"));
    }

    @Test
    public void testNotSHC() {
        SHCRedirectProcessor processor = new SHCRedirectProcessor(Collections.emptyMap());
        assertNull(processor.getRedirectURL("notshc", "basePath", "queryString"));
    }
}
