package edu.stanford.irt.laneweb.config;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class CourseReservesConfigurationTest {

    private CourseReservesConfiguration configuration;


    @Before
    public void setUp() {
        this.configuration = new CourseReservesConfiguration(null, null);
    }

    @Test
    public void testCourseReservesDAO() throws IOException {
        assertNotNull(this.configuration.courseReservesService());
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
