package edu.stanford.irt.laneweb.coursereserves;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.CourseReservesItem;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class CourseReservesItemSAXStrategyTest {

    private CourseReservesItem item;

    private CourseReservesItemSAXStrategy saxStrategy;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.saxStrategy = new CourseReservesItemSAXStrategy();
        this.item = createMock(CourseReservesItem.class);
        this.xmlConsumer = new TestXMLConsumer();
    }

    @Test
    public void testToSAX() throws SAXException, IOException {
        expect(this.item.getId()).andReturn(1);
        expect(this.item.getTitle()).andReturn("title");
        expect(this.item.getAuthor()).andReturn("author");
        expect(this.item.getAvailableCount()).andReturn(Integer.valueOf(1));
        expect(this.item.getCallNumber()).andReturn("call number");
        replay(this.item);
        this.xmlConsumer.startDocument();
        this.saxStrategy.toSAX(this.item, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        verify(this.item);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "CourseReservesItemSAXStrategyTest-toSAX.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test
    public void testToSAXNullAuthor() throws SAXException, IOException {
        expect(this.item.getId()).andReturn(1);
        expect(this.item.getTitle()).andReturn("title");
        expect(this.item.getAuthor()).andReturn(null);
        expect(this.item.getAvailableCount()).andReturn(Integer.valueOf(2));
        expect(this.item.getCallNumber()).andReturn("call number");
        replay(this.item);
        this.xmlConsumer.startDocument();
        this.saxStrategy.toSAX(this.item, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        verify(this.item);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "CourseReservesItemSAXStrategyTest-toSAXNullAuthor.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test(expected = LanewebException.class)
    public void testToSAXThrowsException() throws SAXException {
        XMLConsumer mock = createMock(XMLConsumer.class);
        mock.startElement("http://www.w3.org/1999/xhtml", "li", "li", XMLUtils.EMPTY_ATTRIBUTES);
        expectLastCall().andThrow(new SAXException());
        replay(mock, this.item);
        this.saxStrategy.toSAX(this.item, mock);
    }
}
