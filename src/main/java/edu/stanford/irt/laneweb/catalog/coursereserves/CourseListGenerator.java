package edu.stanford.irt.laneweb.catalog.coursereserves;

import java.util.List;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.Course;
import edu.stanford.irt.coursereserves.CourseReservesDAO;

public class CourseListGenerator extends AbstractGenerator {

    private CourseReservesDAO dao;

    private SAXStrategy<List<Course>> saxStrategy;

    public CourseListGenerator(final CourseReservesDAO dao, final SAXStrategy<List<Course>> saxStrategy) {
        this.dao = dao;
        this.saxStrategy = saxStrategy;
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        this.saxStrategy.toSAX(this.dao.getCourses(), xmlConsumer);
    }
}
