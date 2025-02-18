package edu.stanford.irt.laneweb.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SearchConfigurationTest {

    private SearchConfiguration configuration;

    @BeforeEach
    public void setUp() {
        this.configuration = new SearchConfiguration();
    }

    @Test
    public void testDescriptionLabelTransformer() {
        assertNotNull(this.configuration.descriptionLabelTransformer());
    }

    @Test
    public void testDescriptionLineBreakTransformer() {
        assertNotNull(this.configuration.descriptionLineBreakTransformer());
    }

    @Test
    public void testDescriptionLinkTransformer() {
        assertNotNull(this.configuration.descriptionLinkTransformer());
    }

    @Test
    public void testQueryHighlightingTransformer() {
        assertNotNull(this.configuration.queryHighlightingTransformer());
    }

    @Test
    public void testSolrHighlightingTransformer() {
        assertNotNull(this.configuration.solrHighlightingTransformer());
    }
}
