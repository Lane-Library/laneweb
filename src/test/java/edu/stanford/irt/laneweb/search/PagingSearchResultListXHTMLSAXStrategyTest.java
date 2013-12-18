package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ListIterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.resource.PagingData;
import edu.stanford.irt.search.legacy.Result;

public class PagingSearchResultListXHTMLSAXStrategyTest {

    private ListIterator<SearchResult> iterator;

    private PagingSearchResultList list;

    private PagingData pagingData;

    private SearchResult result;

    private Set<SearchResult> results;

    private SAXStrategy<SearchResult> resultStrategy;

    private PagingSearchResultListXHTMLSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    private Result resourceResult;
    
    private ContentResultSearchResult contentResult;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.results = createMock(Set.class);
        this.xmlConsumer = new TestXMLConsumer();
        this.result = createMock(SearchResult.class);
        this.iterator = createMock(ListIterator.class);
        this.resultStrategy = createMock(SAXStrategy.class);
        this.strategy = new PagingSearchResultListXHTMLSAXStrategy(this.resultStrategy);
        this.list = createMock(PagingSearchResultList.class);
        this.pagingData = createMock(PagingData.class);
        this.resourceResult = createMock(Result.class);
        this.contentResult = createMock(ContentResultSearchResult.class);
    }

    @Test
    public void testAllPages539ToSAX() throws SAXException, IOException {
        expect(this.list.getPagingData()).andReturn(this.pagingData);
        expect(this.pagingData.getStart()).andReturn(0);
        expect(this.pagingData.getLength()).andReturn(539);
        expect(this.list.listIterator(0)).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(true).times(539);
        expect(this.iterator.next()).andReturn(this.result).times(539);
        this.resultStrategy.toSAX(this.result, this.xmlConsumer);
        expectLastCall().times(539);
        expect(this.iterator.hasNext()).andReturn(false);
        //TODO: pagingData
        expect(this.list.size()).andReturn(0);
        expect(this.list.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(false);
        replay(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
        this.strategy.toSAX(this.list, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this,
                "PagingSearchResultListXHTMLSAXStrategyTest-testAllPages539ToSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
    }

    @Test
    public void testAllPagesToSAX() throws SAXException, IOException {
        expect(this.list.getPagingData()).andReturn(this.pagingData);
        expect(this.pagingData.getStart()).andReturn(0);
        expect(this.pagingData.getLength()).andReturn(256);
        expect(this.list.listIterator(0)).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(true).times(256);
        expect(this.iterator.next()).andReturn(this.result).times(256);
        this.resultStrategy.toSAX(this.result, this.xmlConsumer);
        expectLastCall().times(256);
        expect(this.iterator.hasNext()).andReturn(false);
        //TODO: pagingData
        expect(this.list.size()).andReturn(0);
        expect(this.list.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(false);
        replay(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
        this.strategy.toSAX(this.list, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this,
                "PagingSearchResultListXHTMLSAXStrategyTest-testAllPagesToSAX.xml"), this.xmlConsumer.getStringValue());
        verify(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
    }

    @Test
    public void testPage0ToSAX() throws SAXException, IOException {
        expect(this.list.getPagingData()).andReturn(this.pagingData);
        expect(this.pagingData.getStart()).andReturn(0);
        expect(this.pagingData.getLength()).andReturn(100);
        expect(this.list.listIterator(0)).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(true).times(100);
        expect(this.iterator.next()).andReturn(this.result).times(100);
        this.resultStrategy.toSAX(this.result, this.xmlConsumer);
        expectLastCall().times(100);
        expect(this.iterator.hasNext()).andReturn(false);
        //TODO: pagingData
        expect(this.list.size()).andReturn(0);
        expect(this.list.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(false);
        replay(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
        this.strategy.toSAX(this.list, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this,
                "PagingSearchResultListXHTMLSAXStrategyTest-testPage0ToSAX.xml"), this.xmlConsumer.getStringValue());
        verify(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
    }

    @Test
    public void testPage1ToSAX() throws SAXException, IOException {
        expect(this.list.getPagingData()).andReturn(this.pagingData);
        expect(this.pagingData.getStart()).andReturn(100);
        expect(this.pagingData.getLength()).andReturn(100);
        expect(this.list.listIterator(100)).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(true).times(100);
        expect(this.iterator.next()).andReturn(this.result).times(100);
        this.resultStrategy.toSAX(this.result, this.xmlConsumer);
        expectLastCall().times(100);
        expect(this.iterator.hasNext()).andReturn(false);
        //TODO: pagingData
        expect(this.list.size()).andReturn(0);
        expect(this.list.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(false);
        replay(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
        this.strategy.toSAX(this.list, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this,
                "PagingSearchResultListXHTMLSAXStrategyTest-testPage1ToSAX.xml"), this.xmlConsumer.getStringValue());
        verify(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
    }

    @Test
    public void testPage2ToSAX() throws SAXException, IOException {
        expect(this.list.getPagingData()).andReturn(this.pagingData);
        expect(this.pagingData.getStart()).andReturn(200);
        expect(this.pagingData.getLength()).andReturn(100);
        expect(this.list.listIterator(200)).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(true).times(56);
        expect(this.iterator.next()).andReturn(this.result).times(56);
        this.resultStrategy.toSAX(this.result, this.xmlConsumer);
        expectLastCall().times(56);
        expect(this.iterator.hasNext()).andReturn(false);
        //TODO: pagingData
        expect(this.list.size()).andReturn(0);
        expect(this.list.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(false);
        replay(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
        this.strategy.toSAX(this.list, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this,
                "PagingSearchResultListXHTMLSAXStrategyTest-testPage2ToSAX.xml"), this.xmlConsumer.getStringValue());
        verify(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
    }
    
    @Test
    public void testSearchContentCounts() throws SAXException, IOException {
        expect(this.list.getPagingData()).andReturn(this.pagingData);
        expect(this.pagingData.getStart()).andReturn(0);
        expect(this.pagingData.getLength()).andReturn(1);
        expect(this.list.listIterator(0)).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(true);
        expect(this.iterator.next()).andReturn(this.result);
        this.resultStrategy.toSAX(this.result, this.xmlConsumer);
        expect(this.iterator.hasNext()).andReturn(false);
        //TODO: pagingData
        expect(this.list.size()).andReturn(0);
        expect(this.list.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(true);
        expect(this.iterator.next()).andReturn(this.contentResult);
        expect(this.contentResult.getResourceResult()).andReturn(this.resourceResult);
        expect(this.resourceResult.getId()).andReturn("pubmed");
        expect(this.resourceResult.getURL()).andReturn("url");
        expect(this.resourceResult.getHits()).andReturn("hits");
        expect(this.iterator.hasNext()).andReturn(false);
        replay(this.contentResult, this.resourceResult, this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
        this.strategy.toSAX(this.list, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this,
                "PagingSearchResultListXHTMLSAXStrategyTest-testSearchContentCounts.xml"), this.xmlConsumer.getStringValue());
        verify(this.contentResult, this.resourceResult, this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
    }
}
