package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.LinkedList;
import java.util.ListIterator;

import org.apache.cocoon.xml.XMLUtils;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.Resource;

public class PagingXMLizableEresourceList extends LinkedList<Eresource> implements XMLizable, Resource {

//    private static final String ALL = "all";

    private static final String CDATA = "CDATA";

//    private static final String CURRENT_INDEX = "currentIndex";

    private static final int DEFAULT_PAGE_SIZE = 100;

    private static final String LENGTH = "length";

    private static final int MAX_PAGE_COUNT = 4;

//    private static final String PAGINATION = "pagination";

//    private static final String RESULT_LIMIT = "resultLimit";

    private static final long serialVersionUID = 1L;

//    private static final String SHOW = "show";

//    private static final String SHOW_ALL = "showAll";

    private static final String START = "start";

//    private int page;

    private int pageSize;

    private int start;

    private int size;

    private int length;

    public PagingXMLizableEresourceList(final Collection<Eresource> eresources) {
        this(eresources, 0);
    }

    // public PagingXMLizableEresourceList(final Collection<Eresource>
    // eresources, final int page) {
    // this.page = page;
    // int size = eresources.size();
    // this.pageSize = size / MAX_PAGE_COUNT;
    // this.pageSize = this.pageSize % MAX_PAGE_COUNT != 0 ? this.pageSize + 1 :
    // this.pageSize;
    // this.pageSize = this.pageSize < DEFAULT_PAGE_SIZE ? DEFAULT_PAGE_SIZE :
    // this.pageSize;
    // int start = (page - 1) * this.pageSize;
    // this.total = eresources.size();
    // this.start = start;
    // int i = 0;
    // int j = start + this.pageSize;
    // for (Eresource eresource : eresources) {
    // if (i >= start && i < j) {
    // add(eresource);
    // } else if (i == j) {
    // break;
    // }
    // i++;
    // }
    // }
    public PagingXMLizableEresourceList(final Collection<Eresource> eresources, final int show) {
        super(eresources);
        this.size = eresources.size();
        this.pageSize = this.size / MAX_PAGE_COUNT;
        this.pageSize = this.pageSize % MAX_PAGE_COUNT != 0 ? this.pageSize + 1 : this.pageSize;
        this.pageSize = this.pageSize < DEFAULT_PAGE_SIZE ? DEFAULT_PAGE_SIZE : this.pageSize;
        if (show == -1 || this.size <= this.pageSize) {
            this.start = 0;
            this.length = this.size;
        } else {
            this.start = show;
            this.length = this.size - this.start < this.pageSize ? this.size - this.start : this.pageSize;
        }
    }

    public void toSAX(final ContentHandler handler) throws SAXException {
        if (null == handler) {
            throw new IllegalArgumentException("null handler");
        }
        handler.startDocument();
        handler.startPrefixMapping("", NAMESPACE);
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, SIZE, SIZE, CDATA, Integer.toString(this.size));
        atts.addAttribute(EMPTY_NS, START, START, CDATA, Integer.toString(this.start));
        atts.addAttribute(EMPTY_NS, LENGTH, LENGTH, CDATA, Integer.toString(this.length));
        XMLUtils.startElement(handler, NAMESPACE, RESOURCES, atts);
        int i = 0;
        for (ListIterator<Eresource> it = listIterator(this.start); it.hasNext() && i < this.length; i++) {
            new EresourceResource(it.next()).toSAX(handler);
        }
        XMLUtils.endElement(handler, NAMESPACE, RESOURCES);
        handler.endPrefixMapping("");
        handler.endDocument();
    }
}
