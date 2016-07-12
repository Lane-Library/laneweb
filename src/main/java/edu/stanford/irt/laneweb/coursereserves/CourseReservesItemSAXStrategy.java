package edu.stanford.irt.laneweb.coursereserves;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.CourseReservesItem;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;

public class CourseReservesItemSAXStrategy extends AbstractXHTMLSAXStrategy<CourseReservesItem> {

    @Override
    public void toSAX(final CourseReservesItem item, final XMLConsumer xmlConsumer) {
        try {
            startLi(xmlConsumer);
            createAnchor(xmlConsumer, "http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=" + item.getId(),
                    item.getTitle());
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
