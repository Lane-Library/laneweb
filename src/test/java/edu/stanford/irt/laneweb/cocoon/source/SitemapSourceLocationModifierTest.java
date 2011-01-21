package edu.stanford.irt.laneweb.cocoon.source;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class SitemapSourceLocationModifierTest {

    private SitemapSourceLocationModifier locationModifier;

    @Before
    public void setUp() throws Exception {
        this.locationModifier = new SitemapSourceLocationModifier();
    }

    @Test
    public void testApps() {
        assertEquals("apps:/search/xml/foo", this.locationModifier.modify("cocoon://apps/search/xml/foo"));
    }

    @Test
    public void testContent() {
        assertEquals("content:/foo/bar", this.locationModifier.modify("cocoon:/foo/bar"));
    }

    @Test
    public void testEresources() {
        assertEquals("eresources:/browse/type/ej", this.locationModifier.modify("cocoon://eresources/browse/type/ej"));
    }

    @Test
    public void testModifyLocationDoubleSlash() {
        assertEquals("content:/biomed-resources/software.html",
                this.locationModifier.modify("cocoon://content/biomed-resources/software.html"));
    }

    @Test
    public void testNcbiRss2HTML() {
        assertEquals("content:/ncbi-rss2html-brief/foo/bar",
                this.locationModifier.modify("cocoon://rss/ncbi-rss2html-brief/foo/bar"));
        assertEquals("content:/ncbi-rss2html/foo/bar", this.locationModifier.modify("cocoon://rss/ncbi-rss2html/foo/bar"));
    }

    @Test
    public void testRss() {
        assertEquals("rss:/biomed-resources/browse/type/ej",
                this.locationModifier.modify("cocoon://rss/biomed-resources/browse/type/ej"));
    }
}
