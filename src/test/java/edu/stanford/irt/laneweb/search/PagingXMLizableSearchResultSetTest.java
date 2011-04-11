package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.Set;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.resource.Resource;

public class PagingXMLizableSearchResultSetTest {

    private ContentHandler handler;

    private Iterator<SearchResult> iterator;

    private SearchResult result;

    private Set<SearchResult> results;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.results = createMock(Set.class);
        this.handler = createMock(ContentHandler.class);
        this.result = createMock(SearchResult.class);
        this.iterator = createMock(Iterator.class);
    }

    @Test
    public void testAllPages539ToSAX() throws SAXException {
        expect(this.results.size()).andReturn(Integer.valueOf(539));
        expect(this.results.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(Boolean.TRUE).times(539);
        expect(this.iterator.hasNext()).andReturn(Boolean.FALSE);
        expect(this.iterator.next()).andReturn(this.result).times(539);
        this.handler.startDocument();
        this.handler.startPrefixMapping("", Resource.NAMESPACE);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.handler.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES), capture(atts));
        this.handler.startElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class), isA(Attributes.class));
        expectLastCall().times(2);
        this.handler.characters(isA(char[].class), eq(0), eq(1));
        expectLastCall().once();
        this.handler.endElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class));
        expectLastCall().times(3);
        expect(this.result.compareTo(this.result)).andReturn(Integer.valueOf(1)).atLeastOnce();
        this.result.toSAX(this.handler);
        expectLastCall().times(539);
        this.handler.endPrefixMapping("");
        this.handler.endDocument();
        replay(this.results, this.handler, this.result, this.iterator);
        PagingXMLizableSearchResultSet set = new PagingXMLizableSearchResultSet("1", -1);
        set.addAll(this.results);
        set.toSAX(this.handler);
        assertEquals("539", atts.getValue().getValue("size"));
        assertEquals("0", atts.getValue().getValue("start"));
        assertEquals("539", atts.getValue().getValue("length"));
        assertEquals("-1", atts.getValue().getValue("page"));
        assertEquals("4", atts.getValue().getValue("pages"));
        verify(this.results, this.handler, this.result, this.iterator);
    }

    @Test
    public void testAllPagesToSAX() throws SAXException {
        expect(this.results.size()).andReturn(Integer.valueOf(256));
        expect(this.results.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(Boolean.TRUE).times(256);
        expect(this.iterator.hasNext()).andReturn(Boolean.FALSE);
        expect(this.iterator.next()).andReturn(this.result).times(256);
        this.handler.startDocument();
        this.handler.startPrefixMapping("", Resource.NAMESPACE);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.handler.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES), capture(atts));
        this.handler.startElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class), isA(Attributes.class));
        expectLastCall().times(2);
        this.handler.characters(isA(char[].class), eq(0), eq(1));
        expectLastCall().once();
        this.handler.endElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class));
        expectLastCall().times(3);
        expect(this.result.compareTo(this.result)).andReturn(Integer.valueOf(1)).atLeastOnce();
        this.result.toSAX(this.handler);
        expectLastCall().times(256);
        this.handler.endPrefixMapping("");
        this.handler.endDocument();
        replay(this.results, this.handler, this.result, this.iterator);
        PagingXMLizableSearchResultSet set = new PagingXMLizableSearchResultSet("1", -1);
        set.addAll(this.results);
        set.toSAX(this.handler);
        assertEquals("256", atts.getValue().getValue("size"));
        assertEquals("0", atts.getValue().getValue("start"));
        assertEquals("256", atts.getValue().getValue("length"));
        assertEquals("-1", atts.getValue().getValue("page"));
        assertEquals("3", atts.getValue().getValue("pages"));
        verify(this.results, this.handler, this.result, this.iterator);
    }

    @Test
    public void testPage0ToSAX() throws SAXException {
        expect(this.results.size()).andReturn(Integer.valueOf(256));
        expect(this.results.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(Boolean.TRUE).times(256);
        expect(this.iterator.hasNext()).andReturn(Boolean.FALSE);
        expect(this.iterator.next()).andReturn(this.result).times(256);
        this.handler.startDocument();
        this.handler.startPrefixMapping("", Resource.NAMESPACE);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.handler.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES), capture(atts));
        this.handler.startElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class), isA(Attributes.class));
        expectLastCall().times(2);
        this.handler.characters(isA(char[].class), eq(0), eq(1));
        expectLastCall().once();
        this.handler.endElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class));
        expectLastCall().times(3);
        expect(this.result.compareTo(this.result)).andReturn(Integer.valueOf(1)).atLeastOnce();
        this.result.toSAX(this.handler);
        expectLastCall().times(100);
        this.handler.endPrefixMapping("");
        this.handler.endDocument();
        replay(this.results, this.handler, this.result, this.iterator);
        PagingXMLizableSearchResultSet set = new PagingXMLizableSearchResultSet("1", 0);
        set.addAll(this.results);
        set.toSAX(this.handler);
        assertEquals("256", atts.getValue().getValue("size"));
        assertEquals("0", atts.getValue().getValue("start"));
        assertEquals("100", atts.getValue().getValue("length"));
        assertEquals("0", atts.getValue().getValue("page"));
        assertEquals("3", atts.getValue().getValue("pages"));
        verify(this.results, this.handler, this.result, this.iterator);
    }

    @Test
    public void testPage1ToSAX() throws SAXException {
        expect(this.results.size()).andReturn(Integer.valueOf(256));
        expect(this.results.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(Boolean.TRUE).times(256);
        expect(this.iterator.hasNext()).andReturn(Boolean.FALSE);
        expect(this.iterator.next()).andReturn(this.result).times(256);
        this.handler.startDocument();
        this.handler.startPrefixMapping("", Resource.NAMESPACE);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.handler.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES), capture(atts));
        this.handler.startElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class), isA(Attributes.class));
        expectLastCall().times(2);
        this.handler.characters(isA(char[].class), eq(0), eq(1));
        expectLastCall().once();
        this.handler.endElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class));
        expectLastCall().times(3);
        expect(this.result.compareTo(this.result)).andReturn(Integer.valueOf(1)).atLeastOnce();
        this.result.toSAX(this.handler);
        expectLastCall().times(100);
        this.handler.endPrefixMapping("");
        this.handler.endDocument();
        replay(this.results, this.handler, this.result, this.iterator);
        PagingXMLizableSearchResultSet set = new PagingXMLizableSearchResultSet("1", 1);
        set.addAll(this.results);
        set.toSAX(this.handler);
        assertEquals("256", atts.getValue().getValue("size"));
        assertEquals("100", atts.getValue().getValue("start"));
        assertEquals("100", atts.getValue().getValue("length"));
        assertEquals("1", atts.getValue().getValue("page"));
        assertEquals("3", atts.getValue().getValue("pages"));
        verify(this.results, this.handler, this.result, this.iterator);
    }

    @Test
    public void testPage2ToSAX() throws SAXException {
        expect(this.results.size()).andReturn(Integer.valueOf(256));
        expect(this.results.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(Boolean.TRUE).times(256);
        expect(this.iterator.hasNext()).andReturn(Boolean.FALSE);
        expect(this.iterator.next()).andReturn(this.result).times(256);
        this.handler.startDocument();
        this.handler.startPrefixMapping("", Resource.NAMESPACE);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.handler.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES), capture(atts));
        this.handler.startElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class), isA(Attributes.class));
        expectLastCall().times(2);
        this.handler.characters(isA(char[].class), eq(0), eq(1));
        expectLastCall().once();
        this.handler.endElement(eq(Resource.NAMESPACE), isA(String.class), isA(String.class));
        expectLastCall().times(3);
        expect(this.result.compareTo(this.result)).andReturn(Integer.valueOf(1)).atLeastOnce();
        this.result.toSAX(this.handler);
        expectLastCall().times(56);
        this.handler.endPrefixMapping("");
        this.handler.endDocument();
        replay(this.results, this.handler, this.result, this.iterator);
        PagingXMLizableSearchResultSet set = new PagingXMLizableSearchResultSet("1", 2);
        set.addAll(this.results);
        set.toSAX(this.handler);
        assertEquals("256", atts.getValue().getValue("size"));
        assertEquals("200", atts.getValue().getValue("start"));
        assertEquals("56", atts.getValue().getValue("length"));
        assertEquals("2", atts.getValue().getValue("page"));
        assertEquals("3", atts.getValue().getValue("pages"));
        verify(this.results, this.handler, this.result, this.iterator);
    }
}
