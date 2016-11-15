package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.solr.SolrService;

public class BrowseEresourcesGeneratorTest {

    private SolrService solrService;

    private BrowseEresourcesGenerator generator;

    private SAXStrategy<PagingEresourceList> saxStrategy;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.solrService = createMock(SolrService.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new BrowseEresourcesGenerator("er-browse", this.solrService, this.saxStrategy);
    }

    @Test
    public void testEncodedType() {
        this.generator.setModel(Collections.emptyMap());
        this.generator.setParameters(Collections.singletonMap(Model.TYPE, "type%20space"));
        expect(this.solrService.getType("type space", 'a')).andReturn(null);
        replay(this.solrService);
        this.generator.getEresourceList(this.solrService);
        assertEquals("p=0;a=a;t=type space", this.generator.createKey().toString());
        verify(this.solrService);
    }

    @Test
    public void testGetEresourceListNull() {
        assertEquals(0, this.generator.getEresourceList(this.solrService).size());
    }

    @Test
    public void testGetEresourceListType() {
        this.generator.setModel(Collections.emptyMap());
        this.generator.setParameters(Collections.singletonMap(Model.TYPE, "type"));
        expect(this.solrService.getType("type", 'a')).andReturn(null);
        replay(this.solrService);
        this.generator.getEresourceList(this.solrService);
        verify(this.solrService);
    }

    @Test
    public void testGetEresourceListTypeAll() {
        this.generator.setModel(Collections.singletonMap(Model.ALPHA, "all"));
        this.generator.setParameters(Collections.singletonMap(Model.TYPE, "type"));
        expect(this.solrService.getType("type", 'a')).andReturn(null);
        replay(this.solrService);
        this.generator.getEresourceList(this.solrService);
        assertEquals("p=0;a=a;t=type", this.generator.createKey().toString());
        verify(this.solrService);
    }

    @Test
    public void testGetEresourceListTypeAlpha() {
        this.generator.setModel(Collections.singletonMap(Model.ALPHA, "a"));
        this.generator.setParameters(Collections.singletonMap(Model.TYPE, "type"));
        expect(this.solrService.getType("type", 'a')).andReturn(null);
        replay(this.solrService);
        this.generator.getEresourceList(this.solrService);
        assertEquals("p=0;a=a;t=type", this.generator.createKey().toString());
        verify(this.solrService);
    }

    @Test
    public void testGetEresourceListTypeAlphaString() {
        this.generator.setModel(Collections.singletonMap(Model.ALPHA, "abc"));
        this.generator.setParameters(Collections.singletonMap(Model.TYPE, "type"));
        expect(this.solrService.getType("type", 'a')).andReturn(null);
        replay(this.solrService);
        this.generator.getEresourceList(this.solrService);
        assertEquals("p=0;a=a;t=type", this.generator.createKey().toString());
        verify(this.solrService);
    }

    @Test
    public void testGetEresourceListTypeNoAlpha() {
        this.generator.setModel(Collections.singletonMap(Model.TYPE, "type"));
        expect(this.solrService.getType("type", 'a')).andReturn(null);
        replay(this.solrService);
        this.generator.getEresourceList(this.solrService);
        assertEquals("p=0;a=a;t=type", this.generator.createKey().toString());
        verify(this.solrService);
    }
}
