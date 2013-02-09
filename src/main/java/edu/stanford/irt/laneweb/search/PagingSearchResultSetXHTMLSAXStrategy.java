package edu.stanford.irt.laneweb.search;

import java.util.HashSet;
import java.util.Set;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.Resource;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.search.Result;

public class PagingSearchResultSetXHTMLSAXStrategy implements SAXStrategy<PagingSearchResultSet>, Resource {

    private static final String CDATA = "CDATA";

    private static final String CLASS = "class";

    private static final String DD = "dd";

    private static final int DEFAULT_PAGE_SIZE = 100;

    private static final String LENGTH = "length";

    private static final int MAX_PAGE_COUNT = 4;

    private static final String NO_PREFIX = "";

    private static final String PAGE = "page";

    private static final String PAGES = "pages";

    private static final String START = "start";

    private static final String UL = "ul";

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    private SAXStrategy<SearchResult> saxStrategy;

    public PagingSearchResultSetXHTMLSAXStrategy(final SAXStrategy<SearchResult> saxStrategy) {
        this.saxStrategy = saxStrategy;
    }

    public void toSAX(final PagingSearchResultSet results, final XMLConsumer xmlConsumer) {
        int page = results.getPage();
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping(NO_PREFIX, NAMESPACE);
            int size = results.size();
            int pageSize = size / MAX_PAGE_COUNT;
            pageSize = size % MAX_PAGE_COUNT != 0 ? pageSize + 1 : pageSize;
            pageSize = pageSize < DEFAULT_PAGE_SIZE ? DEFAULT_PAGE_SIZE : pageSize;
            int start, length;
            if (page == -1 || size <= pageSize) {
                start = 0;
                length = size;
            } else {
                start = page * pageSize;
                length = size - start < pageSize ? size - start : pageSize;
            }
            int pages = size / pageSize;
            pages = size % pageSize != 0 ? pages + 1 : pages;
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, SIZE, SIZE, CDATA, Integer.toString(size));
            atts.addAttribute(EMPTY_NS, START, START, CDATA, Integer.toString(start));
            atts.addAttribute(EMPTY_NS, LENGTH, LENGTH, CDATA, Integer.toString(length));
            atts.addAttribute(EMPTY_NS, PAGE, PAGE, CDATA, Integer.toString(page));
            atts.addAttribute(EMPTY_NS, PAGES, PAGES, CDATA, Integer.toString(pages));
            XMLUtils.startElement(xmlConsumer, NAMESPACE, RESOURCES, atts);
            String query = results.getQuery();
            if (query != null) {
                XMLUtils.startElement(xmlConsumer, NAMESPACE, QUERY);
                XMLUtils.data(xmlConsumer, query);
                XMLUtils.endElement(xmlConsumer, NAMESPACE, QUERY);
            }
            XMLUtils.startElement(xmlConsumer, NAMESPACE, CONTENT_HIT_COUNTS);
            Set<String> countedResources = new HashSet<String>();
            for (SearchResult resource : results) {
                if (resource instanceof ContentResultSearchResult) {
                    Result resourceResult = ((ContentResultSearchResult) resource).getResourceResult();
                    String id = resourceResult.getId();
                    if (null != id && !countedResources.contains(id)) {
                        countedResources.add(id);
                        atts = new AttributesImpl();
                        atts.addAttribute(EMPTY_NS, RESOURCE_ID, RESOURCE_ID, CDATA, id);
                        atts.addAttribute(EMPTY_NS, RESOURCE_HITS, RESOURCE_HITS, CDATA, resourceResult.getHits());
                        atts.addAttribute(EMPTY_NS, RESOURCE_URL, RESOURCE_URL, CDATA, resourceResult.getURL());
                        XMLUtils.startElement(xmlConsumer, NAMESPACE, RESOURCE, atts);
                        XMLUtils.endElement(xmlConsumer, NAMESPACE, RESOURCE);
                    }
                }
            }
            XMLUtils.endElement(xmlConsumer, NAMESPACE, CONTENT_HIT_COUNTS);
            int i = 0;
            int j = start + length;
            xmlConsumer.startPrefixMapping(NO_PREFIX, XHTML_NS);
            for (SearchResult result : results) {
                if (i >= start && i < j) {
                    XMLUtils.startElement(xmlConsumer, XHTML_NS, DD);
                    StringBuilder sb = new StringBuilder("r-");
                    sb.append(start + 1 + i);
                    atts = new AttributesImpl();
                    atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, sb.toString());
                    XMLUtils.startElement(xmlConsumer, XHTML_NS, UL, atts);
                    xmlConsumer.startPrefixMapping(NO_PREFIX, NAMESPACE);
                    this.saxStrategy.toSAX(result, xmlConsumer);
                    xmlConsumer.endPrefixMapping(NO_PREFIX);
                    XMLUtils.endElement(xmlConsumer, XHTML_NS, UL);
                    XMLUtils.endElement(xmlConsumer, XHTML_NS, DD);
                } else if (i == j) {
                    break;
                }
                i++;
            }
            xmlConsumer.endPrefixMapping(NO_PREFIX);
            XMLUtils.endElement(xmlConsumer, NAMESPACE, RESOURCES);
            xmlConsumer.endPrefixMapping(NO_PREFIX);
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
