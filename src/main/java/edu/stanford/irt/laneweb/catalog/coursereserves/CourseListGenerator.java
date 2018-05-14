package edu.stanford.irt.laneweb.catalog.coursereserves;

import java.util.List;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.Course;

public class CourseListGenerator extends AbstractGenerator {

    private SAXStrategy<List<Course>> saxStrategy;

    private CourseReservesService service;

    public CourseListGenerator(final CourseReservesService service, final SAXStrategy<List<Course>> saxStrategy) {
        this.service = service;
        this.saxStrategy = saxStrategy;
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        this.saxStrategy.toSAX(this.service.getCourses(), xmlConsumer);
    }
}
