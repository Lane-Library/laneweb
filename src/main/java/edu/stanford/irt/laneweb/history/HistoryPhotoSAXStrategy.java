package edu.stanford.irt.laneweb.history;

import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class HistoryPhotoSAXStrategy extends AbstractXHTMLSAXStrategy<List<HistoryPhoto>> {

    @Override
    public void toSAX(final List<HistoryPhoto> photos, final XMLConsumer xmlConsumer) {
        try {
            startHTMLDocument(xmlConsumer);
            startBody(xmlConsumer);
            photos.stream().forEach((final HistoryPhoto p) -> toSAX(p, xmlConsumer));
            endBody(xmlConsumer);
            endHTMLDocument(xmlConsumer);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void toSAX(final HistoryPhoto photo, final XMLConsumer xmlConsumer) {
        try {
            startAnchor(xmlConsumer, photo.getPage());
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", "src", "src", "CDATA", photo.getThumbnail());
            atts.addAttribute("", "alt", "alt", "CDATA", photo.getTitle());
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "img", atts);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "img");
            endAnchor(xmlConsumer);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
