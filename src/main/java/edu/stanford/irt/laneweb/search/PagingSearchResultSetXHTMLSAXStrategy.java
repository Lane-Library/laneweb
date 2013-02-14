package edu.stanford.irt.laneweb.search;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.search.ContentResult;

public class PagingSearchResultSetXHTMLSAXStrategy implements SAXStrategy<PagingSearchResultSet> {

    private static final String CDATA = "CDATA";

    private static final String CLASS = "class";

    private static final String DD = "dd";

    private static final int DEFAULT_PAGE_SIZE = 100;

    private static final String EMPTY_NS = "";

    private static final int MAX_PAGE_COUNT = 4;

    private static final String NO_PREFIX = "";

    private static final String UL = "ul";

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    private SAXStrategy<SearchResult> saxStrategy;

    public PagingSearchResultSetXHTMLSAXStrategy(final SAXStrategy<SearchResult> saxStrategy) {
        this.saxStrategy = saxStrategy;
    }

    //TODO: use a PagingData object
    public void toSAX(final PagingSearchResultSet results, final XMLConsumer xmlConsumer) {
        int page = results.getPage();
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping(NO_PREFIX, XHTML_NS);
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
//            int pages = size / pageSize;
//            pages = size % pageSize != 0 ? pages + 1 : pages;
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "html");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "head");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "title");
            XMLUtils.data(xmlConsumer, "search results");
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "title");
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "head");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "body");
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "lwSearchResults");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "dl", atts);
            int i = 0;
            int j = start + length;
            for (SearchResult result : results) {
                if (i >= start && i < j) {
                    XMLUtils.startElement(xmlConsumer, XHTML_NS, DD);
                    StringBuilder sb = new StringBuilder("r-");
                    sb.append(start + 1 + i);
                    if (isHvrTrig(result)) {
                        sb.append(" hvrTrig");
                    }
                    atts = new AttributesImpl();
                    atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, sb.toString());
                    XMLUtils.startElement(xmlConsumer, XHTML_NS, UL, atts);
                    this.saxStrategy.toSAX(result, xmlConsumer);
                    XMLUtils.endElement(xmlConsumer, XHTML_NS, UL);
                    XMLUtils.endElement(xmlConsumer, XHTML_NS, DD);
                } else if (i == j) {
                    break;
                }
                i++;
            }
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "dl");
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "body");
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "html");
            xmlConsumer.endPrefixMapping(NO_PREFIX);
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private boolean isHvrTrig(SearchResult result) {
        String description = null;
        if (result instanceof EresourceSearchResult) {
            Eresource eresource = ((EresourceSearchResult) result).getEresource();
            description = eresource.getDescription();
        } else if (result instanceof ContentResultSearchResult) {
            ContentResult contentResult = ((ContentResultSearchResult) result).getContentResult();
            description = contentResult.getDescription();
        }
        return description != null && description.length() > 0;
    }
}
