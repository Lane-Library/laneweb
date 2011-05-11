package edu.stanford.irt.laneweb.servlet.redirect;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class NoDotRedirectProcessorTest {

    private NoDotRedirectProcessor processor;

    @Before
    public void setUp() throws Exception {
        this.processor = new NoDotRedirectProcessor();
    }

    @Test
    public void testAppsURL() {
        assertNull(this.processor.getRedirectURL("/apps/ipGroupFetch", "callback=LANEVOY.setIpGroup", null));
    }

    @Test
    public void testEresourcesURL() {
        assertNull(this.processor.getRedirectURL("/eresources/count", "q=heart", null));
    }

    @Test
    public void testGetRedirectURL() {
        assertEquals("/foo/bar/index.html", this.processor.getRedirectURL("/foo/bar", null, null));
    }

    @Test
    public void testGetStageRedirectURL() {
        assertEquals("/stage/foo/bar/index.html", this.processor.getRedirectURL("/foo/bar", "/stage", null));
    }

    @Test
    public void testHasDot() {
        assertNull(this.processor.getRedirectURL("/foo/bar.waz", null, null));
    }

    @Test
    public void testMobileURL() {
        assertNull(this.processor.getRedirectURL("/m/er/browse/type/ej/Journals%20-%20U%20-%20", null, null));
    }

    @Test
    public void testRssURL() {
        assertNull(this.processor.getRedirectURL("/rss/page2rss", "page=/biomed-resources/grandrounds/pedsgrandrounds-2006.html",
                null));
    }

    @Test
    public void testSecureAppsURL() {
        assertNull(this.processor.getRedirectURL("/secure/apps/proxy/credential", "url=foo", null));
    }
}
