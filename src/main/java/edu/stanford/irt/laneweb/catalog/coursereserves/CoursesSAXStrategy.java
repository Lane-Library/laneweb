package edu.stanford.irt.laneweb.catalog.coursereserves;

import java.util.List;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.Course;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class CoursesSAXStrategy extends AbstractXHTMLSAXStrategy<List<Course>> {

    @Override
    public void toSAX(final List<Course> list, final XMLConsumer xmlConsumer) {
        try {
            xmlConsumer.startDocument();
            for (Course course : list) {
                XMLUtils.startElement(xmlConsumer, XHTML_NS, "tr");
                XMLUtils.startElement(xmlConsumer, XHTML_NS, "td");
                startAnchorWithClass(xmlConsumer, "course-reserves-list.html?id=" + course.getId(), "course-name");
                XMLUtils.data(xmlConsumer, course.getName());
                endAnchor(xmlConsumer);
                startElementWithClass(xmlConsumer, "span", "course-number");
                String courseInfo = new StringBuilder(" (").append(course.getNumber()).append(")").toString();
                XMLUtils.data(xmlConsumer, courseInfo);
                XMLUtils.endElement(xmlConsumer, XHTML_NS, "span");
                XMLUtils.endElement(xmlConsumer, XHTML_NS, "td");
                createElement(xmlConsumer, "td", course.getInstructor());
                createElement(xmlConsumer, "td", course.getDepartment());
                XMLUtils.endElement(xmlConsumer, XHTML_NS, "tr");
            }
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
