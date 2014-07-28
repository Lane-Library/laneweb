package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.eresources.CollectionManager;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.MetaSearchManager;

public class MergedSearchGeneratorTest {

    private CollectionManager collectionManager;

    private ContentResultConversionStrategy conversionStrategy;

    private Eresource eresource;

    private MergedSearchGenerator generator;

    private MetaSearchManager MetaSearchManager;

    private SearchResult result;

    private SAXStrategy<PagingSearchResultList> saxStrategy;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.collectionManager = createMock(CollectionManager.class);
        this.conversionStrategy = createMock(ContentResultConversionStrategy.class);
        this.MetaSearchManager = createMock(MetaSearchManager.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new MergedSearchGenerator(this.MetaSearchManager, this.collectionManager, this.saxStrategy,
                this.conversionStrategy);
        this.result = createMock(SearchResult.class);
        this.eresource = createMock(Eresource.class);
    }

    @Test
    public void testGetSearchResults() {
        expect(this.MetaSearchManager.search(isA(Query.class), eq(20000L), eq(true)))
                .andReturn(null);
        List<SearchResult> list = new LinkedList<SearchResult>();
        list.add(this.result);
        expect(this.conversionStrategy.convertResult(null)).andReturn(list);
        expect(this.collectionManager.search("query")).andReturn(Collections.singletonList(this.eresource));
        expect(this.eresource.getTitle()).andReturn("title");
//        expect(this.result.compareTo(this.result)).andReturn(0);
        expect(this.result.getScore()).andReturn(1);
        expect(this.eresource.getScore()).andReturn(0f);
        replay(this.collectionManager, this.conversionStrategy, this.MetaSearchManager, this.saxStrategy, this.result,
                this.eresource);
        assertEquals(2, this.generator.doSearch("query").size());
        verify(this.collectionManager, this.conversionStrategy, this.MetaSearchManager, this.saxStrategy, this.result,
                this.eresource);
    }

    @Test
    public void testGetSearchResultsEmptyQuery() {
        replay(this.collectionManager, this.conversionStrategy, this.MetaSearchManager, this.saxStrategy, this.result,
                this.eresource);
        assertEquals(0, this.generator.doSearch("").size());
        verify(this.collectionManager, this.conversionStrategy, this.MetaSearchManager, this.saxStrategy, this.result,
                this.eresource);
    }

    @Test
    public void testGetSearchResultsNullQuery() {
        replay(this.collectionManager, this.conversionStrategy, this.MetaSearchManager, this.saxStrategy, this.result,
                this.eresource);
        assertEquals(0, this.generator.doSearch(null).size());
        verify(this.collectionManager, this.conversionStrategy, this.MetaSearchManager, this.saxStrategy, this.result,
                this.eresource);
    }
}
