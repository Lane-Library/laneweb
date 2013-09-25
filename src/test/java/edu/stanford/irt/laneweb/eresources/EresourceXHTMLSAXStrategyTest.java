package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class EresourceXHTMLSAXStrategyTest {

    private Eresource eresource;

    private Link link;

    private EresourceXHTMLSAXStrategy strategy;

    private Version version;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.strategy = new EresourceXHTMLSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
        this.eresource = createMock(Eresource.class);
        this.version = createMock(Version.class);
        this.link = createMock(Link.class);
    }

    @Test
    public void testToSAX() throws SAXException, IOException {
        expect(this.eresource.getLinks()).andReturn(Collections.singletonList(this.link));
        expect(this.link.getLinkText()).andReturn("title");
        expect(this.link.getUrl()).andReturn("url");
        expect(((Link)this.link).getType()).andReturn(LinkType.NORMAL);
        expect(this.link.getAdditionalText()).andReturn("summary holdings, dates, publisher, description, instruction");
        expect(this.eresource.getRecordType()).andReturn("bib");
        expect(this.eresource.getRecordId()).andReturn(0);
        expect(this.eresource.getDescription()).andReturn("description");
        replay(this.eresource, this.link, this.version);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "EeresourceXHTMLSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.eresource, this.link, this.version);
    }

    @Test
    public void testToSAX2Versions() throws SAXException, IOException {
        expect(this.eresource.getLinks()).andReturn(Arrays.asList(new Link[] { this.link, this.link }));
        expect(this.link.getLinkText()).andReturn("title");
        expect(this.link.getUrl()).andReturn("url").times(2);
        expect(((Link)this.link).getType()).andReturn(LinkType.NORMAL).times(2);
        expect(this.link.getAdditionalText()).andReturn("summary holdings, dates, publisher, description, instruction");
        expect(this.link.getLinkText()).andReturn("summary holdings, dates description");
        expect(this.link.getLabel()).andReturn("label");
        expect(this.link.getAdditionalText()).andReturn("instruction publisher");
        expect(this.eresource.getRecordType()).andReturn("bib");
        expect(this.eresource.getRecordId()).andReturn(0);
        expect(this.eresource.getDescription()).andReturn("description");
        replay(this.eresource, this.link, this.version);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "EeresourceXHTMLSAXStrategyTest-testToSAX2Versions.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.eresource, this.link, this.version);
    }

    @Test
    public void testToSAXGetPassword() throws SAXException, IOException {
        expect(this.eresource.getLinks()).andReturn(Arrays.asList(new Link[] { this.link }));
        expect(this.link.getLinkText()).andReturn("title");
        expect(((Link)this.link).getType()).andReturn(LinkType.GETPASSWORD);
        expect(this.link.getUrl()).andReturn("url");
        expect(this.link.getAdditionalText()).andReturn("summary holdings, dates, publisher, description, instruction");
        expect(this.eresource.getRecordType()).andReturn("bib");
        expect(this.eresource.getRecordId()).andReturn(0);
        expect(this.eresource.getDescription()).andReturn("description");
        replay(this.eresource, this.link, this.version);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        assertEquals(
                this.xmlConsumer.getExpectedResult(this, "EeresourceXHTMLSAXStrategyTest-testToSAXGetPassword.xml"),
                this.xmlConsumer.getStringValue());
        verify(this.eresource, this.link, this.version);
    }
}
