package edu.stanford.irt.laneweb.search.saxstrategy;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.solr.Image;

public class SolrAdminImageSearchSAXStrategy extends SolrImageSearchSAXStrategy {

    @Override
    protected void generateImages(final XMLConsumer xmlConsumer, final Image image, final int imageNumber)
            throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        if (!image.isEnable()) {
            atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "admin admin-disable");
        } else {
            atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "admin");
        }
        atts.addAttribute(XHTML_NS, ID, ID, CDATA, image.getId());
        atts.addAttribute(XHTML_NS, "row", "row", CDATA, String.valueOf(imageNumber / IMAGE_BY_ROW));
        XMLUtils.startElement(xmlConsumer, XHTML_NS, "li", atts);
        atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, HREF, HREF, CDATA, image.getPageUrl());
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "noproxy");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, "a", atts);
        atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, SRC, SRC, CDATA, image.getThumbnailSrc());
        XMLUtils.startElement(xmlConsumer, XHTML_NS, IMAGE, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, IMAGE);
        endAnchor(xmlConsumer);
        startDivWithClass(xmlConsumer, "imagedecoHidden");
        String imageId = image.getId();
        try {
            atts = new AttributesImpl();
            atts.addAttribute(XHTML_NS, HREF, HREF, CDATA,
                    "/secure/image/update?id=" + URLEncoder.encode(imageId, "UTF-8"));
            atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "imagedeco-admin");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "a", atts);
            XMLUtils.data(xmlConsumer, imageId.substring(imageId.lastIndexOf("/") + 1));
            endAnchor(xmlConsumer);
        } catch (UnsupportedEncodingException e) {
            throw new LanewebException(e);
        }
        endDiv(xmlConsumer);
        endLi(xmlConsumer);
    }
}
