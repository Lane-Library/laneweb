package edu.stanford.irt.laneweb.search.saxstrategy;

import java.text.NumberFormat;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.search.SolrImageSearchResult;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.solr.Image;

public class SolrImageSearchSAXStrategy extends AbstractXHTMLSAXStrategy<SolrImageSearchResult> {

    protected static final String ACTIVED = "actived";

    protected static final String ANCHOR = "a";

    protected static final String BUTTON = "button";

    protected static final String CDATA = "CDATA";

    protected static final String CLASS = "class";

    protected static final String DIV = "div";

    protected static final String EMPTY = "";

    protected static final String HIDDEN = "hidden";

    protected static final String HREF = "href";

    protected static final String ID = "id";

    protected static final String IMAGE = "img";

    protected static final int IMAGE_BY_ROW = 5;

    protected static final String INPUT = "input";

    protected static final String LABEL = "label";

    protected static final String NAME = "name";

    protected static final String NO_BOOKMARKING = "no-bookmarking";

    protected static final String P = "p";

    protected static final String SELECTED_RESOURCE = "selectedResource";

    protected static final String SPAN = "span";

    protected static final String SRC = "src";

    protected static final String STYLE = "style";

    protected static final String TYPE = "type";

    protected static final String UL = "ul";

    protected static final String VALUE = "value";

    private Map<String, String> websiteIdMapping;

    public void setWebsiteIdMapping(final Map<String, String> websiteIdMapping) {
        this.websiteIdMapping = websiteIdMapping;
    }

    @Override
    public void toSAX(final SolrImageSearchResult result, final XMLConsumer xmlConsumer) {
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

    protected void generateImages(final XMLConsumer xmlConsumer, final Image image, final int imageNumber)
            throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY, ID, ID, CDATA, image.getId());
        atts.addAttribute(EMPTY, CLASS, CLASS, CDATA, "yui3-u-1-5");
        atts.addAttribute(EMPTY, "row", "row", CDATA, String.valueOf(imageNumber / IMAGE_BY_ROW));
        XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
        atts = new AttributesImpl();
        atts.addAttribute(EMPTY, HREF, HREF, CDATA, image.getPageUrl());
        atts.addAttribute(EMPTY, CLASS, CLASS, CDATA, "noproxy");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, ANCHOR, atts);
        atts = new AttributesImpl();
        atts.addAttribute(EMPTY, SRC, SRC, CDATA, image.getThumbnailSrc());
        XMLUtils.startElement(xmlConsumer, XHTML_NS, IMAGE, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, IMAGE);
        endAnchor(xmlConsumer);
        atts = new AttributesImpl();
        atts.addAttribute(EMPTY, ID, ID, CDATA, "imagedecorator");
        atts.addAttribute(EMPTY, CLASS, CLASS, CDATA, "imagedecoHidden");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
        XMLUtils.data(xmlConsumer, " ");
        endDiv(xmlConsumer);
        endDiv(xmlConsumer);
    }

    private void createBackwardLink(final XMLConsumer xmlConsumer, final String faClass, final String text,
            final String clazz, final String href) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY, CLASS, CLASS, CDATA, clazz + " " + NO_BOOKMARKING);
        if (href != null) {
            atts.addAttribute(EMPTY, HREF, HREF, CDATA, href);
        }
        XMLUtils.startElement(xmlConsumer, XHTML_NS, ANCHOR, atts);
        atts = new AttributesImpl();
        atts.addAttribute(EMPTY, CLASS, CLASS, CDATA, faClass + " " + NO_BOOKMARKING);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, SPAN, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, SPAN);
        XMLUtils.data(xmlConsumer, " " + text);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, ANCHOR);
    }

    private void createForwardLink(final XMLConsumer xmlConsumer, final String faClass, final String text,
            final String clazz, final String href) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY, CLASS, CLASS, CDATA, clazz + " " + NO_BOOKMARKING);
        if (href != null) {
            atts.addAttribute(EMPTY, HREF, HREF, CDATA, href);
        }
        XMLUtils.startElement(xmlConsumer, XHTML_NS, ANCHOR, atts);
        XMLUtils.data(xmlConsumer, text + " ");
        atts = new AttributesImpl();
        atts.addAttribute(EMPTY, CLASS, CLASS, CDATA, faClass + " " + NO_BOOKMARKING);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, SPAN, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, SPAN);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, ANCHOR);
    }

    private void generateDetailImage(final XMLConsumer xmlConsumer, final int rowNumber) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY, CLASS, CLASS, CDATA, "imageDetailHidden");
        atts.addAttribute(EMPTY, STYLE, STYLE, CDATA, "width:auto; height:auto; min-height:350px;");
        atts.addAttribute(EMPTY, ID, ID, CDATA, "imageDetail_" + rowNumber);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
        atts = new AttributesImpl();
        atts.addAttribute(EMPTY, ID, ID, CDATA, "image-detail-close");
        atts.addAttribute(EMPTY, CLASS, CLASS, CDATA, "image-detail-close close fa fa-close");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
        endDiv(xmlConsumer);
        startDivWithClass(xmlConsumer, "yui3-g");
        startDivWithClass(xmlConsumer, "yui3-u-3-5");
        startDivWithClass(xmlConsumer, "image-location");
        atts = new AttributesImpl();
        atts.addAttribute(EMPTY, CLASS, CLASS, CDATA, "image");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, IMAGE, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, IMAGE);
        endDiv(xmlConsumer);
        endDiv(xmlConsumer);
        startDivWithClass(xmlConsumer, "yui3-u-2-5");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, "h3");
        XMLUtils.endElement(xmlConsumer, XHTML_NS, "h3");
        startDivWithClass(xmlConsumer, "desc");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, LABEL);
        XMLUtils.data(xmlConsumer, "Description: ");
        XMLUtils.endElement(xmlConsumer, XHTML_NS, LABEL);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, P);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, P);
        endDiv(xmlConsumer);
        startDivWithClass(xmlConsumer, "copyright");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, LABEL);
        XMLUtils.data(xmlConsumer, "Rights Statement: ");
        XMLUtils.endElement(xmlConsumer, XHTML_NS, LABEL);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, P);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, P);
        endDiv(xmlConsumer);
        startDivWithClass(xmlConsumer, "article-title");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, LABEL);
        XMLUtils.data(xmlConsumer, "Article Title: ");
        XMLUtils.endElement(xmlConsumer, XHTML_NS, LABEL);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, P);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, P);
        endDiv(xmlConsumer);
        startDivWithClass(xmlConsumer, "to-image");
        startAnchor(xmlConsumer, "");
        atts = new AttributesImpl();
        atts.addAttribute(EMPTY, CLASS, CLASS, CDATA, BUTTON);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, BUTTON, atts);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, SPAN, atts);
        XMLUtils.data(xmlConsumer, "Visit Source Page ");
        XMLUtils.endElement(xmlConsumer, XHTML_NS, SPAN);
        atts = new AttributesImpl();
        atts.addAttribute(EMPTY, CLASS, CLASS, CDATA, "fa fa-arrow-right");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, "i", atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, "i");
        XMLUtils.endElement(xmlConsumer, XHTML_NS, BUTTON);
        endAnchor(xmlConsumer);
        endDiv(xmlConsumer);
        endDiv(xmlConsumer);
        endDiv(xmlConsumer);
        endDiv(xmlConsumer);
    }

    private void generateDirectAccessPageForm(final XMLConsumer xmlConsumer, final Page<Image> page,
            final SolrImageSearchResult result) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY, CLASS, CLASS, CDATA, "pagingForm");
        atts.addAttribute(EMPTY, NAME, NAME, CDATA, "paginationForm");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, "form", atts);
        atts = new AttributesImpl();
        atts.addAttribute(EMPTY, TYPE, TYPE, CDATA, HIDDEN);
        atts.addAttribute(EMPTY, VALUE, VALUE, CDATA, result.getSource());
        atts.addAttribute(EMPTY, NAME, NAME, CDATA, "source");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, INPUT, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, INPUT);
        atts = new AttributesImpl();
        atts.addAttribute(EMPTY, TYPE, TYPE, CDATA, HIDDEN);
        atts.addAttribute(EMPTY, VALUE, VALUE, CDATA, result.getQuery());
        atts.addAttribute(EMPTY, NAME, NAME, CDATA, "q");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, INPUT, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, INPUT);
        atts = new AttributesImpl();
        atts.addAttribute(EMPTY, TYPE, TYPE, CDATA, HIDDEN);
        atts.addAttribute(EMPTY, VALUE, VALUE, CDATA, String.valueOf(page.getTotalPages()));
        atts.addAttribute(EMPTY, NAME, NAME, CDATA, "totalPages");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, INPUT, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, INPUT);
        if (result.getSelectedResource() != null && !"".equals(result.getSelectedResource())) {
            atts = new AttributesImpl();
            atts.addAttribute(EMPTY, TYPE, TYPE, CDATA, HIDDEN);
            atts.addAttribute(EMPTY, VALUE, VALUE, CDATA, result.getSelectedResource());
            atts.addAttribute(EMPTY, NAME, NAME, CDATA, "rid");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, INPUT, atts);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, INPUT);
        }
        atts = new AttributesImpl();
        atts.addAttribute(EMPTY, TYPE, TYPE, CDATA, "text");
        atts.addAttribute(EMPTY, "size", "size", CDATA, "4");
        atts.addAttribute(EMPTY, VALUE, VALUE, CDATA, String.valueOf(page.getNumber() + 1));
        atts.addAttribute(EMPTY, NAME, NAME, CDATA, "page");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, INPUT, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, INPUT);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, "form");
    }

    private void generateFilterWebsiteIdOptions(final XMLConsumer xmlConsumer, final SolrImageSearchResult result)
            throws SAXException {
        Page<FacetFieldEntry> facet = result.getFacet();
        int totalFacet = facet.getNumberOfElements();
        if (totalFacet > 0) {
            String path = result.getPath();
            startDivWithClass(xmlConsumer, "yui3-u-1-4");
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(EMPTY, CLASS, CLASS, CDATA, "view-by");
            atts.addAttribute(EMPTY, ID, ID, CDATA, "sourceFilter");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
            List<FacetFieldEntry> facetList = facet.getContent();
            XMLUtils.data(xmlConsumer, "Filter by source ");
            atts = new AttributesImpl();
            atts.addAttribute(EMPTY, CLASS, CLASS, CDATA, "general-dropdown dropdown");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
            atts = new AttributesImpl();
            atts.addAttribute(EMPTY, CLASS, CLASS, CDATA, "general-dropdown-trigger");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
            String selectedResource = "All";
            if (totalFacet == 1) {
                selectedResource = facetList.get(0).getValue();
            }
            int totalElement = 0;
            int totalSelectedFacet = 0;
            if (result.getSelectedResource() != null && !"".equals(result.getSelectedResource())) {
                selectedResource = result.getSelectedResource();
            }
            for (FacetFieldEntry facetFieldEntry : facetList) {
                totalElement = totalElement + (int) facetFieldEntry.getValueCount();
                if (selectedResource.equals(facetFieldEntry.getValue())) {
                    totalSelectedFacet = (int) facetFieldEntry.getValueCount();
                }
            }
            NumberFormat nf = NumberFormat.getInstance();
            if (!"All".equals(selectedResource)) {
                XMLUtils.data(xmlConsumer,
                        getDisplayedResourceName(selectedResource) + " (" + nf.format(totalSelectedFacet) + ")");
            } else {
                XMLUtils.data(xmlConsumer,
                        getDisplayedResourceName(selectedResource) + " (" + nf.format(totalElement) + ")");
            }
            createElementWithClass(xmlConsumer, "i", "fa fa-angle-double-down", "");
            endDiv(xmlConsumer);
            startDivWithClass(xmlConsumer, "general-dropdown-content dropdown-content");
            startUlWithClass(xmlConsumer, "pagingLabels");
            if (totalFacet > 1 && !"All".equals(selectedResource)) {
                startLi(xmlConsumer);
                startAnchor(xmlConsumer, path);
                XMLUtils.data(xmlConsumer, "All (" + nf.format(totalElement) + ")");
                endAnchor(xmlConsumer);
                endLi(xmlConsumer);
            }
            for (FacetFieldEntry facetFieldEntry : facetList) {
                startLi(xmlConsumer);
                startAnchor(xmlConsumer, path + "&rid=" + facetFieldEntry.getValue());
                XMLUtils.data(xmlConsumer, getDisplayedResourceName(facetFieldEntry.getValue()) + " ("
                        + nf.format(facetFieldEntry.getValueCount()) + ") ");
                endAnchor(xmlConsumer);
                endLi(xmlConsumer);
            }
            endUl(xmlConsumer);
            endDiv(xmlConsumer);
            endDiv(xmlConsumer);
            endDiv(xmlConsumer);
            endDiv(xmlConsumer);
        }
    }

    private void generatePagination(final XMLConsumer xmlConsumer, final Page<Image> page,
            final SolrImageSearchResult result) throws SAXException {
        String path = result.getPath();
        if (result.getSelectedResource() != null && !"".equals(result.getSelectedResource())) {
            path = path + "&rid=" + result.getSelectedResource();
        }
        path = path.concat("&page=");
        startDivWithClass(xmlConsumer, "pagination");
        int currentPage = page.getNumber();
        int totalPages = page.getTotalPages();
        if (currentPage != 0) {
            createBackwardLink(xmlConsumer, "fa fa-fast-backward", "First", ACTIVED, path + "1");
            createBackwardLink(xmlConsumer, "fa fa-step-backward", "Previous", ACTIVED, path + currentPage);
        }
        XMLUtils.startElement(xmlConsumer, XHTML_NS, LABEL);
        XMLUtils.data(xmlConsumer, "Page ");
        XMLUtils.endElement(xmlConsumer, XHTML_NS, LABEL);
        generateDirectAccessPageForm(xmlConsumer, page, result);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, LABEL);
        XMLUtils.data(xmlConsumer, " of " + totalPages);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, LABEL);
        if (currentPage != totalPages - 1) {
            createForwardLink(xmlConsumer, "fa fa-step-forward", "Next", ACTIVED, path + (currentPage + 2));
            createForwardLink(xmlConsumer, "fa fa-fast-forward", "Last", ACTIVED, path + totalPages);
        }
        endDiv(xmlConsumer);
    }

    private void generateResult(final XMLConsumer xmlConsumer, final Page<Image> page,
            final SolrImageSearchResult result) throws SAXException {
        String numberResult = String.valueOf(page.getSize() * page.getNumber() + 1);
        String number = String.valueOf(page.getSize() * page.getNumber() + page.getNumberOfElements());
        startDivWithClass(xmlConsumer, "result");
        if (page.hasContent()) {
            XMLUtils.data(xmlConsumer, numberResult + " to " + number + " of " + page.getTotalElements() + " results");
        } else {
            XMLUtils.data(xmlConsumer,
                    "No " + result.getTab() + " images are available with search term: " + result.getQuery());
        }
        endDiv(xmlConsumer);
    }

    private void generateSumaryResult(final XMLConsumer xmlConsumer, final Page<Image> page,
            final SolrImageSearchResult result, final boolean isTopScreen) throws SAXException {
        startDivWithClass(xmlConsumer, "result-summary");
        if (isTopScreen) {
            generateResult(xmlConsumer, page, result);
            generateFilterWebsiteIdOptions(xmlConsumer, result);
        }
        if (page.getTotalPages() > 1) {
            generatePagination(xmlConsumer, page, result);
        }
        endDiv(xmlConsumer);
    }

    private String getDisplayedResourceName(final String resourceName) {
        if (this.websiteIdMapping.containsKey(resourceName)) {
            return this.websiteIdMapping.get(resourceName);
        }
        return resourceName;
    }

    private void toSAXResult(final SolrImageSearchResult result, final XMLConsumer xmlConsumer) throws SAXException {
        Page<Image> page = result.getPage();
        List<Image> images = page.getContent();
        generateSumaryResult(xmlConsumer, page, result, true);
        int index = 0;
        for (Image image : images) {
            if (index % IMAGE_BY_ROW == 0) {
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute(EMPTY, ID, ID, CDATA, "imageList");
                atts.addAttribute(EMPTY, CLASS, CLASS, CDATA, "yui3-g");
                XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
            }
            generateImages(xmlConsumer, image, index);
            if (index % IMAGE_BY_ROW == IMAGE_BY_ROW - 1 || images.size() - 1 == index) {
                endDiv(xmlConsumer);
                generateDetailImage(xmlConsumer, index / IMAGE_BY_ROW);
            }
            index++;
        }
        generateSumaryResult(xmlConsumer, page, result, false);
    }
}
