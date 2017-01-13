package edu.stanford.irt.laneweb.config;

import java.io.IOException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.coursereserves.Course;
import edu.stanford.irt.coursereserves.CourseReservesDAO;
import edu.stanford.irt.coursereserves.CourseReservesItemList;
import edu.stanford.irt.laneweb.catalog.coursereserves.CourseHeadingSAXStrategy;
import edu.stanford.irt.laneweb.catalog.coursereserves.CourseListGenerator;
import edu.stanford.irt.laneweb.catalog.coursereserves.CourseReservesItemListGenerator;
import edu.stanford.irt.laneweb.catalog.coursereserves.CourseReservesItemListSAXStrategy;
import edu.stanford.irt.laneweb.catalog.coursereserves.CourseReservesItemSAXStrategy;
import edu.stanford.irt.laneweb.catalog.coursereserves.CoursesSAXStrategy;

@Configuration
public class CourseReservesConfiguration {

    private DataSource dataSource;

    @Autowired
    public CourseReservesConfiguration(@Qualifier("javax.sql.DataSource/catalog") final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public CourseReservesDAO courseReservesDAO() throws IOException {
        Class<?> c = CourseReservesDAO.class;
        return new CourseReservesDAO(this.dataSource, c.getResourceAsStream("itemStatus.sql"),
                c.getResourceAsStream("course.sql"), c.getResourceAsStream("courses.sql"),
                c.getResourceAsStream("courseReservesItemListAll.fnc"),
                c.getResourceAsStream("courseReservesItemListCourse.fnc"));
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/course-reserves-item-list")
    @Scope("prototype")
    public Generator courseReservesItemListGenerator() throws IOException {
        return new CourseReservesItemListGenerator(courseReservesDAO(), coursesReservesItemListSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/courses")
    @Scope("prototype")
    public Generator coursesGenerator() throws IOException {
        return new CourseListGenerator(courseReservesDAO(), coursesSAXStrategy());
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
