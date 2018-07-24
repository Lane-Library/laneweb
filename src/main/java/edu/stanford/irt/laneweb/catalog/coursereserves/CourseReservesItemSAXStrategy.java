package edu.stanford.irt.laneweb.catalog.coursereserves;

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

    private static final String CDATA = "CDATA";

    private static final String CLASS = "class";

    private static final String EMPTY_NS = "";

    @Override
    public void toSAX(final CourseReservesItem item, final XMLConsumer xmlConsumer) {
        try {
            startLiWithClass(xmlConsumer, "resource course-reserves-item");
            startDivWithClass(xmlConsumer, "pure-g");
            startDivWithClass(xmlConsumer, "pure-u-1-8");
            String id = Integer.toString(item.getId());
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, "data-bibid", "data-bibid", CDATA, id);
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "bookcover");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "div", atts);
            createElementWithClass(xmlConsumer, "i", "fa fa-book", "");
            endDiv(xmlConsumer);
            endDiv(xmlConsumer);
            startDivWithClass(xmlConsumer, "pure-u-7-8");
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
            endDiv(xmlConsumer);
            endDiv(xmlConsumer);
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
