package edu.stanford.irt.laneweb.catalog.coursereserves;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.Course;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class CourseHeadingSAXStrategy extends AbstractXHTMLSAXStrategy<Course> {

    @Override
    public void toSAX(final Course course, final XMLConsumer xmlConsumer) {
        if (course == null) {
            return;
        }
        try {
            startElementWithClass(xmlConsumer, "h4", "course-reserves-heading");
            XMLUtils.data(xmlConsumer, course.getName());
            XMLUtils.data(xmlConsumer, " (");
            XMLUtils.data(xmlConsumer, course.getNumber());
            XMLUtils.data(xmlConsumer, ")");
            startElementWithClass(xmlConsumer, "div", "course-reserves-instructor");
            XMLUtils.data(xmlConsumer, "Instructor: ");
            XMLUtils.data(xmlConsumer, course.getInstructor());
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "div");
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "h4");
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
