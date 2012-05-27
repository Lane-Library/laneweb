package edu.stanford.irt.cocoon.sitemap.match;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.sitemap.match.RegexpMatcher;
import edu.stanford.irt.laneweb.model.Model;

public class RegexpSitemapURIMatcherTest {

    Map<String, Object> model;

    private RegexpMatcher matcher;

    @Before
    public void setUp() throws Exception {
        this.matcher = new RegexpMatcher(Model.SITEMAP_URI);
        this.model = new HashMap<String, Object>();
    }

    @Test
    public void testGetMatchStringNoSlash() {
        this.model.put(Model.SITEMAP_URI, "foo");
        assertEquals("foo", this.matcher.getMatchString(this.model, null));
    }

    @Test
    public void testGetMatchStringSlash() {
        this.model.put(Model.SITEMAP_URI, "/foo");
        assertEquals("foo", this.matcher.getMatchString(this.model, null));
    }
}
