package edu.stanford.irt.laneweb.eresources.browse;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.laneweb.model.Model;

public class BrowseAllEresourcesGeneratorTest {

    private BrowseAllEresourcesGenerator generator;

    private SAXStrategy<PagingEresourceList> saxStrategy;

    private SolrService solrService;

    @Before
    public void setUp() {
        this.solrService = mock(SolrService.class);
        this.saxStrategy = mock(SAXStrategy.class);
        this.generator = new BrowseAllEresourcesGenerator("er-browse", this.solrService, this.saxStrategy);
    }

    @Test
    public void testCreateKey() {
        this.generator.setParameters(Collections.singletonMap(Model.QUERY, "query"));
        assertEquals("p=;a=;q=query", this.generator.createKey().toString());
    }

    @Test
    public void testGetEresourceList() {
        expect(this.solrService.browseByQuery("query")).andReturn(Collections.singletonList(null));
        replay(this.solrService);
        this.generator.setParameters(Collections.singletonMap(Model.QUERY, "query"));
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
