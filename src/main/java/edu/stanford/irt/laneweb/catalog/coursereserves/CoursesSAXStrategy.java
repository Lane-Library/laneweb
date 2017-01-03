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
                startLi(xmlConsumer);
                startAnchor(xmlConsumer, "?id=" + course.getId());
                XMLUtils.data(xmlConsumer, course.getName());
                XMLUtils.startElement(xmlConsumer, XHTML_NS, "br");
                XMLUtils.endElement(xmlConsumer, XHTML_NS, "br");
                XMLUtils.data(xmlConsumer, '(' + course.getNumber() + ')');
                endAnchor(xmlConsumer);
                endLi(xmlConsumer);
            }
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
