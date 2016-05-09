package edu.stanford.irt.laneweb.flickr;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class FlickrPhotoSAXStrategyTest {

    private FlickrPhotoSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.strategy = new FlickrPhotoSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
    }

    @Test
    public void testToSAX() throws IOException {
        this.strategy.toSAX(Collections.singletonList(new FlickrPhoto("page", "thumbnail")), this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "FlickrPhotoSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test(expected = LanewebException.class)
    public void testToSAXPhotoThrows() throws SAXException {
        XMLConsumer x = createMock(XMLConsumer.class);
        x.startDocument();
        x.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        x.startElement("http://www.w3.org/1999/xhtml", "html", "html", XMLUtils.EMPTY_ATTRIBUTES);
        x.startElement("http://www.w3.org/1999/xhtml", "body", "body", XMLUtils.EMPTY_ATTRIBUTES);
        x.startElement(eq("http://www.w3.org/1999/xhtml"), eq("a"), eq("a"), isA(Attributes.class));
        expectLastCall().andThrow(new SAXException());
        replay(x);
        this.strategy.toSAX(Collections.singletonList(new FlickrPhoto("page", "thumbnail")), x);
    }

    @Test(expected = LanewebException.class)
    public void testToSAXThrows() throws SAXException {
        XMLConsumer x = createMock(XMLConsumer.class);
        x.startDocument();
        expectLastCall().andThrow(new SAXException());
        replay(x);
        this.strategy.toSAX(null, x);
    }
}
