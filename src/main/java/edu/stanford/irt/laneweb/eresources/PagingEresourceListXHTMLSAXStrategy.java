package edu.stanford.irt.laneweb.eresources;

import java.util.ListIterator;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class PagingEresourceListXHTMLSAXStrategy implements SAXStrategy<PagingEresourceList> {

    private static final String CDATA = "CDATA";

    private static final String CLASS = "class";

    private static final String DD = "dd";

    private static final String NO_PREFIX = "";

    private static final String UL = "ul";

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    private static final String EMPTY_NS = "";

    private SAXStrategy<Eresource> saxStrategy;

    public PagingEresourceListXHTMLSAXStrategy(final SAXStrategy<Eresource> saxStrategy) {
        this.saxStrategy = saxStrategy;
    }

    public void toSAX(final PagingEresourceList list, final XMLConsumer xmlConsumer) {
        int start = list.getStart();
        int length = list.getLength();
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping(NO_PREFIX, XHTML_NS);
            AttributesImpl atts = new AttributesImpl();
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "html");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "head");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "title");
            XMLUtils.data(xmlConsumer, "search results");
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "title");
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "head");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "body");
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "lwSearchResults");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "dl", atts);
            int i = 0;
            for (ListIterator<Eresource> it = list.listIterator(start); it.hasNext() && i < length; i++) {
                Eresource eresource = it.next();
                XMLUtils.startElement(xmlConsumer, XHTML_NS, DD);
                StringBuilder sb = new StringBuilder("r-");
                sb.append(start + 1 + i);
                if (eresource.getDescription() != null) {
                    sb.append(" hvrTrig");
                }
                atts = new AttributesImpl();
                atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, sb.toString());
                XMLUtils.startElement(xmlConsumer, XHTML_NS, UL, atts);
                this.saxStrategy.toSAX(eresource, xmlConsumer);
                XMLUtils.endElement(xmlConsumer, XHTML_NS, UL);
                XMLUtils.endElement(xmlConsumer, XHTML_NS, DD);
            }
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "dl");
            
            
            atts = new AttributesImpl();
            atts.addAttribute(NO_PREFIX, CLASS, CLASS, CDATA, "results-nav");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "div", atts);
            atts = new AttributesImpl();
            atts.addAttribute(NO_PREFIX, CLASS, CLASS, CDATA, "yui-g");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "div", atts);
            atts = new AttributesImpl();
            atts.addAttribute(NO_PREFIX, CLASS, CLASS, CDATA, "yui-u first");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "div", atts);
            int size = list.size();
            StringBuilder sb = new StringBuilder("Displaying ");
            if (size > length) {
                sb.append(start + 1).append(" to ").append(start + length).append(" of ");
                XMLUtils.data(xmlConsumer, sb.toString());
                atts = new AttributesImpl();
                atts.addAttribute(NO_PREFIX, "href", "href", CDATA, "?page=all");
                XMLUtils.startElement(xmlConsumer, XHTML_NS, "a", atts);
                XMLUtils.data(xmlConsumer, Integer.toString(size));
                XMLUtils.endElement(xmlConsumer, XHTML_NS, "a");
                XMLUtils.data(xmlConsumer, " matches");
            } else {
                sb.append("all ").append(size).append(" matches");
                XMLUtils.data(xmlConsumer, sb.toString());
            }
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "div");
            atts = new AttributesImpl();
            atts.addAttribute(NO_PREFIX, CLASS, CLASS, CDATA, "yui-u");
            atts.addAttribute(NO_PREFIX, "style", "style", CDATA, "text-align:right");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "div", atts);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "div");
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "div");
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "div");
            atts = new AttributesImpl();
            atts.addAttribute(NO_PREFIX, "id", "id", CDATA, "search-content-counts");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, "div", atts);
            XMLUtils.data(xmlConsumer, "\u00A0");
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "div");
            
            
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "body");
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "html");
            xmlConsumer.endPrefixMapping(NO_PREFIX);
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
