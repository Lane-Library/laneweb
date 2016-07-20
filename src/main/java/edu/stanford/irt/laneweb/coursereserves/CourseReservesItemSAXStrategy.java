package edu.stanford.irt.laneweb.coursereserves;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.CourseReservesItem;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class CourseReservesItemSAXStrategy extends AbstractXHTMLSAXStrategy<CourseReservesItem> {

    private static final String NOT_CHECKED_OUT = "Not Checked Out";

    private static String replaceNotCharged(final String status) {
        if ("Not Charged".equals(status)) {
            return NOT_CHECKED_OUT;
        }
        return status;
    }

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
            toSAX(item.getStatusList(), xmlConsumer);
            String callNumber = item.getCallNumber();
            if (callNumber != null) {
                startDiv(xmlConsumer);
                createElement(xmlConsumer, "strong", "Call #: " + item.getCallNumber());
                endDiv(xmlConsumer);
            }
            endLi(xmlConsumer);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void toSAX(final List<String> statusList, final XMLConsumer xmlConsumer) throws SAXException {
        if (statusList.size() == 1) {
            createElement(xmlConsumer, "div", "Item Status: " + replaceNotCharged(statusList.get(0)));
        } else if (statusList.size() > 1) {
            for (int i = 0; i < statusList.size(); i++) {
                createElement(xmlConsumer, "div",
                        "Item " + (i + 1) + " Status: " + replaceNotCharged(statusList.get(i)));
            }
        }
    }
}
