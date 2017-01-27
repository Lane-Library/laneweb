package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.springframework.oxm.Marshaller;

public class EresourcesConfigurationTest {

    private EresourcesConfiguration configuration;

    private Marshaller marshaller;

    @Before
    public void setUp() {
        this.marshaller = createMock(Marshaller.class);
        this.configuration = new EresourcesConfiguration("url", this.marshaller);
    }

    @Test
    public void testEresourcesBrowseAllGenerator() {
        assertNotNull(this.configuration.eresourcesBrowseAllGenerator());
    }

    @Test
    public void testEresourcesBrowseGenerator() {
        assertNotNull(this.configuration.eresourcesBrowseGenerator());
    }

    @Test
    public void testEresourcesCoreGenerator() {
        assertNotNull(this.configuration.eresourcesCoreGenerator());
    }

    @Test
    public void testEresourcesCountGenerator() {
        assertNotNull(this.configuration.eresourcesCountGenerator());
    }

    @Test
    public void testEresourcesMeshGenerator() {
        assertNotNull(this.configuration.eresourcesMeshGenerator());
    }

    @Test
    public void testPagingEresourceListHTMLSAXStrategy() {
        assertNotNull(this.configuration.pagingEresourceListHTMLSAXStrategy());
    }

    @Test
    public void testSolrQueryParser() {
        assertNotNull(this.configuration.solrQueryParser());
    }

    @Test
    public void testSolrService() {
        assertNotNull(this.configuration.solrService());
    }

    @Test
    public void testSolrTemplate() {
        assertNotNull(this.configuration.solrTemplate());
    }
}