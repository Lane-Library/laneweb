package edu.stanford.irt.laneweb.coursereserves;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class CourseReservesInstructorResultSetSAXStrategyTest {

    private ResultSet resultSet;

    private CourseReservesInstructorResultSetSAXStrategy saxStrategy;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.saxStrategy = new CourseReservesInstructorResultSetSAXStrategy();
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.resultSet = createMock(ResultSet.class);
    }

    @Test
    public void testToSAX() throws SAXException, SQLException {
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "html", "html", XMLUtils.EMPTY_ATTRIBUTES);
        this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "body", "body", XMLUtils.EMPTY_ATTRIBUTES);
        expect(this.resultSet.next()).andReturn(true).times(3);
        expect(this.resultSet.getString(4)).andReturn("4").times(2);
        expect(this.resultSet.getString(5)).andReturn("5").times(2);
        this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "h3", "h3", XMLUtils.EMPTY_ATTRIBUTES);
        expectLastCall().times(2);
        this.xmlConsumer.characters(aryEq("4 5".toCharArray()), eq(0), eq(3));
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "h3", "h3");
        expectLastCall().times(2);
        this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "ul", "ul", XMLUtils.EMPTY_ATTRIBUTES);
        expectLastCall().times(2);
        this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "li", "li", XMLUtils.EMPTY_ATTRIBUTES);
        expectLastCall().times(3);
        expect(this.resultSet.getString(1)).andReturn("1").times(3);
        expect(this.resultSet.getString(2)).andReturn("2").times(3);
        this.xmlConsumer.startElement(isA(String.class), eq("a"), eq("a"), isA(Attributes.class));
        expectLastCall().times(3);
        this.xmlConsumer.characters(aryEq("2".toCharArray()), eq(0), eq(1));
        expectLastCall().times(3);
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "a", "a");
        expectLastCall().times(3);
        expect(this.resultSet.getString(3)).andReturn("3").times(3);
        this.xmlConsumer.characters(aryEq("4 5, 3".toCharArray()), eq(0), eq(6));
        expectLastCall().times(2);
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "li", "li");
        expectLastCall().times(3);
        expect(this.resultSet.getString(4)).andReturn("44");
        expect(this.resultSet.getString(5)).andReturn("55");
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "ul", "ul");
        expectLastCall().times(2);
        this.xmlConsumer.characters(aryEq("44 55".toCharArray()), eq(0), eq(5));
        this.xmlConsumer.characters(aryEq("44 55, 3".toCharArray()), eq(0), eq(8));
        expect(this.resultSet.next()).andReturn(false);
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "body", "body");
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "html", "html");
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.resultSet, this.xmlConsumer);
        this.saxStrategy.toSAX(this.resultSet, this.xmlConsumer);
        verify(this.resultSet, this.xmlConsumer);
    }

    @Test(expected = LanewebException.class)
    public void testToSAXSAXException() throws SAXException {
        this.xmlConsumer.startDocument();
        expectLastCall().andThrow(new SAXException());
        replay(this.resultSet, this.xmlConsumer);
        this.saxStrategy.toSAX(this.resultSet, this.xmlConsumer);
    }

    @Test(expected = LanewebException.class)
    public void testToSAXSQLException() throws SAXException, SQLException {
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "html", "html", XMLUtils.EMPTY_ATTRIBUTES);
        this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "body", "body", XMLUtils.EMPTY_ATTRIBUTES);
        expect(this.resultSet.next()).andThrow(new SQLException());
        replay(this.resultSet, this.xmlConsumer);
        this.saxStrategy.toSAX(this.resultSet, this.xmlConsumer);
    }
}
