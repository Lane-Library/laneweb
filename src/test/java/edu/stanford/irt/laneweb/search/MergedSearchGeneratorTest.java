package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.solr.SolrService;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.MetaSearchManager;

public class MergedSearchGeneratorTest {

    private SolrService solrService;

    private ContentResultConversionStrategy conversionStrategy;

    private Eresource eresource;

    private MergedSearchGenerator generator;

    private MetaSearchManager MetaSearchManager;

    private SearchResult result;

    private SAXStrategy<PagingSearchResultList> saxStrategy;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.solrService = createMock(SolrService.class);
        this.conversionStrategy = createMock(ContentResultConversionStrategy.class);
        this.MetaSearchManager = createMock(MetaSearchManager.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new MergedSearchGenerator(this.MetaSearchManager, this.solrService, this.saxStrategy,
                this.conversionStrategy);
        this.result = createMock(SearchResult.class);
        this.eresource = createMock(Eresource.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetSearchResults() {
        expect(this.MetaSearchManager.search(isA(Query.class), isNull(Collection.class), eq(20000L))).andReturn(null);
        List<SearchResult> list = new ArrayList<SearchResult>();
        list.add(this.result);
        expect(this.conversionStrategy.convertResult(null)).andReturn(list);
        expect(this.solrService.search("query")).andReturn(Collections.singletonList(this.eresource));
        expect(this.eresource.getTitle()).andReturn("title");
        // expect(this.result.compareTo(this.result)).andReturn(0);
        expect(this.result.getScore()).andReturn(1);
        expect(this.eresource.getScore()).andReturn(0f);
        replay(this.solrService, this.conversionStrategy, this.MetaSearchManager, this.saxStrategy, this.result,
                this.eresource);
        assertEquals(2, this.generator.doSearch("query").size());
        verify(this.solrService, this.conversionStrategy, this.MetaSearchManager, this.saxStrategy, this.result,
                this.eresource);
    }

    @Test
    public void testGetSearchResultsEmptyQuery() {
        replay(this.solrService, this.conversionStrategy, this.MetaSearchManager, this.saxStrategy, this.result,
                this.eresource);
        assertEquals(0, this.generator.doSearch("").size());
        verify(this.solrService, this.conversionStrategy, this.MetaSearchManager, this.saxStrategy, this.result,
                this.eresource);
    }

    @Test
    public void testGetSearchResultsNullQuery() {
        replay(this.solrService, this.conversionStrategy, this.MetaSearchManager, this.saxStrategy, this.result,
                this.eresource);
        assertEquals(0, this.generator.doSearch(null).size());
        verify(this.solrService, this.conversionStrategy, this.MetaSearchManager, this.saxStrategy, this.result,
                this.eresource);
    }
}
