package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertNotNull;

import org.apache.solr.client.solrj.SolrClient;
import org.junit.Before;
import org.junit.Test;

public class SolrIndexerConfigurationTest {

    private SolrIndexerConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new SolrIndexerConfiguration();
    }

    @Test
    public void testSolrClient() {
        assertNotNull(this.configuration.solrClient("file:/"));
    }

    @Test
    public void testSolrTemplate() {
        assertNotNull(this.configuration.solrTemplate(createMock(SolrClient.class)));
    }
}
