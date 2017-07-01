package edu.stanford.irt.laneweb.config;

import static edu.stanford.irt.laneweb.util.IOUtils.getResourceAsString;

import java.net.URI;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.coursereserves.Course;
import edu.stanford.irt.coursereserves.CourseReservesItemList;
import edu.stanford.irt.coursereserves.CourseReservesService;
import edu.stanford.irt.coursereserves.JDBCCourseReservesService;
import edu.stanford.irt.laneweb.catalog.coursereserves.CourseHeadingSAXStrategy;
import edu.stanford.irt.laneweb.catalog.coursereserves.CourseListGenerator;
import edu.stanford.irt.laneweb.catalog.coursereserves.CourseReservesItemListGenerator;
import edu.stanford.irt.laneweb.catalog.coursereserves.CourseReservesItemListSAXStrategy;
import edu.stanford.irt.laneweb.catalog.coursereserves.CourseReservesItemSAXStrategy;
import edu.stanford.irt.laneweb.catalog.coursereserves.CoursesSAXStrategy;
import edu.stanford.irt.laneweb.catalog.coursereserves.HTTPCourseReservesService;

@Configuration
public class CourseReservesConfiguration {

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/course-reserves-item-list")
    @Scope("prototype")
    public Generator courseReservesItemListGenerator(final CourseReservesService courseReservesService) {
        return new CourseReservesItemListGenerator(courseReservesService, coursesReservesItemListSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/courses")
    @Scope("prototype")
    public Generator coursesGenerator(final CourseReservesService courseReservesService) {
        return new CourseListGenerator(courseReservesService, coursesSAXStrategy());
    }

    @Bean
    public SAXStrategy<CourseReservesItemList> coursesReservesItemListSAXStrategy() {
        return new CourseReservesItemListSAXStrategy(new CourseHeadingSAXStrategy(),
                new CourseReservesItemSAXStrategy());
    }

    @Bean
    public SAXStrategy<List<Course>> coursesSAXStrategy() {
        return new CoursesSAXStrategy();
    }

    @Bean
    @Profile("gce")
    public CourseReservesService httpCourseReservesService(final ObjectMapper objectMapper,
            @Qualifier("java.net.URI/catalog-service") final URI catalogServiceURI) {
        return new HTTPCourseReservesService(objectMapper, catalogServiceURI);
    }

    @Bean
    @Profile("!gce")
    public CourseReservesService jdbcCourseReservesService(
            @Qualifier("javax.sql.DataSource/catalog") final DataSource dataSource) {
        return new JDBCCourseReservesService(dataSource,
                getResourceAsString(JDBCCourseReservesService.class, "itemStatus.sql"),
                getResourceAsString(JDBCCourseReservesService.class, "course.sql"),
                getResourceAsString(JDBCCourseReservesService.class, "courses.sql"),
                getResourceAsString(JDBCCourseReservesService.class, "courseReservesItemListAll.fnc"),
                getResourceAsString(JDBCCourseReservesService.class, "courseReservesItemListCourse.fnc"));
    }
}
