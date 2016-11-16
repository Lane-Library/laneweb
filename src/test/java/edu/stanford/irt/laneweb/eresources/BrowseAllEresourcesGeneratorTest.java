package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.solr.SolrService;

public class BrowseAllEresourcesGeneratorTest {

    private BrowseAllEresourcesGenerator generator;

    private SAXStrategy<PagingEresourceList> saxStrategy;

    private SolrService solrService;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.solrService = createMock(SolrService.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new BrowseAllEresourcesGenerator("er-browse", this.solrService, this.saxStrategy);
    }

    @Test
    public void testCreateKey() {
        this.generator.setParameters(Collections.singletonMap(Model.TYPE, "type"));
        assertEquals("p=;a=;t=type", this.generator.createKey().toString());
    }

    @Test
    public void testGetEresourceList() {
        expect(this.solrService.getType("type")).andReturn(Collections.singletonList(null));
        replay(this.solrService);
        this.generator.setParameters(Collections.singletonMap(Model.TYPE, "type"));
        assertEquals(1, this.generator.getEresourceList(this.solrService).size());
        verify(this.solrService);
    }

    @Test
    public void testGetEresourceListNoType() {
        this.generator.setParameters(Collections.emptyMap());
        assertEquals(0, this.generator.getEresourceList(this.solrService).size());
    }

    @Test
    public void testGetHeading() {
        assertNull(this.generator.getHeading());
    }
}
