package edu.stanford.irt.cocoon.sitemap.match;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class URLDecodingWildcardURIMatcherTest {

    private URLDecodingMatcher matcher;

    private Map<String, Object> model;

    @Before
    public void setUp() throws Exception {
        this.matcher = new URLDecodingMatcher("key");
        this.model = new HashMap<String, Object>();
    }

    @Test
    public void testGetMatchStringMapParameters() {
        this.model.put("key", "foo%20bar");
        assertEquals("foo bar", this.matcher.getMatchString(this.model, null));
    }
}
