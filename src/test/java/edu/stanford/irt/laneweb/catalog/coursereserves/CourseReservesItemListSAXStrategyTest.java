package edu.stanford.irt.laneweb.catalog.coursereserves;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.Course;
import edu.stanford.irt.coursereserves.CourseReservesItem;
import edu.stanford.irt.coursereserves.CourseReservesItemList;
import edu.stanford.irt.laneweb.TestXMLConsumer;

public class CourseReservesItemListSAXStrategyTest {

    public SAXStrategy<CourseReservesItem> itemStrategy;

    private Course course;

    private SAXStrategy<Course> courseStrategy;

    private CourseReservesItem item;

    private CourseReservesItemList itemList;

    private CourseReservesItemListSAXStrategy saxStrategy;

    private TestXMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() {
        this.courseStrategy = mock(SAXStrategy.class);
        this.itemStrategy = mock(SAXStrategy.class);
        this.saxStrategy = new CourseReservesItemListSAXStrategy(this.courseStrategy, this.itemStrategy);
        this.itemList = mock(CourseReservesItemList.class);
        this.xmlConsumer = new TestXMLConsumer();
        this.course = mock(Course.class);
        this.item = mock(CourseReservesItem.class);
    }

    @Test
    public void testToSAX() throws SAXException, IOException {
        expect(this.itemList.getCourse()).andReturn(this.course);
        this.courseStrategy.toSAX(this.course, this.xmlConsumer);
        expect(this.itemList.getItems()).andReturn(Collections.singletonList(this.item));
        this.itemStrategy.toSAX(this.item, this.xmlConsumer);
        replay(this.itemStrategy, this.courseStrategy, this.itemList, this.course, this.item);
        this.saxStrategy.toSAX(this.itemList, this.xmlConsumer);
        verify(this.itemStrategy, this.courseStrategy, this.itemList, this.course, this.item);
        assertEquals(this.xmlConsumer.getExpectedResult(this, "CourseReservesItemListSAXStrategyTest-toSAX.xml"),
                this.xmlConsumer.getStringValue());
    }

    @Test
    public void testToSAXThrowsException() throws SAXException, IOException {
        assertThrows(IllegalStateException.class, () -> {
            XMLConsumer mock = mock(XMLConsumer.class);
            mock.startDocument();
            expectLastCall().andThrow(new SAXException());
            this.saxStrategy.toSAX(this.itemList, mock);
            replay(this.itemList, mock);
        });

    }
}
