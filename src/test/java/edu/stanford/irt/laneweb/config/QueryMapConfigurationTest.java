package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.solr.core.SolrTemplate;

public class QueryMapConfigurationTest {

    private QueryMapConfiguration configuration;

    private SolrTemplate solrClient;

    @Before
    public void setUp() {
        this.solrClient = createMock(SolrTemplate.class);
        this.configuration = new QueryMapConfiguration(this.solrClient);
    }

    @Test
    public void testQueryMapper() {
        assertNotNull(this.configuration.queryMapper());
    }
}
