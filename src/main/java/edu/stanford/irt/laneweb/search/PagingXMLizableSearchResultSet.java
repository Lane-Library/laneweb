package edu.stanford.irt.laneweb.search;

import java.util.ArrayList;
import java.util.TreeSet;

import org.apache.cocoon.xml.XMLUtils;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.laneweb.Resource;

public class PagingXMLizableSearchResultSet extends TreeSet<SearchResult> implements Resource, XMLizable {

    private static final String CDATA = "CDATA";

    private static final int DEFAULT_PAGE_SIZE = 100;

    private static final String LENGTH = "length";

    private static final int MAX_PAGE_COUNT = 4;

    private static final String PAGE = "page";

    private static final String PAGES = "pages";

    private static final long serialVersionUID = 1L;

    private static final String START = "start";

    private int page;

    private String query;

    public PagingXMLizableSearchResultSet(final String query, final int page) {
        this.query = query;
        this.page = page;
    }

    public void toSAX(final ContentHandler handler) throws SAXException {
        if (null == handler) {
            throw new IllegalArgumentException("null handler");
        }
        handler.startDocument();
        handler.startPrefixMapping("", NAMESPACE);
        int size = size();
        int pageSize = size / MAX_PAGE_COUNT;
        pageSize = size % MAX_PAGE_COUNT != 0 ? pageSize + 1 : pageSize;
        pageSize = pageSize < DEFAULT_PAGE_SIZE ? DEFAULT_PAGE_SIZE : pageSize;
        int start, length;
        if (this.page == -1 || size <= pageSize) {
            start = 0;
            length = size;
        } else {
            start = this.page * pageSize;
            length = size - start < pageSize ? size - start : pageSize;
        }
        int pages = size / pageSize;
        pages = size % pageSize != 0 ? pages + 1 : pages;
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, SIZE, SIZE, CDATA, Integer.toString(size));
        atts.addAttribute(EMPTY_NS, START, START, CDATA, Integer.toString(start));
        atts.addAttribute(EMPTY_NS, LENGTH, LENGTH, CDATA, Integer.toString(length));
        atts.addAttribute(EMPTY_NS, PAGE, PAGE, CDATA, Integer.toString(this.page));
        atts.addAttribute(EMPTY_NS, PAGES, PAGES, CDATA, Integer.toString(pages));
        XMLUtils.startElement(handler, NAMESPACE, RESOURCES, atts);
        XMLUtils.startElement(handler, NAMESPACE, QUERY);
        XMLUtils.data(handler, this.query);
        XMLUtils.endElement(handler, NAMESPACE, QUERY);
        handleSearchContentCounts(handler);
        int i = 0;
        int j = start + length;
        for (SearchResult result : this) {
            if (i >= start && i < j) {
                result.toSAX(handler);
            } else if (i == j) {
                break;
            }
            i++;
        }
        XMLUtils.endElement(handler, NAMESPACE, RESOURCES);
        handler.endPrefixMapping("");
        handler.endDocument();
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
