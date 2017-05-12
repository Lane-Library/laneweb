package edu.stanford.irt.laneweb.eresources.browse;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.same;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.Link;

public class LinkWithCoverEresourceSAXStrategyTest {

    private Eresource eresource;

    private Link link;

    private LinkWithCoverEresourceSAXStrategy saxStrategy;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.saxStrategy = new LinkWithCoverEresourceSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
        this.eresource = createMock(Eresource.class);
        this.link = createMock(Link.class);
    }

    @Test
    public void testToSAX() throws IOException, SAXException {
        expect(this.eresource.getLinks()).andReturn(Collections.singleton(this.link));
        expect(this.link.getUrl()).andReturn("url");
        expect(this.eresource.getId()).andReturn("bib-12");
        expect(this.eresource.getTitle()).andReturn("Lancet");
        replay(this.eresource, this.link);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startElement("", "div", "div", new AttributesImpl());
        this.saxStrategy.toSAX(this.eresource, this.xmlConsumer);
        this.xmlConsumer.endElement("", "div", "div");
        this.xmlConsumer.endDocument();
        verify(this.eresource, this.link);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "LinkWithCoverEresourceSAXStrategyTest-toSAX.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test(expected = LanewebException.class)
    public void testToSAXThrowsException() throws IOException, SAXException {
        XMLConsumer x = createMock(XMLConsumer.class);
        expect(this.eresource.getLinks()).andReturn(Collections.singleton(this.link));
        expect(this.link.getUrl()).andReturn("url");
        x.startElement(same("http://www.w3.org/1999/xhtml"), same("a"), same("a"), isA(AttributesImpl.class));
        expectLastCall().andThrow(new SAXException());
        replay(this.eresource, this.link, x);
        this.saxStrategy.toSAX(this.eresource, x);
    }
}
