package edu.stanford.irt.laneweb.eresources.browse;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.model.Eresource;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class LinkWithCoverEresourceSAXStrategy extends AbstractXHTMLSAXStrategy<Eresource> {

    private static final String CDATA = "CDATA";

    private static final String EMPTY_NS = "";

    @Override
    public void toSAX(final Eresource eresource, final XMLConsumer xmlConsumer) {
        try {
            String bibID = eresource.getRecordId();
            String href = eresource.getLinks()
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new LanewebException("no link for eresource " + bibID))
                    .getUrl();
            String fontAwesomeClass;
            if (eresource.getPrimaryType().startsWith("Journal")) {
                fontAwesomeClass = "fa-solid fa-newspaper";
            } else {
                fontAwesomeClass = "fa-light fa-book";
            }
            startAnchor(xmlConsumer, href);
            String title = eresource.getTitle();
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, "data-bibid", "data-bibid", CDATA, bibID);
            atts.addAttribute(EMPTY_NS, "class", "class", CDATA, "bookcover");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "div", atts);
            createElementWithClass(xmlConsumer, "i", fontAwesomeClass, "");
            endDiv(xmlConsumer);
            endAnchor(xmlConsumer);
            createAnchor(xmlConsumer, href, title);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
