package edu.stanford.irt.laneweb.eresources.browse;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.eresources.EresourceBrowseService;
import edu.stanford.irt.laneweb.model.Model;

public class BrowseEresourcesGeneratorTest {

    private BrowseEresourcesGenerator generator;

    private SAXStrategy<PagingEresourceList> saxStrategy;

    private EresourceBrowseService solrService;

    @BeforeEach
    public void setUp() throws Exception {
        this.solrService = mock(EresourceBrowseService.class);
        this.saxStrategy = mock(SAXStrategy.class);
        this.generator = new BrowseEresourcesGenerator("er-browse", this.solrService, this.saxStrategy);
    }

    @Test
    public void testEncodedType() {
        this.generator.setModel(Collections.emptyMap());
        this.generator.setParameters(Collections.singletonMap(Model.QUERY, "query%20space"));
        expect(this.solrService.browseByQuery("query space", 'a')).andReturn(null);
        replay(this.solrService);
        this.generator.getEresourceList(this.solrService);
        assertEquals("p=0;a=a;q=query space", this.generator.createKey().toString());
        verify(this.solrService);
    }

    @Test
    public void testGetEresourceListNull() {
        this.generator.setModel(Collections.emptyMap());
        assertEquals(0, this.generator.getEresourceList(this.solrService).size());
        assertEquals("p=0;a=a;q=", this.generator.createKey().toString());
    }

    @Test
    public void testGetEresourceListType() {
        this.generator.setModel(Collections.emptyMap());
        this.generator.setParameters(Collections.singletonMap(Model.QUERY, "query"));
        expect(this.solrService.browseByQuery("query", 'a')).andReturn(null);
        replay(this.solrService);
        this.generator.getEresourceList(this.solrService);
        verify(this.solrService);
    }

    @Test
    public void testGetEresourceListTypeAlpha() {
        this.generator.setModel(Collections.singletonMap(Model.ALPHA, "a"));
        this.generator.setParameters(Collections.singletonMap(Model.QUERY, "query"));
        expect(this.solrService.browseByQuery("query", 'a')).andReturn(null);
        replay(this.solrService);
        this.generator.getEresourceList(this.solrService);
        assertEquals("p=0;a=a;q=query", this.generator.createKey().toString());
        verify(this.solrService);
    }

    @Test
    public void testGetEresourceListTypeAlphaString() {
        this.generator.setModel(Collections.singletonMap(Model.ALPHA, "abc"));
        this.generator.setParameters(Collections.singletonMap(Model.QUERY, "query"));
        expect(this.solrService.browseByQuery("query", 'a')).andReturn(null);
        replay(this.solrService);
        this.generator.getEresourceList(this.solrService);
        assertEquals("p=0;a=a;q=query", this.generator.createKey().toString());
        verify(this.solrService);
    }

    @Test
    public void testGetEresourceListTypeEmptyAlpha() {
        this.generator.setModel(Collections.singletonMap(Model.ALPHA, ""));
        this.generator.setParameters(Collections.singletonMap(Model.QUERY, "query"));
        expect(this.solrService.browseByQuery("query", 'a')).andReturn(null);
        replay(this.solrService);
        this.generator.getEresourceList(this.solrService);
        assertEquals("p=0;a=a;q=query", this.generator.createKey().toString());
        verify(this.solrService);
    }

    @Test
    public void testGetEresourceListTypeNoAlpha() {
        this.generator.setModel(Collections.singletonMap(Model.QUERY, "query"));
        this.generator.setParameters(Collections.emptyMap());
        expect(this.solrService.browseByQuery("query", 'a')).andReturn(null);
        replay(this.solrService);
        this.generator.getEresourceList(this.solrService);
        assertEquals("p=0;a=a;q=query", this.generator.createKey().toString());
        verify(this.solrService);
    }
}
