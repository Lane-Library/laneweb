package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.solr.SolrService;

public class EresourcesSearchGeneratorTest {

    private SolrService solrService;

    private Eresource eresource;

    private EresourcesSearchGenerator generator;

    private SAXStrategy<PagingSearchResultList> saxStrategy;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.solrService = createMock(SolrService.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new EresourcesSearchGenerator(this.solrService, this.saxStrategy);
        this.eresource = createMock(Eresource.class);
    }

    @Test
    public void testGetSearchResults() {
        expect(this.solrService.search("query")).andReturn(Collections.singletonList(this.eresource));
        expect(this.eresource.getTitle()).andReturn("title");
        expect(this.eresource.getScore()).andReturn(0f);
        replay(this.solrService, this.saxStrategy, this.eresource);
        this.generator.doSearch("query");
        verify(this.solrService, this.saxStrategy, this.eresource);
    }

    @Test
    public void testGetSearchResultsModelParametersType() {
        expect(this.solrService.searchType("parameterType", "query")).andReturn(
                Collections.singletonList(this.eresource));
        expect(this.eresource.getTitle()).andReturn("title");
        expect(this.eresource.getScore()).andReturn(0f);
        replay(this.solrService, this.saxStrategy, this.eresource);
        this.generator.setModel(Collections.singletonMap(Model.TYPE, "modelType"));
        this.generator.setParameters(Collections.singletonMap(Model.TYPE, "parameterType"));
        this.generator.doSearch("query");
        verify(this.solrService, this.saxStrategy, this.eresource);
    }

    @Test
    public void testGetSearchResultsModelType() {
        expect(this.solrService.searchType("type", "query")).andReturn(
                Collections.singletonList(this.eresource));
        expect(this.eresource.getTitle()).andReturn("title");
        expect(this.eresource.getScore()).andReturn(0f);
        replay(this.solrService, this.saxStrategy, this.eresource);
        this.generator.setModel(Collections.singletonMap(Model.TYPE, "type"));
        this.generator.setParameters(Collections.emptyMap());
        this.generator.doSearch("query");
        verify(this.solrService, this.saxStrategy, this.eresource);
    }

    @Test
    public void testGetSearchResultsParametersType() {
        expect(this.solrService.searchType("type", "query")).andReturn(
                Collections.singletonList(this.eresource));
        expect(this.eresource.getScore()).andReturn(0f);
        expect(this.eresource.getTitle()).andReturn("title");
        replay(this.solrService, this.saxStrategy, this.eresource);
        this.generator.setParameters(Collections.singletonMap(Model.TYPE, "type"));
        this.generator.doSearch("query");
        verify(this.solrService, this.saxStrategy, this.eresource);
    }
}
