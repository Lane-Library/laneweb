package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.resource.PagingData;
import edu.stanford.irt.search.impl.Result;

public class PagingSearchResultListSAXStrategyTest {

        private ListIterator<SearchResult> iterator;

        private PagingSearchResultList list;

        private PagingData pagingData;

        private Result resourceResult;

        private SearchResult result;

        private List<SearchResult> results;

        private SAXStrategy<SearchResult> resultStrategy;

        private PagingSearchResultListSAXStrategy strategy;

        private TestXMLConsumer xmlConsumer;

        @BeforeEach
        public void setUp() throws Exception {
                this.results = mock(List.class);
                this.xmlConsumer = new TestXMLConsumer();
                this.result = mock(SearchResult.class);
                this.iterator = mock(ListIterator.class);
                this.resultStrategy = mock(SAXStrategy.class);
                this.strategy = new PagingSearchResultListSAXStrategy(this.resultStrategy);
                this.list = mock(PagingSearchResultList.class);
                this.pagingData = mock(PagingData.class);
                this.resourceResult = mock(Result.class);
        }

        @Test
        public void testAllPages539ToSAX() throws SAXException, IOException {
                expect(this.list.getPagingData()).andReturn(this.pagingData);
                expect(this.pagingData.getStart()).andReturn(0);
                expect(this.pagingData.getLength()).andReturn(539);
                expect(this.list.size()).andReturn(539);
                expect(this.pagingData.getPage()).andReturn(-1);
                expect(this.pagingData.getPages()).andReturn(4);
                expect(this.list.getQuery()).andReturn("query");
                expect(this.list.iterator()).andReturn(this.iterator);
                expect(this.iterator.hasNext()).andReturn(false);
                expect(this.list.listIterator(0)).andReturn(this.iterator);
                expect(this.iterator.hasNext()).andReturn(true).times(539);
                expect(this.iterator.next()).andReturn(this.result).times(539);
                this.resultStrategy.toSAX(this.result, this.xmlConsumer);
                expectLastCall().times(539);
                expect(this.iterator.hasNext()).andReturn(false);
                replay(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
                this.strategy.toSAX(this.list, this.xmlConsumer);
                assertEquals(
                                this.xmlConsumer.getExpectedResult(this,
                                                "PagingSearchResultListSAXStrategyTest-testAllPages539ToSAX.xml"),
                                this.xmlConsumer.getStringValue());
                verify(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
        }

        @Test
        public void testAllPagesToSAX() throws SAXException, IOException {
                expect(this.list.getPagingData()).andReturn(this.pagingData);
                expect(this.pagingData.getStart()).andReturn(0);
                expect(this.pagingData.getLength()).andReturn(256);
                expect(this.list.size()).andReturn(256);
                expect(this.pagingData.getPage()).andReturn(-1);
                expect(this.pagingData.getPages()).andReturn(3);
                expect(this.list.getQuery()).andReturn("query");
                expect(this.list.iterator()).andReturn(this.iterator);
                expect(this.iterator.hasNext()).andReturn(false);
                expect(this.list.listIterator(0)).andReturn(this.iterator);
                expect(this.iterator.hasNext()).andReturn(true).times(256);
                expect(this.iterator.next()).andReturn(this.result).times(256);
                this.resultStrategy.toSAX(this.result, this.xmlConsumer);
                expectLastCall().times(256);
                expect(this.iterator.hasNext()).andReturn(false);
                replay(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
                this.strategy.toSAX(this.list, this.xmlConsumer);
                assertEquals(
                                this.xmlConsumer.getExpectedResult(this,
                                                "PagingSearchResultListSAXStrategyTest-testAllPagesToSAX.xml"),
                                this.xmlConsumer.getStringValue());
                verify(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
        }

        @Test
        public void testContentHitCounts() throws IOException {
                expect(this.list.getPagingData()).andReturn(this.pagingData);
                expect(this.pagingData.getStart()).andReturn(0);
                expect(this.pagingData.getLength()).andReturn(100);
                expect(this.list.size()).andReturn(256);
                expect(this.pagingData.getPage()).andReturn(0);
                expect(this.pagingData.getPages()).andReturn(3);
                expect(this.list.getQuery()).andReturn("query");
                expect(this.list.iterator()).andReturn(this.iterator);
                expect(this.iterator.hasNext()).andReturn(true);
                expect(this.iterator.next()).andReturn(this.result);
                expect(this.result.getResourceResult()).andReturn(this.resourceResult);
                expect(this.resourceResult.getId()).andReturn("id");
                expect(this.resourceResult.getHits()).andReturn("hits");
                expect(this.resourceResult.getURL()).andReturn("url");
                expect(this.resourceResult.getChildren()).andReturn(Collections.emptySet());
                expect(this.iterator.hasNext()).andReturn(false);
                expect(this.list.listIterator(0)).andReturn(this.iterator);
                expect(this.iterator.hasNext()).andReturn(true).times(100);
                expect(this.iterator.next()).andReturn(this.result).times(100);
                this.resultStrategy.toSAX(this.result, this.xmlConsumer);
                expectLastCall().times(100);
                expect(this.iterator.hasNext()).andReturn(false);
                replay(this.resourceResult, this.results, this.result, this.iterator, this.list, this.pagingData,
                                this.resultStrategy);
                this.strategy.toSAX(this.list, this.xmlConsumer);
                assertEquals(
                                this.xmlConsumer.getExpectedResult(this,
                                                "PagingSearchResultListSAXStrategyTest-testContentHitCounts.xml"),
                                this.xmlConsumer.getStringValue());
                verify(this.resourceResult, this.results, this.result, this.iterator, this.list, this.pagingData,
                                this.resultStrategy);
        }

        @Test
        public void testNullQuery() throws SAXException, IOException {
                expect(this.list.getPagingData()).andReturn(this.pagingData);
                expect(this.pagingData.getStart()).andReturn(0);
                expect(this.pagingData.getLength()).andReturn(0);
                expect(this.list.size()).andReturn(0);
                expect(this.pagingData.getPage()).andReturn(0);
                expect(this.pagingData.getPages()).andReturn(1);
                expect(this.list.getQuery()).andReturn(null);
                expect(this.list.iterator()).andReturn(this.iterator);
                expect(this.iterator.hasNext()).andReturn(false);
                expect(this.list.listIterator(0)).andReturn(this.iterator);
                expect(this.iterator.hasNext()).andReturn(false);
                replay(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
                this.strategy.toSAX(this.list, this.xmlConsumer);
                assertEquals(
                                this.xmlConsumer.getExpectedResult(this,
                                                "PagingSearchResultListSAXStrategyTest-testNullQuery.xml"),
                                this.xmlConsumer.getStringValue());
                verify(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
        }

        @Test
        public void testPage0ToSAX() throws SAXException, IOException {
                expect(this.list.getPagingData()).andReturn(this.pagingData);
                expect(this.pagingData.getStart()).andReturn(0);
                expect(this.pagingData.getLength()).andReturn(100);
                expect(this.list.size()).andReturn(256);
                expect(this.pagingData.getPage()).andReturn(0);
                expect(this.pagingData.getPages()).andReturn(3);
                expect(this.list.getQuery()).andReturn("query");
                expect(this.list.iterator()).andReturn(this.iterator);
                expect(this.iterator.hasNext()).andReturn(false);
                expect(this.list.listIterator(0)).andReturn(this.iterator);
                expect(this.iterator.hasNext()).andReturn(true).times(100);
                expect(this.iterator.next()).andReturn(this.result).times(100);
                this.resultStrategy.toSAX(this.result, this.xmlConsumer);
                expectLastCall().times(100);
                expect(this.iterator.hasNext()).andReturn(false);
                replay(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
                this.strategy.toSAX(this.list, this.xmlConsumer);
                assertEquals(
                                this.xmlConsumer.getExpectedResult(this,
                                                "PagingSearchResultListSAXStrategyTest-testPage0ToSAX.xml"),
                                this.xmlConsumer.getStringValue());
                verify(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
        }

        @Test
        public void testPage1ToSAX() throws SAXException, IOException {
                expect(this.list.getPagingData()).andReturn(this.pagingData);
                expect(this.pagingData.getStart()).andReturn(100);
                expect(this.pagingData.getLength()).andReturn(100);
                expect(this.list.size()).andReturn(256);
                expect(this.pagingData.getPage()).andReturn(1);
                expect(this.pagingData.getPages()).andReturn(3);
                expect(this.list.getQuery()).andReturn("query");
                expect(this.list.iterator()).andReturn(this.iterator);
                expect(this.iterator.hasNext()).andReturn(false);
                expect(this.list.listIterator(100)).andReturn(this.iterator);
                expect(this.iterator.hasNext()).andReturn(true).times(100);
                expect(this.iterator.next()).andReturn(this.result).times(100);
                this.resultStrategy.toSAX(this.result, this.xmlConsumer);
                expectLastCall().times(100);
                expect(this.iterator.hasNext()).andReturn(false);
                replay(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
                this.strategy.toSAX(this.list, this.xmlConsumer);
                assertEquals(
                                this.xmlConsumer.getExpectedResult(this,
                                                "PagingSearchResultListSAXStrategyTest-testPage1ToSAX.xml"),
                                this.xmlConsumer.getStringValue());
                verify(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
        }

        @Test
        public void testPage2ToSAX() throws SAXException, IOException {
                expect(this.list.getPagingData()).andReturn(this.pagingData);
                expect(this.pagingData.getStart()).andReturn(200);
                expect(this.pagingData.getLength()).andReturn(100);
                expect(this.list.size()).andReturn(256);
                expect(this.pagingData.getPage()).andReturn(2);
                expect(this.pagingData.getPages()).andReturn(3);
                expect(this.list.getQuery()).andReturn("query");
                expect(this.list.iterator()).andReturn(this.iterator);
                expect(this.iterator.hasNext()).andReturn(false);
                expect(this.list.listIterator(200)).andReturn(this.iterator);
                expect(this.iterator.hasNext()).andReturn(true).times(56);
                expect(this.iterator.next()).andReturn(this.result).times(56);
                this.resultStrategy.toSAX(this.result, this.xmlConsumer);
                expectLastCall().times(56);
                expect(this.iterator.hasNext()).andReturn(false);
                replay(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
                this.strategy.toSAX(this.list, this.xmlConsumer);
                assertEquals(
                                this.xmlConsumer.getExpectedResult(this,
                                                "PagingSearchResultListSAXStrategyTest-testPage2ToSAX.xml"),
                                this.xmlConsumer.getStringValue());
                verify(this.results, this.result, this.iterator, this.list, this.pagingData, this.resultStrategy);
        }
}
