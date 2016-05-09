package edu.stanford.irt.laneweb.coursereserves;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
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
import edu.stanford.irt.laneweb.util.XMLUtils;

public class CourseReservesResultSetSAXStrategyTest {

    private ResultSet resultSet;

    private CourseReservesResultSetSAXStrategy saxStrategy;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.saxStrategy = new CourseReservesResultSetSAXStrategy();
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.resultSet = createMock(ResultSet.class);
    }

    @Test
    public void testToSAX() throws SAXException, SQLException {
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "html", "html", XMLUtils.EMPTY_ATTRIBUTES);
        this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "body", "body", XMLUtils.EMPTY_ATTRIBUTES);
        this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "ul", "ul", XMLUtils.EMPTY_ATTRIBUTES);
        expect(this.resultSet.next()).andReturn(true);
        this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "li", "li", XMLUtils.EMPTY_ATTRIBUTES);
        expect(this.resultSet.getString(1)).andReturn("1");
        expect(this.resultSet.getString(2)).andReturn("2");
        this.xmlConsumer.startElement(isA(String.class), eq("a"), eq("a"), isA(Attributes.class));
        this.xmlConsumer.characters(aryEq("2".toCharArray()), eq(0), eq(1));
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "a", "a");
        expect(this.resultSet.getString(4)).andReturn("4");
        expect(this.resultSet.getString(5)).andReturn("5");
        expect(this.resultSet.getString(3)).andReturn("3");
        this.xmlConsumer.characters(aryEq("4 5, 3".toCharArray()), eq(0), eq(6));
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "li", "li");
        expect(this.resultSet.next()).andReturn(false);
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "ul", "ul");
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "body", "body");
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "html", "html");
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
        replay(this.resultSet, this.xmlConsumer);
        this.saxStrategy.toSAX(this.resultSet, this.xmlConsumer);
        verify(this.resultSet, this.xmlConsumer);
    }
}
