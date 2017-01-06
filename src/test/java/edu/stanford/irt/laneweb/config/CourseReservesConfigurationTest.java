package edu.stanford.irt.laneweb.config;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

public class CourseReservesConfigurationTest {

    private CourseReservesConfiguration configuration;

    private DataSource dataSource;

    @Before
    public void setUp() {
        this.dataSource = createMock(DataSource.class);
        this.configuration = new CourseReservesConfiguration(this.dataSource);
    }

    @Test
    public void testCourseReservesDAO() throws IOException {
        assertNotNull(this.configuration.courseReservesDAO());
    }

    @Test
    public void testCourseReservesItemListGenerator() throws IOException {
        assertNotNull(this.configuration.courseReservesItemListGenerator());
    }

    @Test
    public void testCoursesGenerator() throws IOException {
        assertNotNull(this.configuration.coursesGenerator());
    }

    @Test
    public void testCoursesReservesItemListSAXStrategy() {
        assertNotNull(this.configuration.coursesReservesItemListSAXStrategy());
    }

    @Test
    public void testCoursesSAXStrategy() {
        assertNotNull(this.configuration.coursesSAXStrategy());
    }
}
