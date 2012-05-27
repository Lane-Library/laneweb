package edu.stanford.irt.cocoon.sitemap.match;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.sitemap.match.URLDecodingMatcher;
import edu.stanford.irt.laneweb.model.Model;

public class URLDecodingWildcardURIMatcherTest {

    private URLDecodingMatcher matcher;

    private Map<String, Object> model;

    @Before
    public void setUp() throws Exception {
        this.matcher = new URLDecodingMatcher(Model.SITEMAP_URI);
        this.model = new HashMap<String, Object>();
    }

    @Test
    public void testGetMatchStringMapParameters() {
        this.model.put(Model.SITEMAP_URI, "foo%20bar");
        assertEquals("foo bar", this.matcher.getMatchString(this.model, null));
    }
}
