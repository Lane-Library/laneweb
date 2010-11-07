package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.LinkedList;

import org.apache.cocoon.xml.XMLUtils;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.Resource;

public class PagingXMLizableEresourceList extends LinkedList<Eresource> implements XMLizable, Resource {

    private static final String CDATA = "CDATA";

    private static final int DEFAULT_PAGE_SIZE = 100;

//    private static final String LENGTH = "length";

    private static final int MAX_PAGE_COUNT = 4;

//    private static final String PAGE = "page";

//    private static final String PAGE_SIZE = "pageSize";

    private static final long serialVersionUID = 1L;

//    private static final String START = "start";

//    private static final String TOTAL = "total";

    private static final String PAGINATION = "pagination";

private static final String RESULT_LIMIT = "resultLimit";

private static final String SHOW = "show";

private static final String SHOW_ALL = "showAll";

private static final String CURRENT_INDEX = "currentIndex";

private static final String ALL = "all";

//    private int page;

    private int pageSize;

    private int start;

    private int total;

    public PagingXMLizableEresourceList(final Collection<Eresource> eresources) {
        this(eresources, 0);
    }

//    public PagingXMLizableEresourceList(final Collection<Eresource> eresources, final int page) {
//        this.page = page;
//        int size = eresources.size();
//        this.pageSize = size / MAX_PAGE_COUNT;
//        this.pageSize = this.pageSize % MAX_PAGE_COUNT != 0 ? this.pageSize + 1 : this.pageSize;
//        this.pageSize = this.pageSize < DEFAULT_PAGE_SIZE ? DEFAULT_PAGE_SIZE : this.pageSize;
//        int start = (page - 1) * this.pageSize;
//        this.total = eresources.size();
//        this.start = start;
//        int i = 0;
//        int j = start + this.pageSize;
//        for (Eresource eresource : eresources) {
//            if (i >= start && i < j) {
//                add(eresource);
//            } else if (i == j) {
//                break;
//            }
//            i++;
//        }
//    }
    
    public PagingXMLizableEresourceList(final Collection<Eresource> eresources, final int show) {
        this.total = eresources.size();
        this.pageSize = this.total / MAX_PAGE_COUNT;
        this.pageSize = this.pageSize % MAX_PAGE_COUNT != 0 ? this.pageSize + 1 : this.pageSize;
        this.pageSize = this.pageSize < DEFAULT_PAGE_SIZE ? DEFAULT_PAGE_SIZE : this.pageSize;
        if (show == -1 || this.total <= this.pageSize) {
            addAll(eresources);
            this.start = 0;
        } else {
            this.start = show;
            int i = 0;
            int j = this.start + this.pageSize;
            for (Eresource eresource : eresources) {
                if (i >= this.start && i < j) {
                    add(eresource);
                } else if (i == j) {
                    break;
                }
                i++;
            }
        }
    }

    public void toSAX(final ContentHandler handler) throws SAXException {
        if (null == handler) {
            throw new IllegalArgumentException("null handler");
        }
        handler.startDocument();
        handler.startPrefixMapping("", NAMESPACE);
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, SIZE, SIZE, "CDATA", Integer.toString(this.total));
//        atts.addAttribute(EMPTY_NS, TOTAL, TOTAL, CDATA, Integer.toString(this.total));
//        atts.addAttribute(EMPTY_NS, START, START, CDATA, Integer.toString(this.start));
//        atts.addAttribute(EMPTY_NS, LENGTH, LENGTH, CDATA, Integer.toString(this.size()));
//        atts.addAttribute(EMPTY_NS, PAGE, PAGE, CDATA, Integer.toString(this.page));
//        atts.addAttribute(EMPTY_NS, PAGE_SIZE, PAGE_SIZE, CDATA, Integer.toString(this.pageSize));
        XMLUtils.startElement(handler, NAMESPACE, RESOURCES, atts);
        atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, RESULT_LIMIT, RESULT_LIMIT, CDATA, Integer.toString(this.pageSize));
        if (this.total == this.size()) {
            atts.addAttribute(EMPTY_NS, SHOW, SHOW, CDATA, ALL);
        } else {
            atts.addAttribute(EMPTY_NS, SHOW, SHOW, CDATA, Integer.toString(this.start));
        }
        atts.addAttribute(EMPTY_NS, CURRENT_INDEX, CURRENT_INDEX, CDATA, Integer.toString(this.start));
        atts.addAttribute(EMPTY_NS, SHOW_ALL, SHOW_ALL, CDATA, Boolean.toString(this.total != this.size()));
        XMLUtils.createElementNS(handler, NAMESPACE, PAGINATION, atts);
        for (Eresource eresource : this) {
            new EresourceResource(eresource).toSAX(handler);
        }
        XMLUtils.endElement(handler, NAMESPACE, RESOURCES);
        handler.endPrefixMapping("");
        handler.endDocument();
    }
}
