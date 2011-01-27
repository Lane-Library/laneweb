package edu.stanford.irt.laneweb.bookmarks;

import org.apache.cocoon.xml.XMLUtils;
import org.springframework.stereotype.Component;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

@Component
public class EditingXMLizableBookmarksView extends DefaultXMLizableBookmarksView {

    private static final String FORM = "form";

    private static final String INPUT = "input";

    private static final String METHOD = "method";

    private static final String NAME = "name";

    private static final String TYPE = "type";

    private static final String VALUE = "value";

    private void createAddForm(final ContentHandler contentHandler, final int position) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute("", METHOD, METHOD, CDATA, "get");
        XMLUtils.startElement(contentHandler, XHTMLNS, FORM, atts);
        atts = new AttributesImpl();
        atts.addAttribute("", NAME, NAME, CDATA, "url");
        XMLUtils.createElementNS(contentHandler, XHTMLNS, INPUT, atts);
        atts = new AttributesImpl();
        atts.addAttribute("", NAME, NAME, CDATA, "label");
        XMLUtils.createElementNS(contentHandler, XHTMLNS, INPUT, atts);
        atts = new AttributesImpl();
        atts.addAttribute("", NAME, NAME, CDATA, "action");
        atts.addAttribute("", VALUE, VALUE, CDATA, "add");
        atts.addAttribute("", TYPE, TYPE, CDATA, "hidden");
        XMLUtils.createElementNS(contentHandler, XHTMLNS, INPUT, atts);
        atts = new AttributesImpl();
        atts.addAttribute("", NAME, NAME, CDATA, "position");
        atts.addAttribute("", VALUE, VALUE, CDATA, Integer.toString(position));
        atts.addAttribute("", TYPE, TYPE, CDATA, "hidden");
        XMLUtils.createElementNS(contentHandler, XHTMLNS, INPUT, atts);
        atts = new AttributesImpl();
        atts.addAttribute("", TYPE, TYPE, CDATA, "submit");
        XMLUtils.createElementNS(contentHandler, XHTMLNS, INPUT, atts);
        XMLUtils.endElement(contentHandler, XHTMLNS, FORM);
    }

    @Override
    protected void createBookmarksUL(final Bookmarks bookmarks, final ContentHandler contentHandler, final int formPosition)
            throws SAXException {
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
            atts.addAttribute("", CLASS, CLASS, CDATA, "nav");
            XMLUtils.createElementNS(contentHandler, XHTMLNS, A, atts, "delete");
            if (bookmarks.size() > 1 && i < bookmarks.size() - 1) {
                atts = new AttributesImpl();
                atts.addAttribute("", HREF, HREF, CDATA, "?action=down&position=" + i);
                atts.addAttribute("", CLASS, CLASS, CDATA, "nav");
                XMLUtils.createElementNS(contentHandler, XHTMLNS, A, atts, "down");
            }
            if (bookmarks.size() > 1 && i > 0) {
                atts = new AttributesImpl();
                atts.addAttribute("", HREF, HREF, CDATA, "?action=up&position=" + i);
                atts.addAttribute("", CLASS, CLASS, CDATA, "nav");
                XMLUtils.createElementNS(contentHandler, XHTMLNS, A, atts, "up");
            }
            atts = new AttributesImpl();
            atts.addAttribute("", HREF, HREF, CDATA, "?action=insertBefore&position=" + i);
            atts.addAttribute("", CLASS, CLASS, CDATA, "nav");
            XMLUtils.createElementNS(contentHandler, XHTMLNS, A, atts, "insertBefore");
            atts = new AttributesImpl();
            atts.addAttribute("", HREF, HREF, CDATA, "?action=insertAfter&position=" + i);
            atts.addAttribute("", CLASS, CLASS, CDATA, "nav");
            XMLUtils.createElementNS(contentHandler, XHTMLNS, A, atts, "insertAfter");
            i++;
            XMLUtils.endElement(contentHandler, XHTMLNS, LI);
        }
        if (i != 0 && i == formPosition) {
            createAddForm(contentHandler, i);
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
