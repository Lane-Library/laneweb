package edu.stanford.irt.laneweb.search;

import java.util.ArrayList;
import java.util.TreeSet;

import org.apache.cocoon.xml.XMLUtils;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.Resource;

public class PagingXMLizableSearchResultSet extends TreeSet<SearchResult> implements Resource, XMLizable {

    private static final String ALL = "all";

    private static final String CDATA = "CDATA";

    // private static final String LENGTH = "length";
    private static final String CURRENT_INDEX = "currentIndex";

    // private static final String PAGE = "page";
    // private static final String PAGE_SIZE = "pageSize";
    private static final int DEFAULT_PAGE_SIZE = 100;

    // private static final String START = "start";
    // private static final String TOTAL = "total";
    private static final int MAX_PAGE_COUNT = 4;

    private static final String PAGINATION = "pagination";

    private static final String RESULT_LIMIT = "resultLimit";

    private static final long serialVersionUID = 1L;

    private static final String SHOW = "show";

    private static final String SHOW_ALL = "showAll";

    private String query;

    private int show;

    // private int page;
    public PagingXMLizableSearchResultSet(final String query, final int show) {
        this.query = query;
        this.show = show;
    }

    public void toSAX(final ContentHandler handler) throws SAXException {
        if (null == handler) {
            throw new IllegalArgumentException("null handler");
        }
        handler.startPrefixMapping("", NAMESPACE);
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, SIZE, SIZE, "CDATA", Integer.toString(size()));
        XMLUtils.startElement(handler, NAMESPACE, RESOURCES, atts);
        XMLUtils.startElement(handler, NAMESPACE, QUERY);
        XMLUtils.data(handler, this.query);
        XMLUtils.endElement(handler, NAMESPACE, QUERY);
        handleSearchContentCounts(handler);
        handleResultsPage(handler);
        XMLUtils.endElement(handler, NAMESPACE, RESOURCES);
        handler.endPrefixMapping("");
    }

    private void handleResultsPage(final ContentHandler handler) throws SAXException {
        int total = size();
        int pageSize = total / MAX_PAGE_COUNT;
        pageSize = pageSize % MAX_PAGE_COUNT != 0 ? pageSize + 1 : pageSize;
        pageSize = pageSize < DEFAULT_PAGE_SIZE ? DEFAULT_PAGE_SIZE : pageSize;
        int start;
        if (this.show == -1 || total <= pageSize) {
            start = 0;
        } else {
            start = this.show;
        }
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, RESULT_LIMIT, RESULT_LIMIT, CDATA, Integer.toString(pageSize));
        if (total > pageSize) {
            atts.addAttribute(EMPTY_NS, SHOW, SHOW, CDATA, ALL);
        } else {
            atts.addAttribute(EMPTY_NS, SHOW, SHOW, CDATA, Integer.toString(start));
        }
        atts.addAttribute(EMPTY_NS, CURRENT_INDEX, CURRENT_INDEX, CDATA, Integer.toString(start));
        atts.addAttribute(EMPTY_NS, SHOW_ALL, SHOW_ALL, CDATA, Boolean.toString(total > pageSize));
        XMLUtils.createElementNS(handler, NAMESPACE, PAGINATION, atts);
        int i = 0;
        int j = start + pageSize;
        for (SearchResult result : this) {
            if (i >= start && i < j) {
                result.toSAX(handler);
            } else if (i == j) {
                break;
            }
            i++;
        }
    }

    private void handleSearchContentCounts(final ContentHandler handler) throws SAXException {
        XMLUtils.startElement(handler, NAMESPACE, CONTENT_HIT_COUNTS);
        ArrayList<String> countedResources = new ArrayList<String>();
        for (SearchResult resource : this) {
            if (resource instanceof ContentResultSearchResult) {
                ContentResultSearchResult cr = (ContentResultSearchResult) resource;
                if (null != cr.getResourceId() && !countedResources.contains(cr.getResourceId())) {
                    countedResources.add(cr.getResourceId());
                    AttributesImpl atts = new AttributesImpl();
                    atts.addAttribute(EMPTY_NS, RESOURCE_ID, RESOURCE_ID, "CDATA", cr.getResourceId());
                    atts.addAttribute(EMPTY_NS, RESOURCE_HITS, RESOURCE_HITS, "CDATA", cr.getResourceHits());
                    atts.addAttribute(EMPTY_NS, RESOURCE_URL, RESOURCE_URL, "CDATA", cr.getResourceUrl());
                    XMLUtils.startElement(handler, NAMESPACE, RESOURCE, atts);
                    XMLUtils.endElement(handler, NAMESPACE, RESOURCE);
                }
            }
        }
        XMLUtils.endElement(handler, NAMESPACE, CONTENT_HIT_COUNTS);
    }
}
