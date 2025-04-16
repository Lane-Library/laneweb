package edu.stanford.irt.laneweb.catalog.coursereserves;

import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.Course;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.TestXMLConsumer;

public class CourseHeadingSAXStrategyTest {

    private Course course;

    private CourseHeadingSAXStrategy saxStrategy;

    private TestXMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() {
        this.saxStrategy = new CourseHeadingSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
        this.course = new Course("id", "name", "number", "instructor", "department");
    }

    @Test
    public void testToSAX() throws SAXException, IOException {
        this.xmlConsumer.startDocument();
        this.saxStrategy.toSAX(this.course, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        assertEquals(this.xmlConsumer.getExpectedResult(this, "CourseHeadingSAXStrategyTest-toSAX.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test
    public void testToSAXNullCourse() throws SAXException, IOException {
        this.xmlConsumer.startDocument();
        this.saxStrategy.toSAX(null, this.xmlConsumer);
        this.xmlConsumer.endDocument();
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", this.xmlConsumer.getStringValue().trim());
    }

    @Test()
    public void testToSAXThrowsException() throws SAXException {
        assertThrows(LanewebException.class, () -> {
            XMLConsumer mock = mock(XMLConsumer.class);
            mock.startElement(isA(String.class), isA(String.class), isA(String.class),
                    isA(org.xml.sax.Attributes.class));
            expectLastCall().andThrow(new SAXException());
            replay(mock);
            this.saxStrategy.toSAX(this.course, mock);
            verify(mock);
        });
    }
}
