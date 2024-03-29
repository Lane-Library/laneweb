package edu.stanford.irt.laneweb.history;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
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

public class HistoryPhotoSAXStrategyTest {

    private HistoryPhotoSAXStrategy strategy;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.strategy = new HistoryPhotoSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
    }

    @Test
    public void testToSAX() throws IOException {
        this.strategy.toSAX(Collections.singletonList(new HistoryPhoto("page", "thumbnail", "title")), this.xmlConsumer);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "HistoryPhotoSAXStrategyTest-testToSAX.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test(expected = LanewebException.class)
    public void testToSAXPhotoThrows() throws SAXException {
        XMLConsumer x = mock(XMLConsumer.class);
        x.startDocument();
        x.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        x.startElement("http://www.w3.org/1999/xhtml", "html", "html", XMLUtils.EMPTY_ATTRIBUTES);
        x.startElement("http://www.w3.org/1999/xhtml", "body", "body", XMLUtils.EMPTY_ATTRIBUTES);
        x.startElement(eq("http://www.w3.org/1999/xhtml"), eq("a"), eq("a"), isA(Attributes.class));
        expectLastCall().andThrow(new SAXException());
        replay(x);
        this.strategy.toSAX(Collections.singletonList(new HistoryPhoto("page", "thumbnail", "title")), x);
    }

    @Test(expected = LanewebException.class)
    public void testToSAXThrows() throws SAXException {
        XMLConsumer x = mock(XMLConsumer.class);
        x.startDocument();
        expectLastCall().andThrow(new SAXException());
        replay(x);
        this.strategy.toSAX(null, x);
    }
}
