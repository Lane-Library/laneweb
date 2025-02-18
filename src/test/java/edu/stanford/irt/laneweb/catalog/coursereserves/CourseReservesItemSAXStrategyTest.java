package edu.stanford.irt.laneweb.catalog.coursereserves;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.CourseReservesItem;
import edu.stanford.irt.coursereserves.ItemType;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;

public class CourseReservesItemSAXStrategyTest {

    private CourseReservesItem item;

    private CourseReservesItemSAXStrategy saxStrategy;

    private TestXMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() {
        this.saxStrategy = new CourseReservesItemSAXStrategy();
        this.item = mock(CourseReservesItem.class);
        this.xmlConsumer = new TestXMLConsumer();
    }

    @Test
    public void testToSAX() throws SAXException, IOException {
        expect(this.item.getId()).andReturn("1");
        expect(this.item.getType()).andReturn(ItemType.PRINT_BOOK);
        expect(this.item.getURL()).andReturn(null);
        expect(this.item.getTitle()).andReturn("title");
        expect(this.item.getAuthor()).andReturn("author");
        expect(this.item.getAvailableCount()).andReturn(Integer.valueOf(1));
        expect(this.item.getCallNumber()).andReturn("call number");
        expect(this.item.getVersionNote()).andReturn("version note");
        replay(this.item);
        this.xmlConsumer.startDocument();
        this.saxStrategy.toSAX(this.item, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        verify(this.item);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "CourseReservesItemSAXStrategyTest-toSAX.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test
    public void testToSAX0AvailableCount() throws SAXException, IOException {
        expect(this.item.getId()).andReturn("1");
        expect(this.item.getType()).andReturn(ItemType.PRINT_BOOK);
        expect(this.item.getURL()).andReturn(null);
        expect(this.item.getTitle()).andReturn("title");
        expect(this.item.getAuthor()).andReturn("author");
        expect(this.item.getAvailableCount()).andReturn(Integer.valueOf(0));
        expect(this.item.getCallNumber()).andReturn("call number");
        expect(this.item.getVersionNote()).andReturn(null);
        replay(this.item);
        this.xmlConsumer.startDocument();
        this.saxStrategy.toSAX(this.item, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        verify(this.item);
        assertEquals(
                this.xmlConsumer.getExpectedResult(this, "CourseReservesItemSAXStrategyTest-toSAX0AvailableCount.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test
    public void testToSAXNullAuthor() throws SAXException, IOException {
        expect(this.item.getId()).andReturn("1");
        expect(this.item.getType()).andReturn(ItemType.DIGITAL_BOOK);
        expect(this.item.getURL()).andReturn("url");
        expect(this.item.getTitle()).andReturn("title");
        expect(this.item.getAuthor()).andReturn(null);
        expect(this.item.getVersionNote()).andReturn(null);
        replay(this.item);
        this.xmlConsumer.startDocument();
        this.saxStrategy.toSAX(this.item, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        verify(this.item);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "CourseReservesItemSAXStrategyTest-toSAXNullAuthor.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test
    public void testToSAXNullAvailableCount() throws SAXException, IOException {
        expect(this.item.getId()).andReturn("1");
        expect(this.item.getType()).andReturn(ItemType.PRINT_BOOK);
        expect(this.item.getURL()).andReturn(null);
        expect(this.item.getTitle()).andReturn("title");
        expect(this.item.getAuthor()).andReturn("author");
        expect(this.item.getAvailableCount()).andReturn(null);
        expect(this.item.getCallNumber()).andReturn("call number");
        expect(this.item.getVersionNote()).andReturn(null);
        replay(this.item);
        this.xmlConsumer.startDocument();
        this.saxStrategy.toSAX(this.item, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        verify(this.item);
        assertEquals(
                this.xmlConsumer.getExpectedResult(this,
                        "CourseReservesItemSAXStrategyTest-toSAXNullAvailableCount.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test
    public void testToSAXNullCallNumber() throws SAXException, IOException {
        expect(this.item.getId()).andReturn("1");
        expect(this.item.getType()).andReturn(ItemType.PRINT_BOOK);
        expect(this.item.getURL()).andReturn(null);
        expect(this.item.getTitle()).andReturn("title");
        expect(this.item.getAuthor()).andReturn("author");
        expect(this.item.getAvailableCount()).andReturn(Integer.valueOf(1));
        expect(this.item.getCallNumber()).andReturn(null);
        expect(this.item.getVersionNote()).andReturn(null);
        replay(this.item);
        this.xmlConsumer.startDocument();
        this.saxStrategy.toSAX(this.item, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        verify(this.item);
        assertEquals(
                this.xmlConsumer.getExpectedResult(this, "CourseReservesItemSAXStrategyTest-toSAXNullCallNumber.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test
    public void testToSAXNullURL() throws SAXException, IOException {
        expect(this.item.getId()).andReturn("1");
        expect(this.item.getType()).andReturn(ItemType.DIGITAL_BOOK);
        expect(this.item.getURL()).andReturn(null);
        expect(this.item.getTitle()).andReturn("title");
        expect(this.item.getAuthor()).andReturn("author");
        expect(this.item.getVersionNote()).andReturn(null);
        replay(this.item);
        this.xmlConsumer.startDocument();
        this.saxStrategy.toSAX(this.item, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        verify(this.item);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "CourseReservesItemSAXStrategyTest-toSAXNullURL.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test()
    public void testToSAXThrowsException() throws SAXException {
        XMLConsumer mock = mock(XMLConsumer.class);
        mock.startElement(eq("http://www.w3.org/1999/xhtml"), eq("li"), eq("li"), isA(Attributes.class));
        expectLastCall().andThrow(new SAXException());
        replay(mock, this.item);
        assertThrows(LanewebException.class, () -> {
            this.saxStrategy.toSAX(this.item, mock);
        });
    }

    @Test
    public void testToSAXVideo() throws SAXException, IOException {
        expect(this.item.getId()).andReturn("1");
        expect(this.item.getType()).andReturn(ItemType.VIDEO);
        expect(this.item.getURL()).andReturn(null);
        expect(this.item.getTitle()).andReturn("title");
        expect(this.item.getAuthor()).andReturn("author");
        expect(this.item.getVersionNote()).andReturn(null);
        replay(this.item);
        this.xmlConsumer.startDocument();
        this.saxStrategy.toSAX(this.item, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        verify(this.item);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "CourseReservesItemSAXStrategyTest-toSAXVideo.xml"),
                this.xmlConsumer.getStringValue());
    }
}
