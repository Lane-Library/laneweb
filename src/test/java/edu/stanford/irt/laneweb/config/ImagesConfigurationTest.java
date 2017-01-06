package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.oxm.Marshaller;

public class ImagesConfigurationTest {

    private ImagesConfiguration configuration;

    private Marshaller marshaller;

    @Before
    public void setUp() {
        this.marshaller = createMock(Marshaller.class);
        this.configuration = new ImagesConfiguration("url", this.marshaller);
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
        assertNotNull(this.configuration.solrImageSearchTabGenerator());
    }

    @Test
    public void testWebsiteIdMapping() {
        assertNotNull(this.configuration.websiteIdMapping());
    }
}
