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

    private void createAddForm(final ContentHandler contentHandler) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute("", METHOD, METHOD, CDATA, "post");
        atts.addAttribute("", CLASS, CLASS, CDATA, "add");
        XMLUtils.startElement(contentHandler, XHTMLNS, FORM, atts);
        XMLUtils.startElement(contentHandler, XHTMLNS, DIV);
        XMLUtils.createElementNS(contentHandler, XHTMLNS, "h4", "add a bookmark");
        XMLUtils.startElement(contentHandler, XHTMLNS, DIV);
        XMLUtils.createElementNS(contentHandler, XHTMLNS, "label", "url:");
        atts = new AttributesImpl();
        atts.addAttribute("", NAME, NAME, CDATA, "url");
        atts.addAttribute("", TYPE, TYPE, CDATA, "text");
        XMLUtils.createElementNS(contentHandler, XHTMLNS, INPUT, atts);
        XMLUtils.endElement(contentHandler, XHTMLNS, DIV);
        XMLUtils.startElement(contentHandler, XHTMLNS, DIV);
        XMLUtils.createElementNS(contentHandler, XHTMLNS, "label", "label:");
        atts = new AttributesImpl();
        atts.addAttribute("", NAME, NAME, CDATA, "label");
        atts.addAttribute("", TYPE, TYPE, CDATA, "text");
        XMLUtils.createElementNS(contentHandler, XHTMLNS, INPUT, atts);
        XMLUtils.endElement(contentHandler, XHTMLNS, DIV);
        atts = new AttributesImpl();
        atts.addAttribute("", NAME, NAME, CDATA, "action");
        atts.addAttribute("", VALUE, VALUE, CDATA, "add");
        atts.addAttribute("", TYPE, TYPE, CDATA, "hidden");
        XMLUtils.createElementNS(contentHandler, XHTMLNS, INPUT, atts);
        // atts = new AttributesImpl();
        // atts.addAttribute("", NAME, NAME, CDATA, "position");
        // atts.addAttribute("", VALUE, VALUE, CDATA,
        // Integer.toString(position));
        // atts.addAttribute("", TYPE, TYPE, CDATA, "hidden");
        // XMLUtils.createElementNS(contentHandler, XHTMLNS, INPUT, atts);
        atts = new AttributesImpl();
        atts.addAttribute("", TYPE, TYPE, CDATA, "submit");
        atts.addAttribute("", VALUE, VALUE, CDATA, "add");
        XMLUtils.createElementNS(contentHandler, XHTMLNS, INPUT, atts);
        XMLUtils.endElement(contentHandler, XHTMLNS, DIV);
        XMLUtils.endElement(contentHandler, XHTMLNS, FORM);
    }

    @Override
    protected String getEditHref() {
        return "bookmarks.html";
    }

    @Override
    protected String getEditLabel() {
        return "done";
    }

    @Override
    protected void maybeCreateBookmarksUL(final Bookmarks bookmarks, final ContentHandler contentHandler) throws SAXException {
        if (bookmarks != null && bookmarks.size() > 0) {
        XMLUtils.startElement(contentHandler, XHTMLNS, UL);
        int i = 0;
        for (Bookmark bookmark : bookmarks) {
            // if (i == formPosition) {
            // createAddForm(contentHandler, i);
            // }
            XMLUtils.startElement(contentHandler, XHTMLNS, LI);
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute("", METHOD, METHOD, CDATA, "post");
            atts.addAttribute("", CLASS, CLASS, CDATA, "delete");
            XMLUtils.startElement(contentHandler, XHTMLNS, FORM, atts);
            XMLUtils.startElement(contentHandler, XHTMLNS, DIV);
            atts = new AttributesImpl();
            atts.addAttribute("", TYPE, TYPE, CDATA, "submit");
            atts.addAttribute("", VALUE, VALUE, CDATA, "delete");
            XMLUtils.createElementNS(contentHandler, XHTMLNS, INPUT, atts);
            atts = new AttributesImpl();
            atts.addAttribute("", HREF, HREF, CDATA, bookmark.getUrl());
            XMLUtils.createElementNS(contentHandler, XHTMLNS, A, atts, bookmark.getLabel());
            atts = new AttributesImpl();
            atts.addAttribute("", NAME, NAME, CDATA, "action");
            atts.addAttribute("", VALUE, VALUE, CDATA, "delete");
            atts.addAttribute("", TYPE, TYPE, CDATA, "hidden");
            XMLUtils.createElementNS(contentHandler, XHTMLNS, INPUT, atts);
            atts = new AttributesImpl();
            atts.addAttribute("", NAME, NAME, CDATA, "position");
            atts.addAttribute("", VALUE, VALUE, CDATA, Integer.toString(i));
            atts.addAttribute("", TYPE, TYPE, CDATA, "hidden");
            XMLUtils.createElementNS(contentHandler, XHTMLNS, INPUT, atts);
            XMLUtils.endElement(contentHandler, XHTMLNS, DIV);
            XMLUtils.endElement(contentHandler, XHTMLNS, FORM);
            
//            atts.addAttribute("", HREF, HREF, CDATA, "?action=delete&position=" + i);
//            atts.addAttribute("", CLASS, CLASS, CDATA, "nav");
//            XMLUtils.createElementNS(contentHandler, XHTMLNS, A, atts, "delete");
            // if (bookmarks.size() > 1 && i < bookmarks.size() - 1) {
            // atts = new AttributesImpl();
            // atts.addAttribute("", HREF, HREF, CDATA, "?action=down&position="
            // + i);
            // atts.addAttribute("", CLASS, CLASS, CDATA, "nav");
            // XMLUtils.createElementNS(contentHandler, XHTMLNS, A, atts,
            // "down");
            // }
            // if (bookmarks.size() > 1 && i > 0) {
            // atts = new AttributesImpl();
            // atts.addAttribute("", HREF, HREF, CDATA, "?action=up&position=" +
            // i);
            // atts.addAttribute("", CLASS, CLASS, CDATA, "nav");
            // XMLUtils.createElementNS(contentHandler, XHTMLNS, A, atts, "up");
            // }
            // atts = new AttributesImpl();
            // atts.addAttribute("", HREF, HREF, CDATA,
            // "?action=insertBefore&position=" + i);
            // atts.addAttribute("", CLASS, CLASS, CDATA, "nav");
            // XMLUtils.createElementNS(contentHandler, XHTMLNS, A, atts,
            // "insertBefore");
            // atts = new AttributesImpl();
            // atts.addAttribute("", HREF, HREF, CDATA,
            // "?action=insertAfter&position=" + i);
            // atts.addAttribute("", CLASS, CLASS, CDATA, "nav");
            // XMLUtils.createElementNS(contentHandler, XHTMLNS, A, atts,
            // "insertAfter");
            i++;
            XMLUtils.endElement(contentHandler, XHTMLNS, LI);
        }
        // if (i != 0 && i == formPosition) {
        // createAddForm(contentHandler, i);
        // }
        XMLUtils.endElement(contentHandler, XHTMLNS, UL);
        }
        createAddForm(contentHandler);
    }
}
