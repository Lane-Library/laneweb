package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.Link;
import edu.stanford.irt.eresources.Version;

public class EresourceXHTMLSAXStrategyTest {

    private static final String A = "a";

    private static final String DIV = "div";

    private static final String LI = "li";

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    private Eresource eresource;

    private Link link;

    private EresourceXHTMLSAXStrategy strategy;

    private Version version;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.strategy = new EresourceXHTMLSAXStrategy();
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.eresource = createMock(Eresource.class);
        this.version = createMock(Version.class);
        this.link = createMock(Link.class);
    }

    @Test
    public void testToSAX() throws SAXException {
        this.xmlConsumer.startElement(eq(XHTML_NS), eq(LI), eq(LI), isA(Attributes.class));
        expect(this.eresource.getVersions()).andReturn(Collections.singletonList(this.version));
        expect(this.eresource.getTitle()).andReturn("title");
        expect(this.version.getLinks()).andReturn(Collections.singletonList(this.link));
        expect(this.link.getLabel()).andReturn("label");
        expect(this.link.getUrl()).andReturn("url");
        this.xmlConsumer.startElement(eq(XHTML_NS), eq(A), eq(A), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("title".toCharArray()), eq(0), eq(5));
        this.xmlConsumer.endElement(XHTML_NS, A, A);
        expect(this.version.getSummaryHoldings()).andReturn("summary holdings");
        expect(this.version.getDates()).andReturn("dates");
        expect(this.version.getPublisher()).andReturn("publisher");
        expect(this.version.getDescription()).andReturn("description");
        expect(this.link.getInstruction()).andReturn("instruction");
        this.xmlConsumer.characters(
                aryEq(" summary holdings, dates, publisher, description, instruction ".toCharArray()), eq(0), eq(62));
        this.xmlConsumer.startElement(eq(XHTML_NS), eq(DIV), eq(DIV), isA(Attributes.class));
        expect(this.eresource.getRecordId()).andReturn(0);
        this.xmlConsumer.startElement(eq(XHTML_NS), eq(A), eq(A), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("Lane Catalog record".toCharArray()), eq(0), eq(19));
        this.xmlConsumer.endElement(XHTML_NS, A, A);
        this.xmlConsumer.endElement(XHTML_NS, DIV, DIV);
        this.xmlConsumer.endElement(XHTML_NS, LI, LI);
        expect(this.eresource.getDescription()).andReturn("description");
        this.xmlConsumer.startElement(eq(XHTML_NS), eq(LI), eq(LI), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("description".toCharArray()), eq(0), eq(11));
        this.xmlConsumer.endElement(XHTML_NS, LI, LI);
        replay(this.eresource, this.link, this.version, this.xmlConsumer);
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        verify(this.eresource, this.link, this.version, this.xmlConsumer);
    }
}
