package edu.stanford.irt.laneweb.bookmarks;

import org.apache.cocoon.xml.XMLUtils;
import org.springframework.stereotype.Component;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

@Component
public class DefaultXMLizableBookmarksView {

    private static final long serialVersionUID = 1L;

    static final String A = "a";

    static final String CDATA = "CDATA";

    static final String CLASS = "class";

    static final String DIV = "div";

    static final String H3 = "h3";

    static final String HREF = "href";

    static final String ID = "id";

    static final String LI = "li";

    static final String UL = "ul";

    static final String XHTMLNS = "http://www.w3.org/1999/xhtml";

    public void toSAX(final Bookmarks bookmarks, final ContentHandler contentHandler) throws SAXException {
        contentHandler.startDocument();
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute("", CLASS, CLASS, CDATA, "module");
        atts.addAttribute("", ID, ID, ID, "bookmarks");
        XMLUtils.startElement(contentHandler, XHTMLNS, DIV, atts);
        XMLUtils.startElement(contentHandler, XHTMLNS, H3);
        String href = getEditHref();
        String label = getEditLabel();
        atts = new AttributesImpl();
        atts.addAttribute("", HREF, HREF, CDATA, href);
        XMLUtils.createElementNS(contentHandler, XHTMLNS, A, atts, label);
        XMLUtils.data(contentHandler, "bookmarks");
        XMLUtils.endElement(contentHandler, XHTMLNS, H3);
        atts = new AttributesImpl();
        atts.addAttribute("", CLASS, CLASS, CDATA, "bd");
        XMLUtils.startElement(contentHandler, XHTMLNS, DIV, atts);
        maybeCreateBookmarksUL(bookmarks, contentHandler);
        XMLUtils.endElement(contentHandler, XHTMLNS, DIV);
        XMLUtils.endElement(contentHandler, XHTMLNS, DIV);
        contentHandler.endDocument();
    }

    protected String getEditHref() {
        return "edit-bookmarks.html";
    }

    protected String getEditLabel() {
        return "edit";
    }

    protected void maybeCreateBookmarksUL(final Bookmarks bookmarks, final ContentHandler contentHandler) throws SAXException {
        if (bookmarks.size() > 0) {
            XMLUtils.startElement(contentHandler, XHTMLNS, UL);
            for (Bookmark bookmark : bookmarks) {
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", CLASS, CLASS, CDATA, "bookmark");
                XMLUtils.startElement(contentHandler, XHTMLNS, LI, atts);
                atts = new AttributesImpl();
                atts.addAttribute("", HREF, HREF, CDATA, bookmark.getUrl());
                XMLUtils.createElementNS(contentHandler, XHTMLNS, A, atts, bookmark.getLabel());
                XMLUtils.endElement(contentHandler, XHTMLNS, LI);
            }
            XMLUtils.endElement(contentHandler, XHTMLNS, UL);
        }
    }
}