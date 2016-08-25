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
            boolean isDigital = item.isDigital();
            String url = item.getURL();
            String href;
            if (isDigital && url != null) {
                href = url;
            } else {
                href = "http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=" + id;
            }
            createAnchor(xmlConsumer, href, item.getTitle());
            String author = item.getAuthor();
            if (author != null) {
                createElement(xmlConsumer, "div", Normalizer.normalize(author, Form.NFKC));
            }
            startDiv(xmlConsumer);
            createStrong(xmlConsumer, "Book ");
            XMLUtils.data(xmlConsumer, isDigital ? "Digital" : "Print");
            endDiv(xmlConsumer);
            if (!isDigital) {
                availableCount(xmlConsumer, item.getAvailableCount());
                callNumber(xmlConsumer, item.getCallNumber());
            }
            endLi(xmlConsumer);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void availableCount(final XMLConsumer xmlConsumer, final Integer availableCount) throws SAXException {
        if (availableCount != null) {
            startDiv(xmlConsumer);
            createStrong(xmlConsumer, "Status: ");
            if (availableCount.intValue() == 0) {
                XMLUtils.data(xmlConsumer, "Checked Out");
            } else {
                XMLUtils.data(xmlConsumer, "Available ");
                createStrong(xmlConsumer, availableCount.toString());
            }
            endDiv(xmlConsumer);
        }
    }

    private void callNumber(final XMLConsumer xmlConsumer, final String callNumber) throws SAXException {
        if (callNumber != null) {
            startDiv(xmlConsumer);
            createStrong(xmlConsumer, "Call #: " + callNumber);
            endDiv(xmlConsumer);
        }
    }
}
