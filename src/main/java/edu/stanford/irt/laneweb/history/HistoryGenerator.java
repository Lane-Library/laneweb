package edu.stanford.irt.laneweb.history;

import org.apache.cocoon.xml.XMLUtils;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.laneweb.bookmarks.Bookmark;
import edu.stanford.irt.laneweb.bookmarks.Bookmarks;
import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;


public class HistoryGenerator extends AbstractGenerator {

    private static final long serialVersionUID = 1L;

    private static final String A = "a";

    private static final String CDATA = "CDATA";

    private static final String HREF = "href";

    private static final String LI = "li";

    private static final String UL = "ul";

    private static final String XHTMLNS = "http://www.w3.org/1999/xhtml";
    
    private Bookmarks history;

    public void generate() throws SAXException {
        this.xmlConsumer.startDocument();
        if (this.history != null && this.history.size() > 0) {
//            XMLUtils.startElement(this.xmlConsumer, XHTMLNS, UL);
            for (Bookmark bookmark : this.history) {
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute("", "class", "class", CDATA, "history");
                XMLUtils.startElement(this.xmlConsumer, XHTMLNS, LI, atts);
                atts = new AttributesImpl();
                atts.addAttribute("", HREF, HREF, CDATA, bookmark.getUrl());
                XMLUtils.createElementNS(this.xmlConsumer, XHTMLNS, A, atts, bookmark.getLabel());
                XMLUtils.endElement(this.xmlConsumer, XHTMLNS, LI);
            }
//            XMLUtils.endElement(this.xmlConsumer, XHTMLNS, UL);
        }
        this.xmlConsumer.endDocument();
    }

    @Override
    protected void initialize() {
        super.initialize();
        this.history = ModelUtil.getObject(this.model, Model.HISTORY, Bookmarks.class);
    }
}
