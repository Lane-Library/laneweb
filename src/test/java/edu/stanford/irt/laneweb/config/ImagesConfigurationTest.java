package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class ImagesConfigurationTest {

    private ImagesConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new ImagesConfiguration();
    }

    @Test
    public void testBassettAccordionGenerator() {
        assertNotNull(this.configuration.bassettAccordionGenerator());
    }

    @Test
    public void testBassettGenerator() {
        assertNotNull(this.configuration.bassettGenerator());
    }

    @Test
    public void testCountSAXStrategy() {
        assertNotNull(this.configuration.countSAXStrategy());
    }

    @Test
    public void testPageSAXStrategy() {
        assertNotNull(this.configuration.pageSAXStrategy());
    }

    @Test
    public void testSolrAdminImageSearchGenerator() {
        assertNotNull(this.configuration.solrAdminImageSearchGenerator());
    }

    @Test
    public void testSolrAdminImageSearchSAXStrategy() {
        assertNotNull(this.configuration.solrImageSearchSAXStrategy());
    }

    @Test
    public void testSolrImageSearchGenerator() {
        assertNotNull(this.configuration.solrImageSearchGenerator());
    }

    @Test
    public void testSolrImageSearchSAXStrategy() {
        assertNotNull(this.configuration.solrImageSearchSAXStrategy());
    }

    @Test
    public void testSolrImageSearchTabGenerator() {
        assertNotNull(this.configuration.solrImageSearchTabGenerator(null));
    }

    @Test
    public void testWebsiteIdMapping() {
        assertNotNull(this.configuration.websiteIdMapping());
    }
}
