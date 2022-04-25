package edu.stanford.irt.laneweb.images;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.solr.Image;

public class SolrAdminImageSearchSAXStrategy extends SolrImageSearchSAXStrategy {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    public SolrAdminImageSearchSAXStrategy(Map<String, String> websiteIdMapping) {
        super(websiteIdMapping);
    }

    @Override
    protected void generateImages(final XMLConsumer xmlConsumer, final Image image, final int imageNumber)
            throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        if (!image.isEnable()) {
            atts.addAttribute(EMPTY, CLASS, CLASS, CDATA, "admin admin-disable");
        } else {
            atts.addAttribute(EMPTY, CLASS, CLASS, CDATA, "admin");
        }
        atts.addAttribute(EMPTY, ID, ID, CDATA, image.getId());
        atts.addAttribute(EMPTY, "imgIndex", "imgIndex", CDATA, String.valueOf(imageNumber));
        XMLUtils.startElement(xmlConsumer, XHTML_NS, "li", atts);
        atts = new AttributesImpl();
        atts.addAttribute(EMPTY, HREF, HREF, CDATA, image.getPageUrl());
        atts.addAttribute(EMPTY, CLASS, CLASS, CDATA, "noproxy");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, "a", atts);
        atts = new AttributesImpl();
        atts.addAttribute(EMPTY, SRC, SRC, CDATA, image.getThumbnailSrc());
        XMLUtils.startElement(xmlConsumer, XHTML_NS, IMAGE, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, IMAGE);
        endAnchor(xmlConsumer);
        startDivWithClass(xmlConsumer, "imagedecoHidden");
        String imageId = image.getId();
        try {
            atts = new AttributesImpl();
            atts.addAttribute(EMPTY, HREF, HREF, CDATA, "/secure/image/update?id=" + URLEncoder.encode(imageId, UTF_8));
            atts.addAttribute(EMPTY, CLASS, CLASS, CDATA, "imagedeco-admin");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "a", atts);
            XMLUtils.data(xmlConsumer, imageId.substring(imageId.lastIndexOf('/') + 1));
            endAnchor(xmlConsumer);
        } catch (UnsupportedEncodingException e) {
            throw new SAXException(e);
        }
        endDiv(xmlConsumer);
        endLi(xmlConsumer);
    }
}
