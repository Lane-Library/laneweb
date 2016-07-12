package edu.stanford.irt.laneweb.coursereserves;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.CourseReservesItem;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class CourseReservesItemSAXStrategy extends AbstractXHTMLSAXStrategy<CourseReservesItem> {

    @Override
    public void toSAX(final CourseReservesItem item, final XMLConsumer xmlConsumer) {
        try {
            startLi(xmlConsumer);
            String id = Integer.toString(item.getId());
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", "class", "class", "CDATA", "bookcover");
            atts.addAttribute("", "data-bibid", "data-bibid", "CDATA", id);
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "img", atts);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "img");
            createAnchor(xmlConsumer, "http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=" + id, item.getTitle());
            if (item.getAuthor() != null) {
                createElement(xmlConsumer, "div", item.getAuthor());
            }
            createElement(xmlConsumer, "div", "Status: " + item.getStatus());
            startDiv(xmlConsumer);
            createElement(xmlConsumer, "strong", "Call #: " + item.getCallNumber());
            endDiv(xmlConsumer);
            endLi(xmlConsumer);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
