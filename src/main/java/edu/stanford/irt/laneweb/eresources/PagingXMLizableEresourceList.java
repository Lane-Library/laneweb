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

    private static final String LENGTH = "length";

    private static final int MAX_PAGE_COUNT = 4;

    private static final long serialVersionUID = 1L;
    
    private static final String PAGE = "page";

    private static final String START = "start";

    private static final String TOTAL = "total";

    private int start;

    private int total;
    
    private int page;

    public PagingXMLizableEresourceList(final Collection<Eresource> eresources) {
        this(eresources, 0);
    }

    public PagingXMLizableEresourceList(final Collection<Eresource> eresources, final int page) {
        this.page = page;
        int size = eresources.size();
        int pageSize = size / MAX_PAGE_COUNT;
        pageSize = pageSize % MAX_PAGE_COUNT != 0 ? pageSize + 1 : pageSize;
        pageSize = pageSize < DEFAULT_PAGE_SIZE ? DEFAULT_PAGE_SIZE : pageSize;
        int start = (page - 1) * pageSize;
        this.total = eresources.size();
        this.start = start;
        int i = 0;
        int j = start + pageSize;
        for (Eresource eresource : eresources) {
            if (i >= start && i < j) {
                add(eresource);
            } else if (i == j) {
                break;
            }
            i++;
        }
    }

    public void toSAX(final ContentHandler handler) throws SAXException {
        if (null == handler) {
            throw new IllegalArgumentException("null handler");
        }
        handler.startDocument();
        handler.startPrefixMapping("", NAMESPACE);
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, TOTAL, TOTAL, CDATA, Integer.toString(this.total));
        atts.addAttribute(EMPTY_NS, START, START, CDATA, Integer.toString(this.start));
        atts.addAttribute(EMPTY_NS, LENGTH, LENGTH, CDATA, Integer.toString(this.size()));
        atts.addAttribute(EMPTY_NS, PAGE, PAGE, CDATA, Integer.toString(this.page));
        XMLUtils.startElement(handler, NAMESPACE, RESOURCES, atts);
        for (Eresource eresource : this) {
            new EresourceResource(eresource).toSAX(handler);
        }
        XMLUtils.endElement(handler, NAMESPACE, RESOURCES);
        handler.endPrefixMapping("");
        handler.endDocument();
    }
}
