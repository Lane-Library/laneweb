package edu.stanford.irt.laneweb.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CourseReservesConfigurationTest {

    private CourseReservesConfiguration configuration;

    @BeforeEach
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
    public void testRESTCourseReservesService() {
        assertNotNull(this.configuration.courseReservesService(null, null));
    }
}
