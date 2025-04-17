package edu.stanford.irt.laneweb.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EresourcesConfigurationTest {

    private EresourcesConfiguration configuration;

    @BeforeEach
    public void setUp() {
        this.configuration = new EresourcesConfiguration();
    }

    @Test
    public void testEresourcesCountGenerator() {
        assertNotNull(this.configuration.eresourcesCountGenerator(null));
    }

    @Test
    public void testLinkWithoutCoverTransformer() {
        assertNotNull(this.configuration.linkWithoutCoverTransformer(null));
    }

    @Test
    public void testSolrPagingEresourceSAXStrategy() {
        assertNotNull(this.configuration.solrPagingEresourceSAXStrategy());
    }

    @Test
    public void testSolrSearchFacetsGenerator() {
        assertNotNull(this.configuration.facetsGenerator(null));
    }

    @Test
    public void testSolrSearchGenerator() {
        assertNotNull(this.configuration.solrSearchGenerator(null));
    }

}
