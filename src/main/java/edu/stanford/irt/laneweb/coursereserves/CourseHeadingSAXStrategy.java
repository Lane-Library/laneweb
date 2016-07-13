package edu.stanford.irt.laneweb.coursereserves;

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
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "h3");
            XMLUtils.data(xmlConsumer, course.getName());
            startElementWithClass(xmlConsumer, "span", "course-reserves-instructor");
            XMLUtils.data(xmlConsumer, "instructor: ");
            createElement(xmlConsumer, "strong", course.getInstructor());
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "span");
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "h3");
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
