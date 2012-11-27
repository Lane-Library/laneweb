package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Query;

public class MergedSearchGeneratorTest {

    private CollectionManager collectionManager;

    private ContentResultConversionStrategy conversionStrategy;

    private Eresource eresource;

    private MergedSearchGenerator generator;

    private MetaSearchManager metaSearchManager;

    private SearchResult result;

    private SAXStrategy<PagingSearchResultSet> saxStrategy;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.collectionManager = createMock(CollectionManager.class);
        this.conversionStrategy = createMock(ContentResultConversionStrategy.class);
        this.metaSearchManager = createMock(MetaSearchManager.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new MergedSearchGenerator(this.metaSearchManager, this.collectionManager, this.saxStrategy,
                this.conversionStrategy);
        this.result = createMock(SearchResult.class);
        this.eresource = createMock(Eresource.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetSearchResults() {
        expect(this.metaSearchManager.search(isA(Query.class), eq(20000L), (Collection<String>) isNull(), eq(true)))
                .andReturn(null);
        List<SearchResult> list = new LinkedList<SearchResult>();
        list.add(this.result);
        expect(this.conversionStrategy.convertResult(null)).andReturn(list);
        expect(this.collectionManager.search("query")).andReturn(Collections.singleton(this.eresource));
        expect(this.eresource.getTitle()).andReturn("title");
//        expect(this.result.compareTo(this.result)).andReturn(0); //java 7 does this
        expect(this.result.getScore()).andReturn(1);
        expect(this.eresource.getScore()).andReturn(0);
        replay(this.collectionManager, this.conversionStrategy, this.metaSearchManager, this.saxStrategy, this.result,
                this.eresource);
        assertEquals(2, this.generator.doSearch("query").size());
        verify(this.collectionManager, this.conversionStrategy, this.metaSearchManager, this.saxStrategy, this.result,
                this.eresource);
    }

    @Test
    public void testGetSearchResultsEmptyQuery() {
        replay(this.collectionManager, this.conversionStrategy, this.metaSearchManager, this.saxStrategy, this.result,
                this.eresource);
        assertEquals(0, this.generator.doSearch("").size());
        verify(this.collectionManager, this.conversionStrategy, this.metaSearchManager, this.saxStrategy, this.result,
                this.eresource);
    }

    @Test
    public void testGetSearchResultsNullQuery() {
        replay(this.collectionManager, this.conversionStrategy, this.metaSearchManager, this.saxStrategy, this.result,
                this.eresource);
        assertEquals(0, this.generator.doSearch(null).size());
        verify(this.collectionManager, this.conversionStrategy, this.metaSearchManager, this.saxStrategy, this.result,
                this.eresource);
    }
}
