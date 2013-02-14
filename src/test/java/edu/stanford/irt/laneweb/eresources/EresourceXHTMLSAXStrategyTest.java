package edu.stanford.irt.laneweb.eresources;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.Link;
import edu.stanford.irt.eresources.Version;
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
    public void testToSAX() throws SAXException {
        expect(this.eresource.getVersions()).andReturn(Collections.singletonList(this.version));
        expect(this.eresource.getTitle()).andReturn("title");
        expect(this.version.getLinks()).andReturn(Collections.singletonList(this.link));
        expect(this.link.getLabel()).andReturn("label");
        expect(this.link.getUrl()).andReturn("url");
        expect(this.version.getSummaryHoldings()).andReturn("summary holdings");
        expect(this.version.getDates()).andReturn("dates");
        expect(this.version.getPublisher()).andReturn("publisher");
        expect(this.version.getDescription()).andReturn("description");
        expect(this.link.getInstruction()).andReturn("instruction");
        expect(this.eresource.getRecordType()).andReturn("bib");
        expect(this.eresource.getRecordId()).andReturn(0);
        expect(this.eresource.getDescription()).andReturn("description");
        replay(this.eresource, this.link, this.version);
        this.xmlConsumer.startDocument();
        XMLUtils.startElement(this.xmlConsumer, "", "test");
        this.strategy.toSAX(this.eresource, this.xmlConsumer);
        XMLUtils.endElement(this.xmlConsumer, "", "test");
        this.xmlConsumer.endDocument();
        System.out.println(this.xmlConsumer.getStringValue());
        verify(this.eresource, this.link, this.version);
    }
}
