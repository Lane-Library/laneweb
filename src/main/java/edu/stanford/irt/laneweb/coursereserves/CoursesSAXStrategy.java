package edu.stanford.irt.laneweb.coursereserves;

import java.util.List;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.Course;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;

public class CoursesSAXStrategy extends AbstractXHTMLSAXStrategy<List<Course>> {

    @Override
    public void toSAX(final List<Course> list, final XMLConsumer xmlConsumer) {
        try {
            xmlConsumer.startDocument();
            for (Course course : list) {
                startLi(xmlConsumer);
                createAnchor(xmlConsumer, "/samples/course-reserves.html?id=" + course.getId(), course.getName() + " (" + course.getNumber() + ')');
                endLi(xmlConsumer);
            }
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
