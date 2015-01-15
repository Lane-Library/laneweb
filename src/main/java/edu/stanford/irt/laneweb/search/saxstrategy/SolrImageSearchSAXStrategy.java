package edu.stanford.irt.laneweb.search.saxstrategy;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.solr.Image;

public class SolrImageSearchSAXStrategy extends AbstractXHTMLSAXStrategy<Map<String, Object>> {
    
    private static final int MAX_TITLE_LENGTH = 90;

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    private static final String CDATA = "CDATA";

    private static final String CLASS = "class";

    private static final String DIV = "div";

    private static final String UL = "ul";

    private static final String ANCHOR = "a";

    private static final String HREF = "href";

    private static final String ID = "id";

    private static final String IMAGE = "image";

    private static final String REL = "rel";

    private static final String SRC = "src";

    private static final String STYLE = "style";
    
    private static final String INPUT = "input";
    
    private static final String TYPE = "type";
    
    private static final String VALUE = "value";
    
    private static final String NAME = "name";
    
    private static final String LABEL = "label";
    
    private static final String SPAN = "span";
    
    private static final String ACTIVED = "actived";
    
    private static final String DISABLED = "disabled";
    
    private static final String HIDDEN = "hidden";
    
    private static final String PREVIEW_IMAGE_NOT_AVAILABLE = "/graphics/2014/no-previewavail.jpg";

    public void toSAX(final Map<String, Object> result, final XMLConsumer xmlConsumer) {
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

    private void toSAXResult(final Map<String, Object> result, final XMLConsumer xmlConsumer) throws SAXException {
        @SuppressWarnings("unchecked")
        Page<Image> page = ((Page<Image>) result.get("page"));
        List<Image> images = page.getContent();
        generateSumaryResult(xmlConsumer, page, result, true);
        startElementWithId(xmlConsumer, UL, "imageList");
        for (Image image : images) {
            generateImages(xmlConsumer, image);
        }
        endUl(xmlConsumer);
        generateSumaryResult(xmlConsumer, page, result, false);
        generateTooltips(xmlConsumer, images);
    }

    protected void generateTooltips(final XMLConsumer xmlConsumer, final List<Image> images) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, STYLE, STYLE, CDATA, "display:none");
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "tooltips");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
        for (Image image : images) {
            generateImagePopup(xmlConsumer, image);
        }
        endDiv(xmlConsumer);
        atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "popups");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
        
        for (Image image : images) {
            generateCopyrightPopup(xmlConsumer, image);
        }
        endDiv(xmlConsumer);
    }

    protected void createImage(final XMLConsumer xmlConsumer, final String id, final String thumbnailSrc,
            final String src) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, SRC, SRC, CDATA, thumbnailSrc);
        if (null != id) {
            atts.addAttribute(XHTML_NS, ID, ID, CDATA, id);
            atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "yui3-tooltip-trigger");
        }
        XMLUtils.startElement(xmlConsumer, XHTML_NS, IMAGE, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, IMAGE);
    }

    protected void generatePagination(final XMLConsumer xmlConsumer, final Page<Image> page, final Map<String, Object> result)
            throws SAXException {
        String path =  (String) result.get("path");
        startDivWithClass(xmlConsumer, "pagination");
        int currentPage = page.getNumber();
        int totalPages = page.getTotalPages();
        if (currentPage != 0) {
            createBackwardLink(xmlConsumer, "fa fa-fast-backward", "First", ACTIVED, path + "1");
            createBackwardLink(xmlConsumer, "fa fa-step-backward", "Previous", ACTIVED,  path + currentPage);
        } else {
            createBackwardLink(xmlConsumer, "fa fa-fast-backward", "First", DISABLED, null);
            createBackwardLink(xmlConsumer, "fa fa-step-backward", "Previous", DISABLED, null);
        }
        XMLUtils.startElement(xmlConsumer, XHTML_NS, LABEL);
        XMLUtils.data(xmlConsumer, "Page ");
        XMLUtils.endElement(xmlConsumer, XHTML_NS, LABEL);
        generateDirectAccessPageForm(xmlConsumer, page, result);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, LABEL);
        XMLUtils.data(xmlConsumer,  " of " + totalPages);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, LABEL);
        if (currentPage != totalPages - 1) {
            createForwardLink(xmlConsumer, "fa fa-step-forward", "Next", ACTIVED, path + (currentPage + 2));
            createForwardLink(xmlConsumer, "fa fa-fast-forward", "Last", ACTIVED, path + totalPages);
        } else {
            createForwardLink(xmlConsumer, "fa fa-step-forward", "Next", DISABLED, null);
            createForwardLink(xmlConsumer, "fa fa-fast-forward", "Last", DISABLED, null);
        }
        
        endDiv(xmlConsumer);
    }

    
    
    protected void generateDirectAccessPageForm(final XMLConsumer xmlConsumer, final Page<Image> page, final Map<String, Object> result) throws SAXException{
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA,"pagingForm");
        atts.addAttribute(XHTML_NS, NAME, NAME, CDATA,"paginationForm");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, "form", atts);
        atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, TYPE, TYPE, CDATA, HIDDEN);
        atts.addAttribute(XHTML_NS, VALUE, VALUE , CDATA, (String)result.get(Model.SOURCE));
        atts.addAttribute(XHTML_NS, NAME, NAME , CDATA, "source");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, INPUT, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, INPUT);
        atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, TYPE, TYPE, CDATA, HIDDEN);
        atts.addAttribute(XHTML_NS, VALUE, VALUE , CDATA, (String)result.get(Model.QUERY));
        atts.addAttribute(XHTML_NS, NAME, NAME , CDATA, "q");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, INPUT, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, INPUT);
        atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, TYPE, TYPE, CDATA, HIDDEN);
        atts.addAttribute(XHTML_NS, VALUE, VALUE , CDATA, String.valueOf( page.getTotalPages()));
        atts.addAttribute(XHTML_NS, NAME, NAME , CDATA, "totalPages");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, INPUT, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, INPUT);
        atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, TYPE, TYPE, CDATA, "text");
        atts.addAttribute(XHTML_NS, "size", "size", CDATA, "4");
        atts.addAttribute(XHTML_NS, VALUE, VALUE , CDATA, String.valueOf((page.getNumber() + 1)));
        atts.addAttribute(XHTML_NS, NAME, NAME , CDATA, "page");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, INPUT, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, INPUT);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, "form");
    }
    
    protected void generateResult(final XMLConsumer xmlConsumer, final Page<Image> page, final Map<String, Object> result) throws SAXException {
        String numberResult = String.valueOf(page.getSize() * page.getNumber() + 1);
        String number = String.valueOf(page.getSize() * page.getNumber() + page.getNumberOfElements());
        startDivWithClass(xmlConsumer, "result");
        if (page.hasContent()) {
            XMLUtils.data(xmlConsumer,  numberResult + " to " + number + " of " + page.getTotalElements() + " results");
        } else {
            XMLUtils.data(xmlConsumer,
                    "No " + result.get("tab") + " images are available with search term, " + result.get(Model.QUERY));
        }
        endDiv(xmlConsumer);
    }

    private void generateSumaryResult(final XMLConsumer xmlConsumer, final Page<Image> page,
            final Map<String, Object> result, boolean isTopScreen) throws SAXException {
        startDivWithClass(xmlConsumer, "result-summary");
        if(isTopScreen){
            generateResult(xmlConsumer, page, result);
        }
        if (page.getTotalPages() > 1) {
            generatePagination(xmlConsumer, page, result);
        }
        endDiv(xmlConsumer);

    }

    protected void generateImages(final XMLConsumer xmlConsumer, final Image image) throws SAXException {

        XMLUtils.startElement(xmlConsumer, XHTML_NS, "li");
        startAnchor(xmlConsumer, image.getPageUrl());
        startElementWithId(xmlConsumer, DIV, "image");
        createImage(xmlConsumer, image.getId(), image.getThumbnailSrc(), image.getSrc());
        endDiv(xmlConsumer);
        startDiv(xmlConsumer);
        String title = image.getTitle();
        if (title.length() > MAX_TITLE_LENGTH) {
            title = title.substring(0, MAX_TITLE_LENGTH).concat("....");
        }
        XMLUtils.data(xmlConsumer, title);
        endDiv(xmlConsumer);
        endAnchor(xmlConsumer);
        generateWebsiteSource(xmlConsumer, image.getId(), image.getPageUrl());
        generateCopyright(xmlConsumer, image);

        endLi(xmlConsumer);
    }

    private void generateCopyright(final XMLConsumer xmlConsumer, final Image image) throws SAXException {
        startElementWithId(xmlConsumer, DIV, "copyright");
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, REL, REL, CDATA,"popup local ".concat(image.getId().hashCode() +"_copyright"));
        XMLUtils.startElement(xmlConsumer, XHTML_NS, ANCHOR, atts);
        XMLUtils.data(xmlConsumer, "Copyright Information ");
        atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "fa fa-arrow-right");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, "i", atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, "i");
        XMLUtils.endElement(xmlConsumer, XHTML_NS, ANCHOR);
        endDiv(xmlConsumer);
    }

    private void generateWebsiteSource(final XMLConsumer xmlConsumer, final String id, final String url)
            throws SAXException {
        startElementWithId(xmlConsumer, DIV, "website-id");
        XMLUtils.data(xmlConsumer, "Source: ");
        startAnchor(xmlConsumer, url);
        if (id.startsWith("ncbi.nlm.nih.gov")) {
            XMLUtils.data(xmlConsumer, "PubMed Central");
        } else {
            XMLUtils.data(xmlConsumer, id.substring(0, id.indexOf("/")));
        }
        endAnchor(xmlConsumer);
        endDiv(xmlConsumer);
    }

    private void generateImagePopup(final XMLConsumer xmlConsumer, final Image image)
            throws SAXException {
        if (null != image.getId()) {
            startElementWithId(xmlConsumer, "span", image.getId().concat("Tooltip"));
            AttributesImpl atts = new AttributesImpl();
            if( null != image.getSrc() && !"".equals(image.getSrc())){
            	atts.addAttribute(XHTML_NS, SRC, SRC, CDATA, image.getSrc());
            }else{
            	atts.addAttribute(XHTML_NS, SRC, SRC, CDATA, PREVIEW_IMAGE_NOT_AVAILABLE);
            }
            atts.addAttribute(XHTML_NS, STYLE, STYLE, CDATA, "max-width: 300px;max-height: 240px");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, IMAGE, atts);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, IMAGE);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "span");
        }
    }
        
    private void generateCopyrightPopup(final XMLConsumer xmlConsumer, final Image image) throws SAXException{    
        if (null != image.getId()) {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(XHTML_NS, ID, ID, CDATA, image.getId().hashCode() +"_copyright");
            atts.addAttribute(XHTML_NS, STYLE, STYLE, CDATA, "width:450px");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, SPAN, atts);
            if(image.getCopyrightText() != null){
                startDiv(xmlConsumer);
                XMLUtils.startElement(xmlConsumer, XHTML_NS, "event_description");
                XMLUtils.data(xmlConsumer, image.getCopyrightText());
                XMLUtils.endElement(xmlConsumer, XHTML_NS, "event_description");
                XMLUtils.endElement(xmlConsumer, XHTML_NS, DIV);
            }
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "span");
        }
    }

    protected void startElementWithId(final XMLConsumer xmlConsumer, final String name, final String id)
            throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, ID, ID, CDATA, id == null ? "" : id);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, name, atts);
    }
    
    private void createBackwardLink(XMLConsumer xmlConsumer, String faClass, String text, String clazz, String href) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, clazz);
        if (href != null) {
            atts.addAttribute(XHTML_NS, HREF, HREF, CDATA, href);
        }
        XMLUtils.startElement(xmlConsumer, XHTML_NS, ANCHOR, atts);
        atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, faClass);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, SPAN , atts);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, LABEL );
        XMLUtils.data(xmlConsumer, text);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, LABEL);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, SPAN);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, ANCHOR);
    }
    
    private void createForwardLink(XMLConsumer xmlConsumer, String faClass, String text, String clazz, String href) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, clazz);
        if (href != null) {
            atts.addAttribute(XHTML_NS, HREF, HREF, CDATA, href);
        }
        XMLUtils.startElement(xmlConsumer, XHTML_NS, ANCHOR, atts);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, LABEL);
        XMLUtils.data(xmlConsumer, text);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, LABEL);
        atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, faClass);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, SPAN , atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, SPAN);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, ANCHOR);
    }
}
