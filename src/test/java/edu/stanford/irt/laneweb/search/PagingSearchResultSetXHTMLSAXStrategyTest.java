package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.aryEq;
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
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.resource.Resource;

public class PagingSearchResultSetXHTMLSAXStrategyTest {

    private Iterator<SearchResult> iterator;

    private SearchResult result;

    private Set<SearchResult> results;

    private SAXStrategy<SearchResult> resultStrategy;

    private PagingSearchResultSet set;

    private PagingSearchResultSetSAXStrategy strategy;

    private XMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.results = createMock(Set.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.result = createMock(SearchResult.class);
        this.iterator = createMock(Iterator.class);
        this.resultStrategy = createMock(SAXStrategy.class);
        this.strategy = new PagingSearchResultSetSAXStrategy(this.resultStrategy);
        this.set = createMock(PagingSearchResultSet.class);
    }

    @Test
    public void testAllPages539ToSAX() throws SAXException {
        expect(this.set.getPage()).andReturn(-1);
        expect(this.set.size()).andReturn(539);
        expect(this.set.getQuery()).andReturn("query");
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES), capture(atts));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.QUERY), eq(Resource.QUERY), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("query".toCharArray()), eq(0), eq(5));
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.QUERY), eq(Resource.QUERY));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.CONTENT_HIT_COUNTS), eq(Resource.CONTENT_HIT_COUNTS),
                isA(Attributes.class));
        expect(this.set.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(false);
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.CONTENT_HIT_COUNTS), eq(Resource.CONTENT_HIT_COUNTS));
        this.xmlConsumer.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        expect(this.set.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(true).times(539);
        expect(this.iterator.next()).andReturn(this.result).times(539);
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("dd"), eq("dd"), isA(AttributesImpl.class));
        expectLastCall().times(539);
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("ul"), eq("ul"), isA(AttributesImpl.class));
        expectLastCall().times(539);
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        expectLastCall().times(539);
        this.resultStrategy.toSAX(this.result, this.xmlConsumer);
        expectLastCall().times(539);
        this.xmlConsumer.endPrefixMapping("");
        expectLastCall().times(539);
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "ul", "ul");
        expectLastCall().times(539);
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "dd", "dd");
        expectLastCall().times(539);
        expect(this.iterator.hasNext()).andReturn(false);
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.results, this.xmlConsumer, this.result, this.iterator, this.set);
        this.strategy.toSAX(this.set, this.xmlConsumer);
        assertEquals("539", atts.getValue().getValue("size"));
        assertEquals("0", atts.getValue().getValue("start"));
        assertEquals("539", atts.getValue().getValue("length"));
        assertEquals("-1", atts.getValue().getValue("page"));
        assertEquals("4", atts.getValue().getValue("pages"));
        verify(this.results, this.xmlConsumer, this.result, this.iterator, this.set);
    }

    @Test
    public void testAllPagesToSAX() throws SAXException {
        expect(this.set.getPage()).andReturn(-1);
        expect(this.set.size()).andReturn(256);
        expect(this.set.getQuery()).andReturn("query");
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES), capture(atts));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.QUERY), eq(Resource.QUERY), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("query".toCharArray()), eq(0), eq(5));
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.QUERY), eq(Resource.QUERY));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.CONTENT_HIT_COUNTS), eq(Resource.CONTENT_HIT_COUNTS),
                isA(Attributes.class));
        expect(this.set.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(false);
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.CONTENT_HIT_COUNTS), eq(Resource.CONTENT_HIT_COUNTS));
        this.xmlConsumer.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        expect(this.set.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(true).times(256);
        expect(this.iterator.next()).andReturn(this.result).times(256);
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("dd"), eq("dd"), isA(AttributesImpl.class));
        expectLastCall().times(256);
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("ul"), eq("ul"), isA(AttributesImpl.class));
        expectLastCall().times(256);
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        expectLastCall().times(256);
        this.resultStrategy.toSAX(this.result, this.xmlConsumer);
        expectLastCall().times(256);
        this.xmlConsumer.endPrefixMapping("");
        expectLastCall().times(256);
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "ul", "ul");
        expectLastCall().times(256);
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "dd", "dd");
        expectLastCall().times(256);
        expect(this.iterator.hasNext()).andReturn(false);
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.results, this.xmlConsumer, this.result, this.iterator, this.set);
        this.strategy.toSAX(this.set, this.xmlConsumer);
        assertEquals("256", atts.getValue().getValue("size"));
        assertEquals("0", atts.getValue().getValue("start"));
        assertEquals("256", atts.getValue().getValue("length"));
        assertEquals("-1", atts.getValue().getValue("page"));
        assertEquals("3", atts.getValue().getValue("pages"));
        verify(this.results, this.xmlConsumer, this.result, this.iterator, this.set);
    }

    @Test
    public void testNullQuery() throws SAXException {
        expect(this.set.getPage()).andReturn(0);
        expect(this.set.size()).andReturn(0);
        expect(this.set.getQuery()).andReturn(null);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES), capture(atts));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.CONTENT_HIT_COUNTS), eq(Resource.CONTENT_HIT_COUNTS),
                isA(Attributes.class));
        expect(this.set.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(false);
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.CONTENT_HIT_COUNTS), eq(Resource.CONTENT_HIT_COUNTS));
        this.xmlConsumer.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        expect(this.set.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(false);
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.results, this.xmlConsumer, this.result, this.iterator, this.set);
        this.strategy.toSAX(this.set, this.xmlConsumer);
        assertEquals("0", atts.getValue().getValue("size"));
        assertEquals("0", atts.getValue().getValue("start"));
        assertEquals("0", atts.getValue().getValue("length"));
        assertEquals("0", atts.getValue().getValue("page"));
        assertEquals("0", atts.getValue().getValue("pages"));
        verify(this.results, this.xmlConsumer, this.result, this.iterator, this.set);
    }

    @Test
    public void testPage0ToSAX() throws SAXException {
        expect(this.set.getPage()).andReturn(0);
        expect(this.set.size()).andReturn(256);
        expect(this.set.getQuery()).andReturn("query");
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES), capture(atts));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.QUERY), eq(Resource.QUERY), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("query".toCharArray()), eq(0), eq(5));
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.QUERY), eq(Resource.QUERY));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.CONTENT_HIT_COUNTS), eq(Resource.CONTENT_HIT_COUNTS),
                isA(Attributes.class));
        expect(this.set.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(false);
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.CONTENT_HIT_COUNTS), eq(Resource.CONTENT_HIT_COUNTS));
        this.xmlConsumer.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        expect(this.set.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(true).times(100);
        expect(this.iterator.next()).andReturn(this.result).times(100);
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("dd"), eq("dd"), isA(AttributesImpl.class));
        expectLastCall().times(100);
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("ul"), eq("ul"), isA(AttributesImpl.class));
        expectLastCall().times(100);
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        expectLastCall().times(100);
        this.resultStrategy.toSAX(this.result, this.xmlConsumer);
        expectLastCall().times(100);
        this.xmlConsumer.endPrefixMapping("");
        expectLastCall().times(100);
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "ul", "ul");
        expectLastCall().times(100);
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "dd", "dd");
        expectLastCall().times(100);
        expect(this.iterator.hasNext()).andReturn(false);
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.results, this.xmlConsumer, this.result, this.iterator, this.set);
        this.strategy.toSAX(this.set, this.xmlConsumer);
        assertEquals("256", atts.getValue().getValue("size"));
        assertEquals("0", atts.getValue().getValue("start"));
        assertEquals("100", atts.getValue().getValue("length"));
        assertEquals("0", atts.getValue().getValue("page"));
        assertEquals("3", atts.getValue().getValue("pages"));
        verify(this.results, this.xmlConsumer, this.result, this.iterator, this.set);
    }

    @Test
    public void testPage1ToSAX() throws SAXException {
        expect(this.set.getPage()).andReturn(1);
        expect(this.set.size()).andReturn(256);
        expect(this.set.getQuery()).andReturn("query");
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES), capture(atts));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.QUERY), eq(Resource.QUERY), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("query".toCharArray()), eq(0), eq(5));
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.QUERY), eq(Resource.QUERY));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.CONTENT_HIT_COUNTS), eq(Resource.CONTENT_HIT_COUNTS),
                isA(Attributes.class));
        expect(this.set.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(false);
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.CONTENT_HIT_COUNTS), eq(Resource.CONTENT_HIT_COUNTS));
        this.xmlConsumer.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        expect(this.set.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(true).times(200);
        expect(this.iterator.next()).andReturn(this.result).times(200);
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("dd"), eq("dd"), isA(AttributesImpl.class));
        expectLastCall().times(100);
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("ul"), eq("ul"), isA(AttributesImpl.class));
        expectLastCall().times(100);
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        expectLastCall().times(100);
        this.resultStrategy.toSAX(this.result, this.xmlConsumer);
        expectLastCall().times(100);
        this.xmlConsumer.endPrefixMapping("");
        expectLastCall().times(100);
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "ul", "ul");
        expectLastCall().times(100);
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "dd", "dd");
        expectLastCall().times(100);
        expect(this.iterator.hasNext()).andReturn(false);
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.results, this.xmlConsumer, this.result, this.iterator, this.set);
        this.strategy.toSAX(this.set, this.xmlConsumer);
        assertEquals("256", atts.getValue().getValue("size"));
        assertEquals("100", atts.getValue().getValue("start"));
        assertEquals("100", atts.getValue().getValue("length"));
        assertEquals("1", atts.getValue().getValue("page"));
        assertEquals("3", atts.getValue().getValue("pages"));
        verify(this.results, this.xmlConsumer, this.result, this.iterator, this.set);
    }

    @Test
    public void testPage2ToSAX() throws SAXException {
        expect(this.set.getPage()).andReturn(2);
        expect(this.set.size()).andReturn(256);
        expect(this.set.getQuery()).andReturn("query");
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        Capture<Attributes> atts = new Capture<Attributes>();
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES), capture(atts));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.QUERY), eq(Resource.QUERY), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("query".toCharArray()), eq(0), eq(5));
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.QUERY), eq(Resource.QUERY));
        this.xmlConsumer.startElement(eq(Resource.NAMESPACE), eq(Resource.CONTENT_HIT_COUNTS), eq(Resource.CONTENT_HIT_COUNTS),
                isA(Attributes.class));
        expect(this.set.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(false);
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.CONTENT_HIT_COUNTS), eq(Resource.CONTENT_HIT_COUNTS));
        this.xmlConsumer.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        expect(this.set.iterator()).andReturn(this.iterator);
        expect(this.iterator.hasNext()).andReturn(true).times(256);
        expect(this.iterator.next()).andReturn(this.result).times(256);
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("dd"), eq("dd"), isA(AttributesImpl.class));
        expectLastCall().times(56);
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("ul"), eq("ul"), isA(AttributesImpl.class));
        expectLastCall().times(56);
        this.xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
        expectLastCall().times(56);
        this.resultStrategy.toSAX(this.result, this.xmlConsumer);
        expectLastCall().times(56);
        this.xmlConsumer.endPrefixMapping("");
        expectLastCall().times(56);
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "ul", "ul");
        expectLastCall().times(56);
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "dd", "dd");
        expectLastCall().times(56);
        expect(this.iterator.hasNext()).andReturn(false);
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endElement(eq(Resource.NAMESPACE), eq(Resource.RESOURCES), eq(Resource.RESOURCES));
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.results, this.xmlConsumer, this.result, this.iterator, this.set);
        this.strategy.toSAX(this.set, this.xmlConsumer);
        assertEquals("256", atts.getValue().getValue("size"));
        assertEquals("200", atts.getValue().getValue("start"));
        assertEquals("56", atts.getValue().getValue("length"));
        assertEquals("2", atts.getValue().getValue("page"));
        assertEquals("3", atts.getValue().getValue("pages"));
        verify(this.results, this.xmlConsumer, this.result, this.iterator, this.set);
    }
}
