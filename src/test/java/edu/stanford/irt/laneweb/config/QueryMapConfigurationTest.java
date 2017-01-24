package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertNotNull;

import org.apache.solr.client.solrj.SolrClient;
import org.junit.Before;
import org.junit.Test;

public class QueryMapConfigurationTest {

    private QueryMapConfiguration configuration;

    private SolrClient solrClient;

    @Before
    public void setUp() {
        this.solrClient = createMock(SolrClient.class);
        this.configuration = new QueryMapConfiguration(this.solrClient);
    }

    @Test
    public void testQueryMapper() {
        assertNotNull(this.configuration.queryMapper());
    }

    @Test
    public void testQueryMapperSolrTemplate() {
        assertNotNull(this.configuration.queryMapperSolrTemplate());
    }
}
