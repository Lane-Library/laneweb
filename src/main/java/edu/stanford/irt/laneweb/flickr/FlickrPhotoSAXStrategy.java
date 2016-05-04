package edu.stanford.irt.laneweb.flickr;

import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class FlickrPhotoSAXStrategy extends AbstractXHTMLSAXStrategy<List<FlickrPhoto>> {

    @Override
    public void toSAX(final List<FlickrPhoto> photos, final XMLConsumer xmlConsumer) {
        try {
            startHTMLDocument(xmlConsumer);
            startBody(xmlConsumer);
            photos.stream().forEach(p -> toSAX(p, xmlConsumer));
            endBody(xmlConsumer);
            endHTMLDocument(xmlConsumer);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void toSAX(final FlickrPhoto photo, final XMLConsumer xmlConsumer) {
        try {
            startAnchor(xmlConsumer, photo.getPage());
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", "src", "src", "CDATA", photo.getThumbnail());
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "img", atts);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "img");
            endAnchor(xmlConsumer);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
