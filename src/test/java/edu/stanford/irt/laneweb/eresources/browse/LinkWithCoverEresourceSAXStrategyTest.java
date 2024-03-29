package edu.stanford.irt.laneweb.eresources.browse;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
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
import edu.stanford.irt.laneweb.eresources.model.Eresource;
import edu.stanford.irt.laneweb.eresources.model.Link;

public class LinkWithCoverEresourceSAXStrategyTest {

    private Eresource eresource;

    private Link link;

    private LinkWithCoverEresourceSAXStrategy saxStrategy;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.saxStrategy = new LinkWithCoverEresourceSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
        this.eresource = mock(Eresource.class);
        this.link = mock(Link.class);
    }

    @Test(expected = LanewebException.class)
    public void testNoLink() throws IOException, SAXException {
        expect(this.eresource.getRecordId()).andReturn("12");
        expect(this.eresource.getLinks()).andReturn(Collections.emptySet());
        replay(this.eresource, this.link);
        this.saxStrategy.toSAX(this.eresource, this.xmlConsumer);
    }

    @Test
    public void testToSAXBook() throws IOException, SAXException {
        expect(this.eresource.getRecordId()).andReturn("12");
        expect(this.eresource.getLinks()).andReturn(Collections.singleton(this.link));
        expect(this.link.getUrl()).andReturn("url");
        expect(this.eresource.getPrimaryType()).andReturn("Book");
        expect(this.eresource.getTitle()).andReturn("Lancet");
        replay(this.eresource, this.link);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startElement("", "div", "div", new AttributesImpl());
        this.saxStrategy.toSAX(this.eresource, this.xmlConsumer);
        this.xmlConsumer.endElement("", "div", "div");
        this.xmlConsumer.endDocument();
        verify(this.eresource, this.link);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "LinkWithCoverEresourceSAXStrategyTest-toSAXBook.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test
    public void testToSAXJournal() throws IOException, SAXException {
        expect(this.eresource.getRecordId()).andReturn("12");
        expect(this.eresource.getLinks()).andReturn(Collections.singleton(this.link));
        expect(this.link.getUrl()).andReturn("url");
        expect(this.eresource.getPrimaryType()).andReturn("Journal");
        expect(this.eresource.getTitle()).andReturn("Lancet");
        replay(this.eresource, this.link);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startElement("", "div", "div", new AttributesImpl());
        this.saxStrategy.toSAX(this.eresource, this.xmlConsumer);
        this.xmlConsumer.endElement("", "div", "div");
        this.xmlConsumer.endDocument();
        verify(this.eresource, this.link);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "LinkWithCoverEresourceSAXStrategyTest-toSAXJournal.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test(expected = LanewebException.class)
    public void testToSAXThrowsException() throws IOException, SAXException {
        XMLConsumer x = mock(XMLConsumer.class);
        expect(this.eresource.getRecordId()).andReturn("12");
        expect(this.eresource.getLinks()).andReturn(Collections.singleton(this.link));
        expect(this.link.getUrl()).andReturn("url");
        expect(this.eresource.getPrimaryType()).andReturn("");
        x.startElement(same("http://www.w3.org/1999/xhtml"), same("a"), same("a"), isA(AttributesImpl.class));
        expectLastCall().andThrow(new SAXException());
        replay(this.eresource, this.link, x);
        this.saxStrategy.toSAX(this.eresource, x);
    }
}
