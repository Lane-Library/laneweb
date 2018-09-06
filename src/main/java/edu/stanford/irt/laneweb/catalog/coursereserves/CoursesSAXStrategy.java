package edu.stanford.irt.laneweb.catalog.coursereserves;

import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

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
                startLi(xmlConsumer);
                startAnchor(xmlConsumer, "?id=" + course.getId());
                XMLUtils.data(xmlConsumer, course.getName());
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "style", "style", "CDATA", "font-size:smaller");
                XMLUtils.startElement(xmlConsumer, XHTML_NS, "span", atts);
                String courseInfo = new StringBuilder(" -\u00a0")
                        .append(course.getInstructor())
                        .append(" (")
                        .append(course.getNumber())
                        .append(')')
                        .toString();
                XMLUtils.data(xmlConsumer, courseInfo);
                XMLUtils.endElement(xmlConsumer, XHTML_NS, "span");
                endAnchor(xmlConsumer);
                endLi(xmlConsumer);
            }
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
