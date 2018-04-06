package edu.stanford.irt.laneweb.eresources.browse;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.eresources.SolrService;
import edu.stanford.irt.laneweb.model.Model;

public class BrowseEresourcesGeneratorTest {

    private BrowseEresourcesGenerator generator;

    private SAXStrategy<PagingEresourceList> saxStrategy;

    private SolrService solrService;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.solrService = mock(SolrService.class);
        this.saxStrategy = mock(SAXStrategy.class);
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
        this.generator.setModel(Collections.emptyMap());
        assertEquals(0, this.generator.getEresourceList(this.solrService).size());
        assertEquals("p=0;a=a;t=", this.generator.createKey().toString());
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
    public void testGetEresourceListTypeEmptyAlpha() {
        this.generator.setModel(Collections.singletonMap(Model.ALPHA, ""));
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
        this.generator.setParameters(Collections.emptyMap());
        expect(this.solrService.getType("type", 'a')).andReturn(null);
        replay(this.solrService);
        this.generator.getEresourceList(this.solrService);
        assertEquals("p=0;a=a;t=type", this.generator.createKey().toString());
        verify(this.solrService);
    }
}
