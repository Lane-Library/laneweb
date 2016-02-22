package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.solr.Eresource;
import edu.stanford.irt.laneweb.solr.Link;
import edu.stanford.irt.laneweb.solr.LinkType;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class EresourceXHTMLSAXStrategyTest {

    private Eresource eresource;

    private Link link;

    private EresourceXHTMLSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.strategy = new EresourceXHTMLSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
        this.eresource = createMock(Eresource.class);
        this.link = createMock(Link.class);
    }

    @Test
    public void testToSAX() throws SAXException, IOException {
        expect(this.eresource.getLinks()).andReturn(Arrays.asList(new Link[] { this.link }));
        expect(this.link.getLinkText()).andReturn("title");
        expect(this.link.getAdditionalText()).andReturn("publisher, additional text, instruction");
        expect(this.link.getUrl()).andReturn("url");
        expect(this.link.getHoldingsAndDates()).andReturn("holdings, dates").times(2);
        expect(this.link.getType()).andReturn(LinkType.NORMAL);
        expect(this.eresource.getRecordType()).andReturn("bib");
        expect(this.eresource.getPrimaryType()).andReturn("primary type");
        expect(this.eresource.getRecordId()).andReturn(0);
        expect(this.eresource.getDescription()).andReturn("description");
        replay(this.eresource, this.link);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "EeresourceXHTMLSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.eresource, this.link);
    }

    @Test
    public void testToSAX2Links() throws SAXException, IOException {
        expect(this.eresource.getLinks()).andReturn(Arrays.asList(new Link[] { this.link, this.link }));
        expect(this.link.getLinkText()).andReturn("title").times(2);
        expect(this.link.getAdditionalText()).andReturn("publisher, additional text, instruction").times(2);
        expect(this.link.getUrl()).andReturn("url").times(2);
        expect(this.link.getHoldingsAndDates()).andReturn("holdings, dates").times(2);
        expect(this.link.getLabel()).andReturn("label");
        expect(this.link.getType()).andReturn(LinkType.NORMAL).times(2);
        expect(this.eresource.getRecordType()).andReturn("bib");
        expect(this.eresource.getPrimaryType()).andReturn("primary type");
        expect(this.eresource.getRecordId()).andReturn(0);
        expect(this.eresource.getDescription()).andReturn("description");
        replay(this.eresource, this.link);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "EeresourceXHTMLSAXStrategyTest-testToSAX2Links.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.eresource, this.link);
    }

    @Test
    public void testToSAX2ndLinkGetPassword() throws SAXException, IOException {
        expect(this.eresource.getLinks()).andReturn(Arrays.asList(new Link[] { this.link, this.link }));
        expect(this.link.getLinkText()).andReturn("title").times(2);
        expect(this.link.getAdditionalText()).andReturn("publisher, additional text, instruction").times(2);
        expect(this.link.getUrl()).andReturn("url").times(2);
        expect(this.link.getHoldingsAndDates()).andReturn("holdings, dates").times(2);
        expect(this.link.getLabel()).andReturn("label");
        expect(this.link.getType()).andReturn(LinkType.NORMAL);
        expect(this.link.getType()).andReturn(LinkType.GETPASSWORD);
        expect(this.eresource.getRecordType()).andReturn("bib");
        expect(this.eresource.getPrimaryType()).andReturn("primary type");
        expect(this.eresource.getRecordId()).andReturn(0);
        expect(this.eresource.getDescription()).andReturn("description");
        replay(this.eresource, this.link);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this,
                "EeresourceXHTMLSAXStrategyTest-testToSAX2ndLinkGetPassword.xml"), this.xmlConsumer.getStringValue());
        verify(this.eresource, this.link);
    }

    @Test
    public void testToSAXAuth() throws SAXException, IOException {
        expect(this.eresource.getLinks()).andReturn(Arrays.asList(new Link[] { this.link }));
        expect(this.link.getLinkText()).andReturn("title");
        expect(this.link.getAdditionalText()).andReturn("publisher, additional text, instruction");
        expect(this.link.getUrl()).andReturn("url");
        expect(this.link.getHoldingsAndDates()).andReturn("holdings, dates").times(2);
        expect(this.link.getType()).andReturn(LinkType.NORMAL);
        expect(this.eresource.getRecordType()).andReturn("auth");
        expect(this.eresource.getDescription()).andReturn("description");
        replay(this.eresource, this.link);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "EeresourceXHTMLSAXStrategyTest-testToSAXAuth.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.eresource, this.link);
    }

    @Test
    public void testToSAXBadType() throws SAXException, IOException {
        expect(this.eresource.getLinks()).andReturn(Arrays.asList(new Link[] { this.link }));
        expect(this.link.getLinkText()).andReturn("title");
        expect(this.link.getAdditionalText()).andReturn("publisher, additional text, instruction");
        expect(this.link.getUrl()).andReturn("url");
        expect(this.link.getHoldingsAndDates()).andReturn("holdings, dates").times(2);
        expect(this.link.getType()).andReturn(LinkType.NORMAL);
        expect(this.eresource.getRecordType()).andReturn("bad");
        expect(this.eresource.getDescription()).andReturn("description");
        replay(this.eresource, this.link);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "EeresourceXHTMLSAXStrategyTest-testToSAXBadType.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.eresource, this.link);
    }

    @Test
    public void testToSAXClass() throws SAXException, IOException {
        expect(this.eresource.getLinks()).andReturn(Arrays.asList(new Link[] { this.link }));
        expect(this.link.getLinkText()).andReturn("title");
        expect(this.link.getAdditionalText()).andReturn("publisher, additional text, instruction");
        expect(this.link.getUrl()).andReturn("url");
        expect(this.link.getHoldingsAndDates()).andReturn("holdings, dates").times(2);
        expect(this.link.getType()).andReturn(LinkType.NORMAL);
        expect(this.eresource.getRecordType()).andReturn("class");
        expect(this.eresource.getDescription()).andReturn("description");
        replay(this.eresource, this.link);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "EeresourceXHTMLSAXStrategyTest-testToSAXClass.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.eresource, this.link);
    }

    @Test
    public void testToSAXEmptyAdditionalText() throws SAXException, IOException {
        expect(this.eresource.getLinks()).andReturn(Arrays.asList(new Link[] { this.link }));
        expect(this.link.getLinkText()).andReturn("title");
        expect(this.link.getAdditionalText()).andReturn("");
        expect(this.link.getUrl()).andReturn("url");
        expect(this.link.getHoldingsAndDates()).andReturn("holdings, dates").times(2);
        expect(this.link.getType()).andReturn(LinkType.NORMAL);
        expect(this.eresource.getRecordType()).andReturn("bib");
        expect(this.eresource.getPrimaryType()).andReturn("primary type");
        expect(this.eresource.getRecordId()).andReturn(0);
        expect(this.eresource.getDescription()).andReturn("description");
        replay(this.eresource, this.link);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this,
                "EeresourceXHTMLSAXStrategyTest-testToSAXNullAdditionalText.xml"), this.xmlConsumer.getStringValue());
        verify(this.eresource, this.link);
    }

    @Test
    public void testToSAXEmptyDescription() throws SAXException, IOException {
        expect(this.eresource.getLinks()).andReturn(Arrays.asList(new Link[] { this.link }));
        expect(this.link.getLinkText()).andReturn("title");
        expect(this.link.getAdditionalText()).andReturn("publisher, additional text, instruction");
        expect(this.link.getUrl()).andReturn("url");
        expect(this.link.getHoldingsAndDates()).andReturn("holdings, dates").times(2);
        expect(this.link.getType()).andReturn(LinkType.NORMAL);
        expect(this.eresource.getRecordType()).andReturn("bib");
        expect(this.eresource.getPrimaryType()).andReturn("primary type");
        expect(this.eresource.getRecordId()).andReturn(0);
        expect(this.eresource.getDescription()).andReturn("");
        replay(this.eresource, this.link);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(
                this.xmlConsumer.getExpectedResult(this, "EeresourceXHTMLSAXStrategyTest-testToSAXNullDescription.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.eresource, this.link);
    }

    @Test
    public void testToSAXGetPassword() throws SAXException, IOException {
        expect(this.eresource.getLinks()).andReturn(Arrays.asList(new Link[] { this.link }));
        expect(this.link.getLinkText()).andReturn("title");
        expect(this.link.getAdditionalText()).andReturn("publisher, additional text, instruction");
        expect(this.link.getUrl()).andReturn("url");
        expect(this.link.getHoldingsAndDates()).andReturn("holdings, dates").times(2);
        expect(this.link.getType()).andReturn(LinkType.GETPASSWORD);
        expect(this.eresource.getRecordType()).andReturn("bib");
        expect(this.eresource.getPrimaryType()).andReturn("primary type");
        expect(this.eresource.getRecordId()).andReturn(0);
        expect(this.eresource.getDescription()).andReturn("description");
        replay(this.eresource, this.link);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(
                this.xmlConsumer.getExpectedResult(this, "EeresourceXHTMLSAXStrategyTest-testToSAXGetPassword.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.eresource, this.link);
    }

    @Test
    public void testToSAXNullAdditionalText() throws SAXException, IOException {
        expect(this.eresource.getLinks()).andReturn(Arrays.asList(new Link[] { this.link }));
        expect(this.link.getLinkText()).andReturn("title");
        expect(this.link.getAdditionalText()).andReturn(null);
        expect(this.link.getUrl()).andReturn("url");
        expect(this.link.getHoldingsAndDates()).andReturn("holdings, dates").times(2);
        expect(this.link.getType()).andReturn(LinkType.NORMAL);
        expect(this.eresource.getRecordType()).andReturn("bib");
        expect(this.eresource.getPrimaryType()).andReturn("primary type");
        expect(this.eresource.getRecordId()).andReturn(0);
        expect(this.eresource.getDescription()).andReturn("description");
        replay(this.eresource, this.link);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this,
                "EeresourceXHTMLSAXStrategyTest-testToSAXNullAdditionalText.xml"), this.xmlConsumer.getStringValue());
        verify(this.eresource, this.link);
    }

    @Test
    public void testToSAXNullDescription() throws SAXException, IOException {
        expect(this.eresource.getLinks()).andReturn(Arrays.asList(new Link[] { this.link }));
        expect(this.link.getLinkText()).andReturn("title");
        expect(this.link.getAdditionalText()).andReturn("publisher, additional text, instruction");
        expect(this.link.getUrl()).andReturn("url");
        expect(this.link.getHoldingsAndDates()).andReturn("holdings, dates").times(2);
        expect(this.link.getType()).andReturn(LinkType.NORMAL);
        expect(this.eresource.getRecordType()).andReturn("bib");
        expect(this.eresource.getPrimaryType()).andReturn("primary type");
        expect(this.eresource.getRecordId()).andReturn(0);
        expect(this.eresource.getDescription()).andReturn(null);
        replay(this.eresource, this.link);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(
                this.xmlConsumer.getExpectedResult(this, "EeresourceXHTMLSAXStrategyTest-testToSAXNullDescription.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.eresource, this.link);
    }

    @Test
    public void testToSAXPrint() throws SAXException, IOException {
        expect(this.eresource.getLinks()).andReturn(Arrays.asList(new Link[] { this.link }));
        expect(this.link.getLinkText()).andReturn("title");
        expect(this.link.getAdditionalText()).andReturn("publisher, additional text, instruction");
        expect(this.link.getUrl()).andReturn("url");
        expect(this.link.getHoldingsAndDates()).andReturn("holdings, dates").times(2);
        expect(this.link.getType()).andReturn(LinkType.NORMAL);
        expect(this.eresource.getRecordType()).andReturn("bib");
        expect(this.eresource.getPrimaryType()).andReturn("foo Print");
        expect(this.eresource.getDescription()).andReturn("description");
        replay(this.eresource, this.link);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "EeresourceXHTMLSAXStrategyTest-testToSAXPrint.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.eresource, this.link);
    }

    @Test(expected = LanewebException.class)
    public void testToSAXThrowException() throws SAXException, IOException {
        XMLConsumer c = createMock(XMLConsumer.class);
        expect(this.eresource.getLinks()).andReturn(Collections.singletonList(this.link));
        expect(this.link.getLinkText()).andReturn("title");
        expect(this.link.getAdditionalText()).andReturn("publisher, additional text, instruction");
        expect(this.link.getLinkText()).andReturn("title");
        c.startElement(eq("http://www.w3.org/1999/xhtml"), eq("div"), eq("div"), isA(Attributes.class));
        expectLastCall().andThrow(new SAXException());
        replay(this.eresource, this.link, c);
        this.strategy.toSAX(this.eresource, c);
        verify(this.eresource, this.link, c);
    }

    @Test
    public void testToSAXWeb() throws SAXException, IOException {
        expect(this.eresource.getLinks()).andReturn(Arrays.asList(new Link[] { this.link }));
        expect(this.link.getLinkText()).andReturn("title");
        expect(this.link.getAdditionalText()).andReturn("publisher, additional text, instruction");
        expect(this.link.getUrl()).andReturn("url");
        expect(this.link.getHoldingsAndDates()).andReturn("holdings, dates").times(2);
        expect(this.link.getType()).andReturn(LinkType.NORMAL);
        expect(this.eresource.getRecordType()).andReturn("web");
        expect(this.eresource.getDescription()).andReturn("description");
        replay(this.eresource, this.link);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "EeresourceXHTMLSAXStrategyTest-testToSAXWeb.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.eresource, this.link);
    }
}
