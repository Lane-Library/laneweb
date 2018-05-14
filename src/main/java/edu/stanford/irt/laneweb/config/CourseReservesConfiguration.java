package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.coursereserves.Course;
import edu.stanford.irt.coursereserves.CourseReservesItemList;
import edu.stanford.irt.laneweb.catalog.coursereserves.CourseHeadingSAXStrategy;
import edu.stanford.irt.laneweb.catalog.coursereserves.CourseListGenerator;
import edu.stanford.irt.laneweb.catalog.coursereserves.CourseReservesItemListGenerator;
import edu.stanford.irt.laneweb.catalog.coursereserves.CourseReservesItemListSAXStrategy;
import edu.stanford.irt.laneweb.catalog.coursereserves.CourseReservesItemSAXStrategy;
import edu.stanford.irt.laneweb.catalog.coursereserves.CourseReservesService;
import edu.stanford.irt.laneweb.catalog.coursereserves.CoursesSAXStrategy;
import edu.stanford.irt.laneweb.catalog.coursereserves.HTTPCourseReservesService;
import edu.stanford.irt.laneweb.catalog.coursereserves.RESTCourseReservesService;
import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.util.ServiceURIResolver;

@Configuration
public class CourseReservesConfiguration {

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/course-reserves-item-list")
    @Scope("prototype")
    public Generator courseReservesItemListGenerator(
            @Qualifier("edu.stanford.irt.laneweb.catalog.coursereserves.CourseReservesService/HTTP")
            final CourseReservesService courseReservesService) {
        return new CourseReservesItemListGenerator(courseReservesService, coursesReservesItemListSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.laneweb.catalog.coursereserves.CourseReservesService/HTTP")
    public CourseReservesService courseReservesService(final ObjectMapper objectMapper,
            @Qualifier("java.net.URI/catalog-service") final URI catalogServiceURI,
            final ServiceURIResolver uriResolver) {
        return new HTTPCourseReservesService(objectMapper, catalogServiceURI, uriResolver);
    }

    @Bean(name = "edu.stanford.irt.laneweb.catalog.coursereserves.CourseReservesService/REST")
    public CourseReservesService courseReservesService(
            @Qualifier("java.net.URI/catalog-service") final URI catalogServiceURI,
            final RESTService restService) {
        return new RESTCourseReservesService(catalogServiceURI, restService);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/courses")
    @Scope("prototype")
    public Generator coursesGenerator(
            @Qualifier("edu.stanford.irt.laneweb.catalog.coursereserves.CourseReservesService/HTTP")
            final CourseReservesService courseReservesService) {
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
}
