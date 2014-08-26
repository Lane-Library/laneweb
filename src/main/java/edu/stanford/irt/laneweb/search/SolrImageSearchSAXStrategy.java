package edu.stanford.irt.laneweb.search;

import java.util.List;

import org.springframework.data.domain.Page;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.solr.Image;

public class SolrImageSearchSAXStrategy extends AbstractXHTMLSAXStrategy<Page<Image>> {

    private static final String CDATA = "CDATA";

    private static final String CLASS = "class";

    private static final String DIV = "div";

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";
    
    protected static final String UL = "ul";

    private static final String ANCHOR = "a";
    
    private static final String ID = "id";

    private static final String IMAGE = "image";

    private static final String REL = "rel";

    private static final String SRC = "src";

    private static final String STYLE = "style";

    

    @Override
    public void toSAX(final Page<Image> result, final XMLConsumer xmlConsumer) {
        try {
            xmlConsumer.startDocument();
            startDiv(xmlConsumer);
            toSAXResult(result, xmlConsumer);
            endDiv(xmlConsumer);
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void toSAXResult(final Page<Image> result,    final XMLConsumer xmlConsumer) {
        List<Image> images = result.getContent();
        try {
            startDiv(xmlConsumer);
            String number = String.valueOf( result.getNumberOfElements() * result.getNumber() + images.size());
            createTitle(xmlConsumer, number, String.valueOf(result.getTotalElements()));
            startElementWithId(xmlConsumer, UL, "imageList");
            for (Image image : images) {
                generateImages(xmlConsumer, image.getId(), image.getTitle(), image.getPageUrl(), image.getThumbnailSrc(), image.getSrc(),
                String.valueOf(image.getCopyrightValue()));
            }
            endUl(xmlConsumer);
            endDiv(xmlConsumer);
            generateTooltips(xmlConsumer, images);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    protected void generateTooltips(final XMLConsumer xmlConsumer,
            final List<Image> images) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, STYLE, STYLE, CDATA, "display:none");
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "tooltips");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
        for (Image image : images) {
            generateTooltipsImage(xmlConsumer, image.getId(), image.getSrc());
        }

        endDiv(xmlConsumer);
    }
    
    protected void createImage(final XMLConsumer xmlConsumer, final String id, final String thumbnailSrc, final String src) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, SRC, SRC, CDATA, thumbnailSrc);
        if (null != id && src != null && !"".equals(src)) {
            atts.addAttribute(XHTML_NS, ID, ID, CDATA, id);
            atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "yui3-tooltip-trigger");
        }
        XMLUtils.startElement(xmlConsumer, XHTML_NS, IMAGE, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, IMAGE);
    }

    protected void createTitle(final XMLConsumer xmlConsumer,final String hits, final String total) throws SAXException {
        startElementWithId(xmlConsumer, DIV, "result");
        XMLUtils.data(xmlConsumer, " ".concat(hits).concat(" of "));
        XMLUtils.data(xmlConsumer, total.concat(" images found"));
        endDiv(xmlConsumer);
    }

    protected void generateImages(final XMLConsumer xmlConsumer, final String id, final String title, final String url,
            final String thumbnailSrc, final String imageSrc, final String copyright) throws SAXException {
        
        XMLUtils.startElement(xmlConsumer, XHTML_NS, "li");
        startAnchor(xmlConsumer, url);
        startElementWithId(xmlConsumer, DIV, "image");
        createImage(xmlConsumer, id, thumbnailSrc , imageSrc);
        endDiv(xmlConsumer);
        startDiv(xmlConsumer);
        if(title.length() > 90 ){
            XMLUtils.data(xmlConsumer, title.substring(0, 90).concat("...."));
        } else {
        XMLUtils.data(xmlConsumer, title);
        }
        endDiv(xmlConsumer);
        endAnchor(xmlConsumer);
        generateWebsiteSource(xmlConsumer, id, url);
        generateCopyright(xmlConsumer, id);
        
        endLi(xmlConsumer);
    }

    private void generateCopyright(final XMLConsumer xmlConsumer, final String id) throws SAXException {
        startElementWithId(xmlConsumer, DIV, "copyright");
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, REL, REL, CDATA, "popup local ".concat(id.substring(0, id.indexOf("/")).replaceAll("\\.", "-")));
        XMLUtils.startElement(xmlConsumer, XHTML_NS, ANCHOR, atts);
        XMLUtils.data(xmlConsumer, "Copyright Information ");
        atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "fa fa-arrow-right");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, "i", atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, "i");
        XMLUtils.endElement(xmlConsumer, XHTML_NS, ANCHOR);
        endDiv(xmlConsumer);
    }
        
    
    private void generateWebsiteSource(final XMLConsumer xmlConsumer, final String id, final String url) throws SAXException {
        startElementWithId(xmlConsumer, DIV, "website-id");
        XMLUtils.data(xmlConsumer, "Source: ");
        startAnchor(xmlConsumer, url);
        if (id.startsWith("lane.bassett")) {
            XMLUtils.data(xmlConsumer, "Bassett");
        } else if (id.startsWith("ncbi.nlm.nih.gov")) {
            XMLUtils.data(xmlConsumer, "PubMed Central");
        } else {
            XMLUtils.data(xmlConsumer, id.substring(0, id.indexOf("/")));
        }
        endAnchor(xmlConsumer);
        endDiv(xmlConsumer);
    }
    
    protected void generateTooltipsImage(final XMLConsumer xmlConsumer, final String id, final String imageSrc)
            throws SAXException {
        if (null != imageSrc && !"".equals(imageSrc)) {
            startElementWithId(xmlConsumer, "span", id.concat("Tooltip"));
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(XHTML_NS, SRC, SRC, CDATA, imageSrc);
            atts.addAttribute(XHTML_NS, STYLE, STYLE, CDATA, "max-width: 300px;max-height: 240px");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, IMAGE, atts);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, IMAGE);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "span");
        }
    }

    protected void startElementWithId(final XMLConsumer xmlConsumer, final String name, final String id)
            throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, ID, ID, CDATA, id == null ? "" : id);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, name, atts);
    }

}
