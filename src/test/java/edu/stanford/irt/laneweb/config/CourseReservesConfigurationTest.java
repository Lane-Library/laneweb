package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class CourseReservesConfigurationTest {

    private CourseReservesConfiguration configuration;

    @Before
    public void setUp() {
        this.configuration = new CourseReservesConfiguration();
    }

    @Test
    public void testCourseReservesItemListGenerator() throws IOException {
        assertNotNull(this.configuration.courseReservesItemListGenerator(null));
    }

    @Test
    public void testCoursesGenerator() throws IOException {
        assertNotNull(this.configuration.coursesGenerator(null));
    }

    @Test
    public void testCoursesReservesItemListSAXStrategy() {
        assertNotNull(this.configuration.coursesReservesItemListSAXStrategy());
    }

    @Test
    public void testCoursesSAXStrategy() {
        assertNotNull(this.configuration.coursesSAXStrategy());
    }

    @Test
    public void testHTTPCourseReservesService() {
        assertNotNull(this.configuration.courseReservesService(null, null, null));
    }

    @Test
    public void testRESTCourseReservesService() {
        assertNotNull(this.configuration.courseReservesService(null, null));
    }
}
