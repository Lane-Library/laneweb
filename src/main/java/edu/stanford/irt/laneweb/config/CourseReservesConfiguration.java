package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

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
import edu.stanford.irt.laneweb.catalog.coursereserves.RESTCourseReservesService;
import edu.stanford.irt.laneweb.rest.BasicAuthRESTService;

@Configuration
public class CourseReservesConfiguration {

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/course-reserves-item-list") @Scope("prototype")
    public Generator courseReservesItemListGenerator(final CourseReservesService courseReservesService) {
        return new CourseReservesItemListGenerator(courseReservesService, coursesReservesItemListSAXStrategy());
    }

    @Bean
    public CourseReservesService courseReservesService(
            @Qualifier("java.net.URI/catalog-service") final URI catalogServiceURI,
            @Qualifier("restService/catalog-service") final BasicAuthRESTService restService) {
        return new RESTCourseReservesService(catalogServiceURI, restService);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/courses") @Scope("prototype")
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
}
