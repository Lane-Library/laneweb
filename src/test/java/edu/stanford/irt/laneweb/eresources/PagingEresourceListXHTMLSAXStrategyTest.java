package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.eresources.Eresource;

public class PagingEresourceListXHTMLSAXStrategyTest {

    private static final String BODY = "body";

    private static final String DL = "dl";

    private static final String HEAD = "head";

    private static final String HTML = "html";

    private static final String TITLE = "title";

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    private Eresource eresource;

    private Eresource[] eresourceArray = new Eresource[256];

    private SAXStrategy<Eresource> eresourceStrategy;

    private PagingEresourceList list;

    private ListIterator<Eresource> listIterator;

    private PagingLabel pagingLabel;

    private List<PagingLabel> pagingLabels;

    private ListIterator<PagingLabel> pagingLabelsIterator;

    private PagingEresourceListXHTMLSAXStrategy strategy;

    private XMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.eresource = createMock(Eresource.class);
        Arrays.fill(this.eresourceArray, this.eresource);
        this.eresourceStrategy = createMock(SAXStrategy.class);
        this.strategy = new PagingEresourceListXHTMLSAXStrategy(this.eresourceStrategy);
        this.list = createMock(PagingEresourceList.class);
        this.listIterator = createMock(ListIterator.class);
        this.pagingLabel = createMock(PagingLabel.class);
        this.pagingLabels = createMock(List.class);
        this.pagingLabelsIterator = createMock(ListIterator.class);
    }

    @Test
    public void testAllPagesToSAX() throws SAXException {
        expect(this.list.getStart()).andReturn(0);
        expect(this.list.getLength()).andReturn(256);
        recordHead();
        expect(this.eresource.getDescription()).andReturn(null).times(256);
        expect(this.list.listIterator(0)).andReturn(this.listIterator);
        expect(this.listIterator.hasNext()).andReturn(true).times(256);
        expect(this.listIterator.next()).andReturn(this.eresource).times(256);
        this.xmlConsumer.startElement(eq(XHTML_NS), eq("dd"), eq("dd"), isA(Attributes.class));
        expectLastCall().times(256);
        this.xmlConsumer.startElement(eq(XHTML_NS), eq("ul"), eq("ul"), isA(Attributes.class));
        expectLastCall().times(256);
        this.eresourceStrategy.toSAX(this.eresource, this.xmlConsumer);
        expectLastCall().times(256);
        this.xmlConsumer.endElement(XHTML_NS, "ul", "ul");
        expectLastCall().times(256);
        this.xmlConsumer.endElement(XHTML_NS, "dd", "dd");
        expectLastCall().times(256);
        expect(this.listIterator.hasNext()).andReturn(false);
        recordTail();
        replay(this.xmlConsumer, this.eresource, this.eresourceStrategy, this.list, this.listIterator,
                this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);
        this.strategy.toSAX(this.list, this.xmlConsumer);
        verify(this.xmlConsumer, this.eresource, this.eresourceStrategy, this.list, this.listIterator,
                this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);
    }

    @Test
    public void testPage0ToSAX() throws SAXException {
        expect(this.list.getStart()).andReturn(0);
        expect(this.list.getLength()).andReturn(100);
        recordHead();
        expect(this.eresource.getDescription()).andReturn(null).times(100);
        expect(this.list.listIterator(0)).andReturn(this.listIterator);
        expect(this.listIterator.hasNext()).andReturn(true).times(101);
        expect(this.listIterator.next()).andReturn(this.eresource).times(100);
        this.xmlConsumer.startElement(eq(XHTML_NS), eq("dd"), eq("dd"), isA(Attributes.class));
        expectLastCall().times(100);
        this.xmlConsumer.startElement(eq(XHTML_NS), eq("ul"), eq("ul"), isA(Attributes.class));
        expectLastCall().times(100);
        this.eresourceStrategy.toSAX(this.eresource, this.xmlConsumer);
        expectLastCall().times(100);
        this.xmlConsumer.endElement(XHTML_NS, "ul", "ul");
        expectLastCall().times(100);
        this.xmlConsumer.endElement(XHTML_NS, "dd", "dd");
        expectLastCall().times(100);
        recordTail();
        replay(this.xmlConsumer, this.eresource, this.eresourceStrategy, this.list, this.listIterator,
                this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);
        this.strategy.toSAX(this.list, this.xmlConsumer);
        verify(this.xmlConsumer, this.eresource, this.eresourceStrategy, this.list, this.listIterator,
                this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);
    }

    @Test
    public void testPage1ToSAX() throws SAXException {
        expect(this.list.getStart()).andReturn(100);
        expect(this.list.getLength()).andReturn(100);
        recordHead();
        expect(this.eresource.getDescription()).andReturn(null).times(100);
        expect(this.list.listIterator(100)).andReturn(this.listIterator);
        expect(this.listIterator.hasNext()).andReturn(true).times(101);
        expect(this.listIterator.next()).andReturn(this.eresource).times(100);
        this.xmlConsumer.startElement(eq(XHTML_NS), eq("dd"), eq("dd"), isA(Attributes.class));
        expectLastCall().times(100);
        this.xmlConsumer.startElement(eq(XHTML_NS), eq("ul"), eq("ul"), isA(Attributes.class));
        expectLastCall().times(100);
        this.eresourceStrategy.toSAX(this.eresource, this.xmlConsumer);
        expectLastCall().times(100);
        this.xmlConsumer.endElement(XHTML_NS, "ul", "ul");
        expectLastCall().times(100);
        this.xmlConsumer.endElement(XHTML_NS, "dd", "dd");
        expectLastCall().times(100);
        recordTail();
        replay(this.xmlConsumer, this.eresource, this.eresourceStrategy, this.list, this.listIterator,
                this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);
        this.strategy.toSAX(this.list, this.xmlConsumer);
        verify(this.xmlConsumer, this.eresource, this.eresourceStrategy, this.list, this.listIterator,
                this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);
    }

    @Test
    public void testPage2ToSAX() throws SAXException {
        expect(this.list.getStart()).andReturn(200);
        expect(this.list.getLength()).andReturn(56);
        recordHead();
        expect(this.eresource.getDescription()).andReturn(null).times(56);
        expect(this.list.listIterator(200)).andReturn(this.listIterator);
        expect(this.listIterator.hasNext()).andReturn(true).times(57);
        expect(this.listIterator.next()).andReturn(this.eresource).times(56);
        this.xmlConsumer.startElement(eq(XHTML_NS), eq("dd"), eq("dd"), isA(Attributes.class));
        expectLastCall().times(56);
        this.xmlConsumer.startElement(eq(XHTML_NS), eq("ul"), eq("ul"), isA(Attributes.class));
        expectLastCall().times(56);
        this.eresourceStrategy.toSAX(this.eresource, this.xmlConsumer);
        expectLastCall().times(56);
        this.xmlConsumer.endElement(XHTML_NS, "ul", "ul");
        expectLastCall().times(56);
        this.xmlConsumer.endElement(XHTML_NS, "dd", "dd");
        expectLastCall().times(56);
        recordTail();
        replay(this.xmlConsumer, this.eresource, this.eresourceStrategy, this.list, this.listIterator,
                this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);
        this.strategy.toSAX(this.list, this.xmlConsumer);
        verify(this.xmlConsumer, this.eresource, this.eresourceStrategy, this.list, this.listIterator,
                this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);
    }

    @Test
    public void testPage3With596ToSAX() throws SAXException {
        expect(this.list.getStart()).andReturn(447);
        expect(this.list.getLength()).andReturn(149);
        recordHead();
        expect(this.eresource.getDescription()).andReturn(null).times(149);
        expect(this.list.listIterator(447)).andReturn(this.listIterator);
        expect(this.listIterator.hasNext()).andReturn(true).times(150);
        expect(this.listIterator.next()).andReturn(this.eresource).times(149);
        this.xmlConsumer.startElement(eq(XHTML_NS), eq("dd"), eq("dd"), isA(Attributes.class));
        expectLastCall().times(149);
        this.xmlConsumer.startElement(eq(XHTML_NS), eq("ul"), eq("ul"), isA(Attributes.class));
        expectLastCall().times(149);
        this.eresourceStrategy.toSAX(this.eresource, this.xmlConsumer);
        expectLastCall().times(149);
        this.xmlConsumer.endElement(XHTML_NS, "ul", "ul");
        expectLastCall().times(149);
        this.xmlConsumer.endElement(XHTML_NS, "dd", "dd");
        expectLastCall().times(149);
        recordTail();
        replay(this.xmlConsumer, this.eresource, this.eresourceStrategy, this.list, this.listIterator,
                this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);
        this.strategy.toSAX(this.list, this.xmlConsumer);
        verify(this.xmlConsumer, this.eresource, this.eresourceStrategy, this.list, this.listIterator,
                this.pagingLabel, this.pagingLabels, this.pagingLabelsIterator);
    }

    private void recordHead() throws SAXException {
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", XHTML_NS);
        this.xmlConsumer.startElement(eq(XHTML_NS), eq(HTML), eq(HTML), isA(Attributes.class));
        this.xmlConsumer.startElement(eq(XHTML_NS), eq(HEAD), eq(HEAD), isA(Attributes.class));
        this.xmlConsumer.startElement(eq(XHTML_NS), eq(TITLE), eq(TITLE), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("search results".toCharArray()), eq(0), eq(14));
        this.xmlConsumer.endElement(XHTML_NS, TITLE, TITLE);
        this.xmlConsumer.endElement(XHTML_NS, HEAD, HEAD);
        this.xmlConsumer.startElement(eq(XHTML_NS), eq(BODY), eq(BODY), isA(Attributes.class));
        this.xmlConsumer.startElement(eq(XHTML_NS), eq(DL), eq(DL), isA(Attributes.class));
    }

    private void recordTail() throws SAXException {
        this.xmlConsumer.endElement(XHTML_NS, DL, DL);
        expect(this.list.size()).andReturn(0);
        this.xmlConsumer.startElement(eq(XHTML_NS), eq("div"), eq("div"), isA(Attributes.class));
        this.xmlConsumer.startElement(eq(XHTML_NS), eq("div"), eq("div"), isA(Attributes.class));
        this.xmlConsumer.startElement(eq(XHTML_NS), eq("div"), eq("div"), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("Displaying all 0 matches".toCharArray()), eq(0), eq(24));
        this.xmlConsumer.endElement(XHTML_NS, "div", "div");
        this.xmlConsumer.startElement(eq(XHTML_NS), eq("div"), eq("div"), isA(Attributes.class));
        this.xmlConsumer.endElement(XHTML_NS, "div", "div");
        this.xmlConsumer.endElement(XHTML_NS, "div", "div");
        this.xmlConsumer.endElement(XHTML_NS, "div", "div");
        this.xmlConsumer.startElement(eq(XHTML_NS), eq("div"), eq("div"), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq(" ".toCharArray()), eq(0), eq(1));
        this.xmlConsumer.endElement(XHTML_NS, "div", "div");
        this.xmlConsumer.endElement(XHTML_NS, BODY, BODY);
        this.xmlConsumer.endElement(XHTML_NS, HTML, HTML);
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
    }
}
