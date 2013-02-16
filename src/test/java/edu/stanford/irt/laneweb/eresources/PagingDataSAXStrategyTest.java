package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.resource.PagingData;

public class PagingDataSAXStrategyTest {

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    private PagingData pagingData;

    private PagingDataSAXStrategy strategy;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.strategy = new PagingDataSAXStrategy();
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.pagingData = createMock(PagingData.class);
    }

    @Test
    public void testToSAX() throws SAXException {
        expect(this.pagingData.getSize()).andReturn(0);
        expect(this.pagingData.getLength()).andReturn(0);
        expect(this.pagingData.getStart()).andReturn(0);
        expect(this.pagingData.getBaseQuery()).andReturn("");
        this.xmlConsumer.startPrefixMapping("fx", "http://lane.stanford.edu/fx");
        this.xmlConsumer.startElement(eq(XHTML_NS), eq("div"), eq("div"), isA(Attributes.class));
        this.xmlConsumer.startElement(eq(XHTML_NS), eq("div"), eq("div"), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("Displaying all 0 matches".toCharArray()), eq(0), eq(24));
        this.xmlConsumer.endElement(XHTML_NS, "div", "div");
        this.xmlConsumer.startElement(eq(XHTML_NS), eq("div"), eq("div"), isA(Attributes.class));
        this.xmlConsumer.endElement(XHTML_NS, "div", "div");
        this.xmlConsumer.endElement(XHTML_NS, "div", "div");
        this.xmlConsumer.endPrefixMapping("fx");
        replay(this.pagingData, this.xmlConsumer);
        this.strategy.toSAX(this.pagingData, this.xmlConsumer);
        verify(this.pagingData, this.xmlConsumer);
    }
}
