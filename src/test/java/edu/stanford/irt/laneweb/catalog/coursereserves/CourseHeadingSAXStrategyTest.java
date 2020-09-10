package edu.stanford.irt.laneweb.catalog.coursereserves;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
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

public class CourseHeadingSAXStrategyTest {

    private Course course;

    private CourseHeadingSAXStrategy saxStrategy;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.saxStrategy = new CourseHeadingSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
        this.course = mock(Course.class);
    }

    @Test
    public void testToSAX() throws SAXException, IOException {
        expect(this.course.getName()).andReturn("name");
        expect(this.course.getNumber()).andReturn("number");
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
        XMLConsumer mock = mock(XMLConsumer.class);
        replay(mock);
        this.saxStrategy.toSAX(null, mock);
        verify(mock);
    }

    @Test(expected = LanewebException.class)
    public void testToSAXThrowsException() throws SAXException {
        XMLConsumer mock = mock(XMLConsumer.class);
        mock.startElement(isA(String.class), isA(String.class), isA(String.class), isA(org.xml.sax.Attributes.class));
        expectLastCall().andThrow(new SAXException());
        replay(mock);
        this.saxStrategy.toSAX(this.course, mock);
        verify(mock);
    }
}
