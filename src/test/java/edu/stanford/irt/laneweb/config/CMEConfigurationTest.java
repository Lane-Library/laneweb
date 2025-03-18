package edu.stanford.irt.laneweb.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CMEConfigurationTest {

    private CMEConfiguration configuration;

    @BeforeEach
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
