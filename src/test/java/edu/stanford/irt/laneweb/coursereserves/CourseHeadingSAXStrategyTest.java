package edu.stanford.irt.laneweb.coursereserves;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.Course;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class CourseHeadingSAXStrategyTest {

    private Course course;

    private CourseHeadingSAXStrategy saxStrategy;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.saxStrategy = new CourseHeadingSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
        this.course = createMock(Course.class);
    }

    @Test
    public void testToSAX() throws SAXException, IOException {
        expect(this.course.getName()).andReturn("name");
        expect(this.course.getInstructor()).andReturn("instructor");
        replay(this.course);
        this.xmlConsumer.startDocument();
        this.saxStrategy.toSAX(this.course, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        verify(this.course);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "CourseHeadingSAXStrategyTest-toSAX.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test
    public void testToSAXNullCourse() {
        XMLConsumer mock = createMock(XMLConsumer.class);
        replay(mock);
        this.saxStrategy.toSAX(null, mock);
        verify(mock);
    }

    @Test(expected = LanewebException.class)
    public void testToSAXThrowsException() throws SAXException {
        XMLConsumer mock = createMock(XMLConsumer.class);
        mock.startElement("http://www.w3.org/1999/xhtml", "h3", "h3", XMLUtils.EMPTY_ATTRIBUTES);
        expectLastCall().andThrow(new SAXException());
        replay(mock);
        this.saxStrategy.toSAX(this.course, mock);
        verify(mock);
    }
}
