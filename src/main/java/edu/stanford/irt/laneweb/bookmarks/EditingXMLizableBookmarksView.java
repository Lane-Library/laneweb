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
                createAddForm(contentHandler, i);
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
            createAddForm(contentHandler, i);
        }
        XMLUtils.endElement(contentHandler, XHTMLNS, UL);
    }
    
    private void createAddForm(ContentHandler contentHandler, int position) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute("", "method", "method", CDATA, "get");
        XMLUtils.startElement(contentHandler, XHTMLNS, "form", atts);
        atts = new AttributesImpl();
        atts.addAttribute("", "name", "name", CDATA, "url");
        XMLUtils.createElementNS(contentHandler, XHTMLNS, "input", atts);
        atts = new AttributesImpl();
        atts.addAttribute("", "name", "name", CDATA, "label");
        XMLUtils.createElementNS(contentHandler, XHTMLNS, "input", atts);
        atts = new AttributesImpl();
        atts.addAttribute("", "name", "name", CDATA, "action");
        atts.addAttribute("", "value", "value", CDATA, "add");
        atts.addAttribute("", "type", "type", CDATA, "hidden");
        XMLUtils.createElementNS(contentHandler, XHTMLNS, "input", atts);
        atts = new AttributesImpl();
        atts.addAttribute("", "name", "name", CDATA, "position");
        atts.addAttribute("", "value", "value", CDATA, Integer.toString(position));
        atts.addAttribute("", "type", "type", CDATA, "hidden");
        XMLUtils.createElementNS(contentHandler, XHTMLNS, "input", atts);
        atts = new AttributesImpl();
        atts.addAttribute("", "type", "type", CDATA, "submit");
        XMLUtils.createElementNS(contentHandler, XHTMLNS, "input", atts);
        XMLUtils.endElement(contentHandler, XHTMLNS, "form");
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
