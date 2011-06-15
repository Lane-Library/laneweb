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

    static final String HREF = "href";

    static final String ID = "id";

    static final String LI = "li";

    static final String UL = "ul";

    static final String XHTMLNS = "http://www.w3.org/1999/xhtml";

    public void toSAX(final Bookmarks bookmarks, final ContentHandler contentHandler) throws SAXException {
        contentHandler.startDocument();
        maybeCreateBookmarksUL(bookmarks, contentHandler);
        contentHandler.endDocument();
    }

    protected void maybeCreateBookmarksUL(final Bookmarks bookmarks, final ContentHandler contentHandler) throws SAXException {
        XMLUtils.startElement(contentHandler, XHTMLNS, UL);
        if (bookmarks != null) {
            for (Bookmark bookmark : bookmarks) {
                XMLUtils.startElement(contentHandler, XHTMLNS, LI);
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", HREF, HREF, CDATA, bookmark.getUrl());
                XMLUtils.createElementNS(contentHandler, XHTMLNS, A, atts, bookmark.getLabel());
                XMLUtils.endElement(contentHandler, XHTMLNS, LI);
            }
        }
        XMLUtils.endElement(contentHandler, XHTMLNS, UL);
    }
}