package edu.stanford.irt.laneweb.eresources.browse;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class LinkWithCoverEresourceSAXStrategy extends AbstractXHTMLSAXStrategy<Eresource> {

    private static final String CDATA = "CDATA";

    private static final String EMPTY_NS = "";

    @Override
    public void toSAX(final Eresource eresource, final XMLConsumer xmlConsumer) {
        try {
            String href = eresource.getLinks().stream().findFirst().get().getUrl();
            startAnchor(xmlConsumer, href);
            String bibID = eresource.getId().substring(4);
            String title = eresource.getTitle();
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, "data-bibid", "data-bibid", CDATA, bibID);
            atts.addAttribute(EMPTY_NS, "class", "class", CDATA, "bookcover module-img");
            atts.addAttribute(EMPTY_NS, "width", "width", CDATA, "59");
            atts.addAttribute(EMPTY_NS, "height", "height", CDATA, "81");
            atts.addAttribute(EMPTY_NS, "title", "title", CDATA, "Book Cover: " + title);
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "img", atts);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "img");
            endAnchor(xmlConsumer);
            createAnchor(xmlConsumer, href, title);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
