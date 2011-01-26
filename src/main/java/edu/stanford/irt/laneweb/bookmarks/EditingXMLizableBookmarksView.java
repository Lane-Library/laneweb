package edu.stanford.irt.laneweb.bookmarks;

import org.apache.cocoon.xml.XMLUtils;
import org.springframework.stereotype.Component;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

@Component
public class EditingXMLizableBookmarksView extends DefaultXMLizableBookmarksView {

    private static final String STYLE = "style";

    @Override
    protected void createBookmarksUL(final Bookmarks bookmarks, final ContentHandler contentHandler, final int formPosition) throws SAXException {
        XMLUtils.startElement(contentHandler, XHTMLNS, UL);
        int i = 0;
        for (Bookmark bookmark : bookmarks) {
            if (i == formPosition) {
                XMLUtils.createElementNS(contentHandler, XHTMLNS, LI, "new item would go here");
            }
            XMLUtils.startElement(contentHandler, XHTMLNS, LI);
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", HREF, HREF, CDATA, bookmark.getUrl());
            XMLUtils.createElementNS(contentHandler, XHTMLNS, A, atts, bookmark.getLabel());
            atts = new AttributesImpl();
            atts.addAttribute("", HREF, HREF, CDATA, "?action=delete&position=" + i);
            atts.addAttribute("", CLASS, STYLE, STYLE, "float:right");
            XMLUtils.createElementNS(contentHandler, XHTMLNS, A, atts, "delete");
            if (bookmarks.size() > 1 && i < bookmarks.size() - 1) {
                atts = new AttributesImpl();
                atts.addAttribute("", HREF, HREF, CDATA, "?action=down&position=" + i);
                atts.addAttribute("", CLASS, STYLE, STYLE, "float:right");
                XMLUtils.createElementNS(contentHandler, XHTMLNS, A, atts, "down");
            }
            if (bookmarks.size() > 1 && i > 0) {
                atts = new AttributesImpl();
                atts.addAttribute("", HREF, HREF, CDATA, "?action=up&position=" + i);
                atts.addAttribute("", CLASS, STYLE, STYLE, "float:right");
                XMLUtils.createElementNS(contentHandler, XHTMLNS, A, atts, "up");
            }
            atts = new AttributesImpl();
            atts.addAttribute("", HREF, HREF, CDATA, "?action=insertBefore&position=" + i);
            atts.addAttribute("", CLASS, STYLE, STYLE, "float:right");
            XMLUtils.createElementNS(contentHandler, XHTMLNS, A, atts, "insertBefore");
            atts = new AttributesImpl();
            atts.addAttribute("", HREF, HREF, CDATA, "?action=insertAfter&position=" + i);
            atts.addAttribute("", CLASS, STYLE, STYLE, "float:right");
            XMLUtils.createElementNS(contentHandler, XHTMLNS, A, atts, "insertAfter");
            i++;
            XMLUtils.endElement(contentHandler, XHTMLNS, LI);
        }
        if (i != 0 && i == formPosition) {
            XMLUtils.createElementNS(contentHandler, XHTMLNS, LI, "new item would go here");
        }
        XMLUtils.endElement(contentHandler, XHTMLNS, UL);
    }

    @Override
    protected String getEditHref() {
        return "bookmarks.html";
    }

    @Override
    protected String getEditLabel() {
        return "done";
    }
}
