package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class SearchConfigurationTest {

    private SearchConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new SearchConfiguration();
    }

    @Test
    public void testDescriptionLabelTransformer() {
        assertNotNull(this.configuration.descriptionLabelTransformer());
    }

    @Test
    public void testQueryHighlightingTransformer() {
        assertNotNull(this.configuration.queryHighlightingTransformer());
    }
}