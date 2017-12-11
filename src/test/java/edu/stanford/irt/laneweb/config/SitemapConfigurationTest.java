package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class SitemapConfigurationTest {

    private SitemapConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new SitemapConfiguration();
    }

    @Test
    public void testCacheableSelector() {
        assertNotNull(this.configuration.cacheableSelector());
    }

    @Test
    public void testCustomEditorConfigurer() {
        assertNotNull(SitemapConfiguration.customEditorConfigurer());
    }

    @Test
    public void testFacetsSelector() {
        assertNotNull(this.configuration.facetsSelector());
    }

    @Test
    public void testIpGroupSelector() {
        assertNotNull(this.configuration.ipGroupSelector());
    }

    @Test
    public void testLoggedInSelector() {
        assertNotNull(this.configuration.loggedInSelector());
    }

    @Test
    public void testParameterRegexpMatcher() {
        assertNotNull(this.configuration.parameterRegexpMatcher());
    }

    @Test
    public void testRegexpMatcher() {
        assertNotNull(this.configuration.regexpMatcher());
    }

    @Test
    public void testResponsiveSelector() {
        assertNotNull(this.configuration.responsiveSelector());
    }

    @Test
    public void testUrlDecodingMatcher() {
        assertNotNull(this.configuration.urlDecodingMatcher());
    }

    @Test
    public void testWildcardMatcher() {
        assertNotNull(this.configuration.wildcardMatcher());
    }
}
