package edu.stanford.irt.laneweb.catalog.coursereserves;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.Course;
import edu.stanford.irt.coursereserves.CourseReservesService;
import edu.stanford.irt.coursereserves.JDBCCourseReservesService;

public class CourseListGeneratorTest {

    private CourseReservesService dao;

    private CourseListGenerator generator;

    private SAXStrategy<List<Course>> saxStrategy;

    private XMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.dao = mock(JDBCCourseReservesService.class);
        this.saxStrategy = mock(SAXStrategy.class);
        this.generator = new CourseListGenerator(this.dao, this.saxStrategy);
        this.xmlConsumer = mock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerateXMLConsumer() {
        expect(this.dao.getCourses()).andReturn(Collections.emptyList());
        this.saxStrategy.toSAX(Collections.emptyList(), this.xmlConsumer);
        replay(this.dao, this.saxStrategy, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.dao, this.saxStrategy, this.xmlConsumer);
    }
}
