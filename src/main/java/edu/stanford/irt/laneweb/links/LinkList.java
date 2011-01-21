package edu.stanford.irt.laneweb.links;

import java.util.LinkedList;

import org.apache.cocoon.xml.XMLUtils;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.eresources.Link;

public class LinkList extends LinkedList<Link> implements XMLizable {

    private static final String A = "a";

    private static final String CDATA = "CDATA";

    private static final String HREF = "href";

    private static final String LI = "li";

    private static final long serialVersionUID = 1L;

    private static final String UL = "ul";

    private static final String XHTMLNS = "http://www.w3.org/1999/xhtml";

    public void toSAX(final ContentHandler handler) throws SAXException {
        handler.startDocument();
        if (size() > 0) {
            XMLUtils.startElement(handler, XHTMLNS, UL);
            for (Link link : this) {
                XMLUtils.startElement(handler, XHTMLNS, LI);
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", HREF, HREF, CDATA, link.getUrl());
                XMLUtils.createElementNS(handler, XHTMLNS, A, atts, link.getLabel());
                XMLUtils.endElement(handler, XHTMLNS, LI);
            }
            XMLUtils.endElement(handler, XHTMLNS, UL);
        }
        handler.endDocument();
    }
}
