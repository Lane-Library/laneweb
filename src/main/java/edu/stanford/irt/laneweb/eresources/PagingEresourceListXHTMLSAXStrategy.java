package edu.stanford.irt.laneweb.eresources;

import java.util.ListIterator;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.PagingData;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class PagingEresourceListXHTMLSAXStrategy implements SAXStrategy<PagingEresourceList> {

    private static final String CDATA = "CDATA";

    private static final String CLASS = "class";

    private static final String LI = "li";

    private static final int DEFAULT_PAGE_SIZE = 100;

    private static final String EMPTY_NS = "";

    private static final String NO_PREFIX = "";

    private static final String UL = "ul";

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    private SAXStrategy<PagingData> pagingSaxStrategy;

    private SAXStrategy<Eresource> saxStrategy;

    public PagingEresourceListXHTMLSAXStrategy(final SAXStrategy<Eresource> saxStrategy,
            final SAXStrategy<PagingData> pagingSaxStrategy) {
        this.saxStrategy = saxStrategy;
        this.pagingSaxStrategy = pagingSaxStrategy;
    }

    public void toSAX(final PagingEresourceList list, final XMLConsumer xmlConsumer) {
        PagingData pagingData = list.getPagingData();
        int start = pagingData.getStart();
        int length = pagingData.getLength();
        int size = list.size();
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
            if (size > DEFAULT_PAGE_SIZE) {
                this.pagingSaxStrategy.toSAX(pagingData, xmlConsumer);
            }
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "lwSearchResults");
//            atts.addAttribute(EMPTY_NS, "style", "style", CDATA, "padding:0;margin-bottom:0");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, UL, atts);
            int i = 0;
            for (ListIterator<Eresource> it = list.listIterator(start); it.hasNext() && i < length; i++) {
                Eresource eresource = it.next();
                StringBuilder sb = new StringBuilder("r-");
                sb.append(start + 1 + i);
                if (eresource.getDescription() != null) {
                    sb.append(" hvrTrig");
                }
                atts = new AttributesImpl();
                atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, sb.toString());
//                atts.addAttribute(EMPTY_NS, "style", "style", CDATA, "padding:5px;margin-bottom:10px");
                XMLUtils.startElement(xmlConsumer, XHTML_NS, LI, atts);
                this.saxStrategy.toSAX(eresource, xmlConsumer);
                XMLUtils.endElement(xmlConsumer, XHTML_NS, LI);
            }
            XMLUtils.endElement(xmlConsumer, XHTML_NS, UL);
            if (size > DEFAULT_PAGE_SIZE) {
                this.pagingSaxStrategy.toSAX(pagingData, xmlConsumer);
            }
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "body");
            XMLUtils.endElement(xmlConsumer, XHTML_NS, "html");
            xmlConsumer.endPrefixMapping(NO_PREFIX);
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
