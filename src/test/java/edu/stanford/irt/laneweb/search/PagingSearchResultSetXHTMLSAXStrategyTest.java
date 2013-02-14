package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.TestXMLConsumer;

public class PagingSearchResultSetXHTMLSAXStrategyTest {

    private Iterator<SearchResult> iterator;

    private SearchResult result;

    private Set<SearchResult> results;

    private SAXStrategy<SearchResult> resultStrategy;

    private PagingSearchResultSet set;

    private PagingSearchResultSetXHTMLSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.results = createMock(Set.class);
        this.xmlConsumer = new TestXMLConsumer();
        this.result = createMock(SearchResult.class);
        this.iterator = createMock(Iterator.class);
        this.resultStrategy = createMock(SAXStrategy.class);
        this.strategy = new PagingSearchResultSetXHTMLSAXStrategy(this.resultStrategy);
        this.set = createMock(PagingSearchResultSet.class);
    }

    @Test
    public void testAllPages539ToSAX() throws SAXException, IOException {
        expect(this.set.getPage()).andReturn(-1);
        expect(this.set.size()).andReturn(539);
        expect(this.set.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(true).times(539);
        expect(this.iterator.next()).andReturn(this.result).times(539);
        this.resultStrategy.toSAX(this.result, this.xmlConsumer);
        expectLastCall().times(539);
        expect(this.iterator.hasNext()).andReturn(false);
        replay(this.results, this.result, this.iterator, this.set);
        this.strategy.toSAX(this.set, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this,
                "PagingSearchResultSetXHTMLSAXStrategyTest-testAllPages539ToSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.results, this.result, this.iterator, this.set);
    }

    @Test
    public void testAllPagesToSAX() throws SAXException, IOException {
        expect(this.set.getPage()).andReturn(-1);
        expect(this.set.size()).andReturn(256);
        expect(this.set.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(true).times(256);
        expect(this.iterator.next()).andReturn(this.result).times(256);
        this.resultStrategy.toSAX(this.result, this.xmlConsumer);
        expectLastCall().times(256);
        expect(this.iterator.hasNext()).andReturn(false);
        replay(this.results, this.result, this.iterator, this.set, this.resultStrategy);
        this.strategy.toSAX(this.set, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this,
                "PagingSearchResultSetXHTMLSAXStrategyTest-testAllPagesToSAX.xml"), this.xmlConsumer.getStringValue());
        verify(this.results, this.result, this.iterator, this.set, this.resultStrategy);
    }

    @Test
    public void testPage0ToSAX() throws SAXException, IOException {
        expect(this.set.getPage()).andReturn(0);
        expect(this.set.size()).andReturn(256);
        expect(this.set.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(true).times(100);
        expect(this.iterator.next()).andReturn(this.result).times(100);
        this.resultStrategy.toSAX(this.result, this.xmlConsumer);
        expectLastCall().times(100);
        expect(this.iterator.hasNext()).andReturn(false);
        replay(this.results, this.result, this.iterator, this.set, this.resultStrategy);
        this.strategy.toSAX(this.set, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this,
                "PagingSearchResultSetXHTMLSAXStrategyTest-testPage0ToSAX.xml"), this.xmlConsumer.getStringValue());
        verify(this.results, this.result, this.iterator, this.set, this.resultStrategy);
    }

    @Test
    public void testPage1ToSAX() throws SAXException, IOException {
        expect(this.set.getPage()).andReturn(1);
        expect(this.set.size()).andReturn(256);
        expect(this.set.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(true).times(200);
        expect(this.iterator.next()).andReturn(this.result).times(200);
        this.resultStrategy.toSAX(this.result, this.xmlConsumer);
        expectLastCall().times(100);
        expect(this.iterator.hasNext()).andReturn(false);
        replay(this.results, this.result, this.iterator, this.set, this.resultStrategy);
        this.strategy.toSAX(this.set, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this,
                "PagingSearchResultSetXHTMLSAXStrategyTest-testPage1ToSAX.xml"), this.xmlConsumer.getStringValue());
        verify(this.results, this.result, this.iterator, this.set, this.resultStrategy);
    }

    @Test
    public void testPage2ToSAX() throws SAXException, IOException {
        expect(this.set.getPage()).andReturn(2);
        expect(this.set.size()).andReturn(256);
        expect(this.set.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(true).times(256);
        expect(this.iterator.next()).andReturn(this.result).times(256);
        this.resultStrategy.toSAX(this.result, this.xmlConsumer);
        expectLastCall().times(56);
        expect(this.iterator.hasNext()).andReturn(false);
        replay(this.results, this.result, this.iterator, this.set, this.resultStrategy);
        this.strategy.toSAX(this.set, this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this,
                "PagingSearchResultSetXHTMLSAXStrategyTest-testPage2ToSAX.xml"), this.xmlConsumer.getStringValue());
        verify(this.results, this.result, this.iterator, this.set, this.resultStrategy);
    }
}
