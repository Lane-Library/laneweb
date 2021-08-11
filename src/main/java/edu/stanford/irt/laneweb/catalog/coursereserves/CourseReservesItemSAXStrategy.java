package edu.stanford.irt.laneweb.catalog.coursereserves;

import java.text.Normalizer;
import java.text.Normalizer.Form;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.CourseReservesItem;
import edu.stanford.irt.coursereserves.ItemType;
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
            ItemType type = item.getType();
            boolean isDigital = type == ItemType.DIGITAL_BOOK || type == ItemType.VIDEO;
            String url = item.getURL();
            String href;
            if (isDigital && url != null) {
                href = url;
            } else {
                href = "https://lane.stanford.edu/view/bib/" + id;
            }
            createAnchor(xmlConsumer, href, item.getTitle());
            String author = item.getAuthor();
            if (author != null) {
                createElement(xmlConsumer, "div", Normalizer.normalize(author, Form.NFKC));
            }
            startDiv(xmlConsumer);
            if (type == ItemType.DIGITAL_BOOK || type == ItemType.PRINT_BOOK) {
                createStrong(xmlConsumer, "Book ");
                XMLUtils.data(xmlConsumer, isDigital ? "Digital" : "Print");
            } else if (type == ItemType.VIDEO) {
                createStrong(xmlConsumer, "Video");
            }
            String versionNote = item.getVersionNote();
            if (versionNote != null) {
                createSpanWithClass(xmlConsumer, "version-note", versionNote);
            }
            endDiv(xmlConsumer);
            if (type == ItemType.PRINT_BOOK) {
                availableCount(xmlConsumer, item.getAvailableCount(), id);
                callNumber(xmlConsumer, item.getCallNumber());
            }
            endDiv(xmlConsumer);
            endDiv(xmlConsumer);
            endLi(xmlConsumer);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void availableCount(final XMLConsumer xmlConsumer, final Integer availableCount, final String id)
            throws SAXException {
        if (availableCount != null) {
            startDiv(xmlConsumer);
            createStrong(xmlConsumer, "Status: ");
            if (availableCount.intValue() == 0) {
                XMLUtils.data(xmlConsumer, "Checked Out");
            } else {
                XMLUtils.data(xmlConsumer, "Available ");
                createStrong(xmlConsumer, availableCount.toString());
                startElementWithClass(xmlConsumer, "span", "requestIt");
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "btn alt");
                atts.addAttribute(EMPTY_NS, "title", "title", CDATA, "Request this item");
                atts.addAttribute(EMPTY_NS, "href", "href", CDATA,
                        "https://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=" + id + "&lw.req=true");
                atts.addAttribute(EMPTY_NS, "rel", "rel", CDATA, "popup console 1020 800");
                XMLUtils.startElement(xmlConsumer, XHTML_NS, "a", atts);
                XMLUtils.data(xmlConsumer, "Request");
                XMLUtils.endElement(xmlConsumer, XHTML_NS, "a");
                XMLUtils.endElement(xmlConsumer, XHTML_NS, "span");
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
