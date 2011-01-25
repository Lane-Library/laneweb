package edu.stanford.irt.laneweb.bookmarks;

import java.io.IOException;

import org.apache.cocoon.xml.XMLUtils;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class BookmarksGenerator extends AbstractGenerator {

    private static final String A = "a";
    
    private static final String DIV = "div";

    private static final String CDATA = "CDATA";
    
    private static final String CLASS = "class";
    
    private static final String H3 = "h3";

    private static final String HREF = "href";
    
    private static final String ID = "id";

    private static final String LI = "li";

    private static final long serialVersionUID = 1L;

    private static final String UL = "ul";

    private static final String XHTMLNS = "http://www.w3.org/1999/xhtml";

    private Bookmarks bookmarks;

    private String requestUri;

    public void generate() throws SAXException, IOException {
        this.xmlConsumer.startDocument();
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute("", CLASS, CLASS, CDATA, "module");
        atts.addAttribute("", ID, ID, ID, "bookmarks");
        XMLUtils.startElement(this.xmlConsumer, XHTMLNS, DIV, atts);
        XMLUtils.startElement(this.xmlConsumer, XHTMLNS, H3);
        XMLUtils.data(this.xmlConsumer, "bookmarks");
        String href = this.requestUri.equals("edit-bookmarks.html") ? "bookmarks.html" : "edit-bookmarks.html";
        String label = href.equals("bookmarks.html") ? "done" : "edit";
        atts = new AttributesImpl();
        atts.addAttribute("", HREF, HREF, CDATA, href);
        XMLUtils.createElementNS(this.xmlConsumer, XHTMLNS, A, atts, label);
        XMLUtils.endElement(this.xmlConsumer, XHTMLNS, H3);
        atts = new AttributesImpl();
        atts.addAttribute("", CLASS, CLASS, CDATA, "bd");
        XMLUtils.startElement(this.xmlConsumer, XHTMLNS, DIV, atts);
        if (this.bookmarks.size() > 0) {
            XMLUtils.startElement(this.xmlConsumer, XHTMLNS, UL);
            for (Bookmark bookmark : this.bookmarks) {
                XMLUtils.startElement(this.xmlConsumer, XHTMLNS, LI);
                atts = new AttributesImpl();
                atts.addAttribute("", HREF, HREF, CDATA, bookmark.getUrl());
                XMLUtils.createElementNS(this.xmlConsumer, XHTMLNS, A, atts, bookmark.getLabel());
                XMLUtils.endElement(this.xmlConsumer, XHTMLNS, LI);
            }
            XMLUtils.endElement(this.xmlConsumer, XHTMLNS, UL);
        }
        XMLUtils.endElement(this.xmlConsumer, XHTMLNS, DIV);
        XMLUtils.endElement(this.xmlConsumer, XHTMLNS, DIV);
        this.xmlConsumer.endDocument();
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.bookmarks = ModelUtil.getObject(this.model, Model.BOOKMARKS, Bookmarks.class);
        this.requestUri = ModelUtil.getString(this.model, Model.REQUEST_URI);
    }
}
