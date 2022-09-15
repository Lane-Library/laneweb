package edu.stanford.irt.laneweb.catalog.coursereserves;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.Course;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;

public class CoursesSAXStrategyTest {

    private Course course = mock(Course.class);

    private CoursesSAXStrategy saxStrategy;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.saxStrategy = new CoursesSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
    }

    @Test
    public void testToSAX() throws IOException {
        expect(this.course.getId()).andReturn("1");
        expect(this.course.getName()).andReturn("name");
        expect(this.course.getInstructor()).andReturn("instructor");
        expect(this.course.getNumber()).andReturn("number");
        expect(this.course.getDepartment()).andReturn("department");
        replay(this.course);
        this.saxStrategy.toSAX(Collections.singletonList(this.course), this.xmlConsumer);
        verify(this.course);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "CoursesSAXStrategyTest-toSAX.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test(expected = LanewebException.class)
    public void testToSAXThrowsException() throws SAXException {
        XMLConsumer mock = mock(XMLConsumer.class);
        mock.startDocument();
        expectLastCall().andThrow(new SAXException());
        replay(mock);
        this.saxStrategy.toSAX(Collections.singletonList(this.course), mock);
    }
}
