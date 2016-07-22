package edu.stanford.irt.laneweb.coursereserves;

import java.text.Normalizer;
import java.text.Normalizer.Form;

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
            String author = item.getAuthor();
            if (author != null) {
                createElement(xmlConsumer, "div", Normalizer.normalize(author, Form.NFKC));
            }
            Integer availableCount = item.getAvailableCount();
            if (availableCount != null) {
                startDiv(xmlConsumer);
                createElement(xmlConsumer, "strong", "Status: ");
                if (availableCount.intValue() == 0) {
                    XMLUtils.data(xmlConsumer, "Checked Out");
                } else {
                    XMLUtils.data(xmlConsumer, "Available ");
                    createElement(xmlConsumer, "strong", availableCount.toString());
                }
                endDiv(xmlConsumer);
            }
            String callNumber = item.getCallNumber();
            if (callNumber != null) {
                startDiv(xmlConsumer);
                createElement(xmlConsumer, "strong", "Call #: " + callNumber);
                endDiv(xmlConsumer);
            }
            endLi(xmlConsumer);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
