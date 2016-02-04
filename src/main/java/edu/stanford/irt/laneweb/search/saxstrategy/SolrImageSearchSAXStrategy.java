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
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.solr.Image;

public class SolrImageSearchSAXStrategy extends AbstractXHTMLSAXStrategy<Map<String, Object>> {

    protected static final String ACTIVED = "actived";

    protected static final String ANCHOR = "a";

    protected static final String BUTTON = "button";

    protected static final String CDATA = "CDATA";

    protected static final String CLASS = "class";

    protected static final String DIV = "div";

    protected static final String HIDDEN = "hidden";

    protected static final String HREF = "href";

    protected static final String ID = "id";

    protected static final String IMAGE = "image";

    protected static final int IMAGE_BY_ROW = 4;

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

    protected static final String INPUT = "input";

    private NumberFormat nf = NumberFormat.getInstance();

    private Map<String, String> websiteIdMapping;

    public Map<String, String> getWebsiteIdMapping() {
        return this.websiteIdMapping;
    }

    public void setWebsiteIdMapping(final Map<String, String> websiteIdMapping) {
        this.websiteIdMapping = websiteIdMapping;
    }

    @Override
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

    protected void generateImages(final XMLConsumer xmlConsumer, final Image image, final int imageNumber)
            throws SAXException {
        AttributesImpl atts = new AttributesImpl();
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
        atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "imagedecoHidden");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
        XMLUtils.data(xmlConsumer, " ");
        endAnchor(xmlConsumer);
        endDiv(xmlConsumer);
        endLi(xmlConsumer);
    }

    private void createBackwardLink(final XMLConsumer xmlConsumer, final String faClass, final String text,
            final String clazz, final String href) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, clazz + " " + NO_BOOKMARKING);
        if (href != null) {
            atts.addAttribute(XHTML_NS, HREF, HREF, CDATA, href);
        }
        XMLUtils.startElement(xmlConsumer, XHTML_NS, ANCHOR, atts);
        atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, faClass + " " + NO_BOOKMARKING);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, SPAN, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, SPAN);
        XMLUtils.data(xmlConsumer, " " + text);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, ANCHOR);
    }

    private void createForwardLink(final XMLConsumer xmlConsumer, final String faClass, final String text,
            final String clazz, final String href) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, clazz + " " + NO_BOOKMARKING);
        if (href != null) {
            atts.addAttribute(XHTML_NS, HREF, HREF, CDATA, href);
        }
        XMLUtils.startElement(xmlConsumer, XHTML_NS, ANCHOR, atts);
        XMLUtils.data(xmlConsumer, text + " ");
        atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, faClass + " " + NO_BOOKMARKING);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, SPAN, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, SPAN);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, ANCHOR);
    }

    private void generateDetailImage(final XMLConsumer xmlConsumer, final int rowNumber) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "imageDetailHidden");
        atts.addAttribute(XHTML_NS, STYLE, STYLE, CDATA, "width:935px; height:350px;");
        atts.addAttribute(XHTML_NS, ID, ID, CDATA, "imageDetail_" + rowNumber);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, "li", atts);
        atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, ID, ID, CDATA, "image-detail-close");
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "image-detail-close close fa fa-close");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
        endDiv(xmlConsumer);
        startDivWithClass(xmlConsumer, "yui3-g");
        startDivWithClass(xmlConsumer, "yui3-u-3-5");
        startDivWithClass(xmlConsumer, "image-location");
        atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "image");
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
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, BUTTON);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, BUTTON, atts);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, SPAN, atts);
        XMLUtils.data(xmlConsumer, "Visit Source Page ");
        XMLUtils.endElement(xmlConsumer, XHTML_NS, SPAN);
        atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "fa fa-arrow-right");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, "i", atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, "i");
        XMLUtils.endElement(xmlConsumer, XHTML_NS, BUTTON);
        endAnchor(xmlConsumer);
        endDiv(xmlConsumer);
        endDiv(xmlConsumer);
        endDiv(xmlConsumer);
        endLi(xmlConsumer);
    }

    private void generateDirectAccessPageForm(final XMLConsumer xmlConsumer, final Page<Image> page,
            final Map<String, Object> result) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "pagingForm");
        atts.addAttribute(XHTML_NS, NAME, NAME, CDATA, "paginationForm");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, "form", atts);
        atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, TYPE, TYPE, CDATA, HIDDEN);
        atts.addAttribute(XHTML_NS, VALUE, VALUE, CDATA, (String) result.get(Model.SOURCE));
        atts.addAttribute(XHTML_NS, NAME, NAME, CDATA, "source");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, INPUT, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, INPUT);
        atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, TYPE, TYPE, CDATA, HIDDEN);
        atts.addAttribute(XHTML_NS, VALUE, VALUE, CDATA, (String) result.get(Model.QUERY));
        atts.addAttribute(XHTML_NS, NAME, NAME, CDATA, "q");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, INPUT, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, INPUT);
        atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, TYPE, TYPE, CDATA, HIDDEN);
        atts.addAttribute(XHTML_NS, VALUE, VALUE, CDATA, String.valueOf(page.getTotalPages()));
        atts.addAttribute(XHTML_NS, NAME, NAME, CDATA, "totalPages");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, INPUT, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, INPUT);
        if (result.get(SELECTED_RESOURCE) != null && !"".equals(result.get(SELECTED_RESOURCE))) {
            atts = new AttributesImpl();
            atts.addAttribute(XHTML_NS, TYPE, TYPE, CDATA, HIDDEN);
            atts.addAttribute(XHTML_NS, VALUE, VALUE, CDATA, (String) result.get(SELECTED_RESOURCE));
            atts.addAttribute(XHTML_NS, NAME, NAME, CDATA, "rid");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, INPUT, atts);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, INPUT);
        }
        atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, TYPE, TYPE, CDATA, "text");
        atts.addAttribute(XHTML_NS, "size", "size", CDATA, "4");
        atts.addAttribute(XHTML_NS, VALUE, VALUE, CDATA, String.valueOf(page.getNumber() + 1));
        atts.addAttribute(XHTML_NS, NAME, NAME, CDATA, "page");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, INPUT, atts);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, INPUT);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, "form");
    }

    private void generateFilterWebsiteIdOptions(final XMLConsumer xmlConsumer, final Map<String, Object> result)
            throws SAXException {
        @SuppressWarnings("unchecked")
        Page<FacetFieldEntry> facet = (Page<FacetFieldEntry>) result.get("websiteIdFacet");
        int totalFacet = facet.getNumberOfElements();
        if (totalFacet > 0) {
            String path = (String) result.get("path");
            startDivWithClass(xmlConsumer, "yui3-u-1-4");
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "view-by");
            atts.addAttribute(XHTML_NS, ID, ID, CDATA, "sourceFilter");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
            List<FacetFieldEntry> facetList = facet.getContent();
            XMLUtils.data(xmlConsumer, "Filter by source ");
            atts = new AttributesImpl();
            atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "general-dropdown dropdown");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
            atts = new AttributesImpl();
            atts.addAttribute(XHTML_NS, CLASS, CLASS, CDATA, "general-dropdown-trigger");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
            String selectedResource = "All";
            if (totalFacet == 1) {
                selectedResource = facetList.get(0).getValue();
            }
            int totalElement = 0;
            int totalSelectedFacet = 0;
            if (result.get(SELECTED_RESOURCE) != null && !"".equals(result.get(SELECTED_RESOURCE))) {
                selectedResource = (String) result.get(SELECTED_RESOURCE);
            }
            for (FacetFieldEntry facetFieldEntry : facetList) {
                totalElement = totalElement + (int) facetFieldEntry.getValueCount();
                if (selectedResource.equals(facetFieldEntry.getValue())) {
                    totalSelectedFacet = (int) facetFieldEntry.getValueCount();
                }
            }
            if (!"All".equals(selectedResource)) {
                XMLUtils.data(xmlConsumer,
                        getDisplayedResourceName(selectedResource) + " (" + this.nf.format(totalSelectedFacet) + ")");
            } else {
                XMLUtils.data(xmlConsumer,
                        getDisplayedResourceName(selectedResource) + " (" + this.nf.format(totalElement) + ")");
            }
            createElementWithClass(xmlConsumer, "i", "fa fa-angle-double-down", "");
            endDiv(xmlConsumer);
            startDivWithClass(xmlConsumer, "general-dropdown-content dropdown-content");
            startUlWithClass(xmlConsumer, "pagingLabels");
            if (!"All".equals(selectedResource) && totalFacet > 1) {
                startLi(xmlConsumer);
                startAnchor(xmlConsumer, path);
                XMLUtils.data(xmlConsumer, "All (" + this.nf.format(totalElement) + ")");
                endAnchor(xmlConsumer);
                endLi(xmlConsumer);
            }
            for (FacetFieldEntry facetFieldEntry : facetList) {
                startLi(xmlConsumer);
                startAnchor(xmlConsumer, path + "&rid=" + facetFieldEntry.getValue());
                XMLUtils.data(xmlConsumer, getDisplayedResourceName(facetFieldEntry.getValue()) + " ("
                        + this.nf.format(facetFieldEntry.getValueCount()) + ") ");
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
            final Map<String, Object> result) throws SAXException {
        String path = (String) result.get("path");
        if (result.get(SELECTED_RESOURCE) != null && !"".equals(result.get(SELECTED_RESOURCE))) {
            path = path + "&rid=" + (String) result.get(SELECTED_RESOURCE);
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

    private void generateResult(final XMLConsumer xmlConsumer, final Page<Image> page, final Map<String, Object> result)
            throws SAXException {
        String numberResult = String.valueOf(page.getSize() * page.getNumber() + 1);
        String number = String.valueOf(page.getSize() * page.getNumber() + page.getNumberOfElements());
        startDivWithClass(xmlConsumer, "result");
        if (page.hasContent()) {
            XMLUtils.data(xmlConsumer, numberResult + " to " + number + " of " + page.getTotalElements() + " results");
        } else {
            XMLUtils.data(xmlConsumer,
                    "No " + result.get("tab") + " images are available with search term: " + result.get(Model.QUERY));
        }
        endDiv(xmlConsumer);
    }

    private void generateSumaryResult(final XMLConsumer xmlConsumer, final Page<Image> page,
            final Map<String, Object> result, final boolean isTopScreen) throws SAXException {
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

    private void startElementWithId(final XMLConsumer xmlConsumer, final String name, final String id)
            throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(XHTML_NS, ID, ID, CDATA, id == null ? "" : id);
        XMLUtils.startElement(xmlConsumer, XHTML_NS, name, atts);
    }

    private void toSAXResult(final Map<String, Object> result, final XMLConsumer xmlConsumer) throws SAXException {
        @SuppressWarnings("unchecked")
        Page<Image> page = (Page<Image>) result.get("page");
        List<Image> images = page.getContent();
        generateSumaryResult(xmlConsumer, page, result, true);
        startElementWithId(xmlConsumer, UL, "imageList");
        int index = 0;
        for (Image image : images) {
            generateImages(xmlConsumer, image, index);
            if (index % IMAGE_BY_ROW == 3 || images.size() - 1 == index) {
                generateDetailImage(xmlConsumer, index / IMAGE_BY_ROW);
            }
            index++;
        }
        endUl(xmlConsumer);
        generateSumaryResult(xmlConsumer, page, result, false);
    }
}
