package edu.stanford.irt.laneweb.cocoon;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;

public class LocationModifierTest {

    private LocationModifier locationModifier;

    @Before
    public void setUp() {
        this.locationModifier = new LocationModifier();
    }

    @Test
    public void testApps() throws URISyntaxException {
        assertEquals(new URI("apps:/search/xml/foo"), this.locationModifier.modify(new URI("cocoon://apps/search/xml/foo")));
    }

    @Test
    public void testContent() throws URISyntaxException {
        assertEquals(new URI("content:/foo/bar"), this.locationModifier.modify(new URI("cocoon:/foo/bar")));
    }

    @Test
    public void testEresources() throws URISyntaxException {
        assertEquals(new URI("eresources:/browse/type/ej"), this.locationModifier.modify(new URI("cocoon://eresources/browse/type/ej")));
    }

    @Test
    public void testModifyLocationDoubleSlash() throws URISyntaxException {
        assertEquals(new URI("content:/biomed-resources/software.html"),
                this.locationModifier.modify(new URI("cocoon://content/biomed-resources/software.html")));
    }

    @Test
    public void testNcbiRss2HTML() throws URISyntaxException {
        assertEquals(new URI("content:/ncbi-rss2html-brief/foo/bar"),
                this.locationModifier.modify(new URI("cocoon://rss/ncbi-rss2html-brief/foo/bar")));
        assertEquals(new URI("content:/ncbi-rss2html/foo/bar"),
                this.locationModifier.modify(new URI("cocoon://rss/ncbi-rss2html/foo/bar")));
    }

    @Test
    public void testRss() throws URISyntaxException {
        assertEquals(new URI("rss:/biomed-resources/browse/type/ej"),
                this.locationModifier.modify(new URI("cocoon://rss/biomed-resources/browse/type/ej")));
    }
}
