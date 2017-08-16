package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class CMEConfigurationTest {

    private CMEConfiguration configuration;

    @Before
    public void setUp() throws Exception {
        this.configuration = new CMEConfiguration();
    }

    @Test
    public void testCmeLinksSelector() {
        assertNotNull(this.configuration.cmeLinksSelector());
    }

    @Test
    public void testHtmlCMELinkTransformer() {
        assertNotNull(this.configuration.htmlCMELinkTransformer());
    }
}
