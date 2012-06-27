package edu.stanford.irt.laneweb.search;

import java.util.ArrayList;
import java.util.TreeSet;

import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.laneweb.resource.Resource;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class PagingXMLizableSearchResultSet extends TreeSet<SearchResult> implements Resource {

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

    public void toSAX(final XMLConsumer xmlConsumer) throws SAXException {
        if (null == xmlConsumer) {
            throw new IllegalArgumentException("null handler");
        }
        xmlConsumer.startDocument();
        xmlConsumer.startPrefixMapping("", NAMESPACE);
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
        XMLUtils.startElement(xmlConsumer, NAMESPACE, RESOURCES, atts);
        if (this.query != null) {
            XMLUtils.startElement(xmlConsumer, NAMESPACE, QUERY);
            XMLUtils.data(xmlConsumer, this.query);
            XMLUtils.endElement(xmlConsumer, NAMESPACE, QUERY);
        }
        handleSearchContentCounts(xmlConsumer);
        int i = 0;
        int j = start + length;
        for (SearchResult result : this) {
            if (i >= start && i < j) {
                result.toSAX(xmlConsumer);
            } else if (i == j) {
                break;
            }
            i++;
        }
        XMLUtils.endElement(xmlConsumer, NAMESPACE, RESOURCES);
        xmlConsumer.endPrefixMapping("");
        xmlConsumer.endDocument();
    }

    private void handleSearchContentCounts(final XMLConsumer xmlConsumer) throws SAXException {
        XMLUtils.startElement(xmlConsumer, NAMESPACE, CONTENT_HIT_COUNTS);
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
                    XMLUtils.startElement(xmlConsumer, NAMESPACE, RESOURCE, atts);
                    XMLUtils.endElement(xmlConsumer, NAMESPACE, RESOURCE);
                }
            }
        }
        XMLUtils.endElement(xmlConsumer, NAMESPACE, CONTENT_HIT_COUNTS);
    }
}
