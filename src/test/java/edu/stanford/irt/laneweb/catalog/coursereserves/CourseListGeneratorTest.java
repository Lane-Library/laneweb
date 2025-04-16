package edu.stanford.irt.laneweb.catalog.coursereserves;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.Course;

public class CourseListGeneratorTest {

    private CourseListGenerator generator;

    private SAXStrategy<List<Course>> saxStrategy;

    private CourseReservesService service;

    private XMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() {
        this.service = mock(CourseReservesService.class);
        this.saxStrategy = mock(SAXStrategy.class);
        this.generator = new CourseListGenerator(this.service, this.saxStrategy);
        this.xmlConsumer = mock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerateXMLConsumer() {
        expect(this.service.getCourses()).andReturn(Collections.emptyList());
        this.saxStrategy.toSAX(Collections.emptyList(), this.xmlConsumer);
        replay(this.service, this.saxStrategy, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.saxStrategy, this.xmlConsumer);
    }
}
