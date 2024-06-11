package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;


public class EresourcesConfigurationTest {

    private EresourcesConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new EresourcesConfiguration();
    }

    @Test
    public void testAtoZBrowseSAXStrategy() {
        assertNotNull(this.configuration.aToZBrowseSAXStrategy());
    }

    @Test
    public void testEresourcesAtoZBrowseGenerator() {
        assertNotNull(this.configuration.eresourcesAtoZBrowseGenerator(null));
    }

    @Test
    public void testEresourcesBrowseAllGenerator() {
        assertNotNull(this.configuration.eresourcesBrowseAllGenerator(null));
    }

    @Test
    public void testEresourcesBrowseGenerator() {
        assertNotNull(this.configuration.eresourcesBrowseGenerator(null));
    }

    @Test
    public void testEresourcesCountGenerator() {
        assertNotNull(this.configuration.eresourcesCountGenerator(null));
    }

    @Test
    public void testLinkWithCoverSAXStrategy() {
        assertNotNull(this.configuration.linkWithCoverSAXStrategy());
    }

    @Test
    public void testLinkWithCoverTransformer() {
        assertNotNull(this.configuration.linkWithCoverTransformer(null));
    }

    @Test
    public void testPagingEresourceListHTMLSAXStrategy() {
        assertNotNull(this.configuration.pagingEresourceListHTMLSAXStrategy());
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
