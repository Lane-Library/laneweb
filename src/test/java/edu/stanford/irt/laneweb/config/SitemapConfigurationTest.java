package edu.stanford.irt.laneweb.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SitemapConfigurationTest {

    private SitemapConfiguration configuration;

    @BeforeEach
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
    public void testUrlDecodingMatcher() {
        assertNotNull(this.configuration.urlDecodingMatcher());
    }

    @Test
    public void testWildcardMatcher() {
        assertNotNull(this.configuration.wildcardMatcher());
    }
}
