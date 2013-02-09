package edu.stanford.irt.laneweb.eresources;

import java.util.ListIterator;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.Resource;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class PagingEresourceListXHTMLSAXStrategy implements SAXStrategy<PagingEresourceList>, Resource {

    private static final String CDATA = "CDATA";

    private static final String CLASS = "class";

    private static final String DD = "dd";

    private static final String END = "end";

    private static final String LENGTH = "length";

    private static final String NO_PREFIX = "";

    private static final String PAGE = "page";

    private static final String PAGES = "pages";

    private static final String PAGING_LABEL = "pagingLabel";

    private static final String RESULTS = "results";

    private static final String START = "start";

    private static final String UL = "ul";

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    private SAXStrategy<Eresource> saxStrategy;

    public PagingEresourceListXHTMLSAXStrategy(final SAXStrategy<Eresource> saxStrategy) {
        this.saxStrategy = saxStrategy;
    }

    public void toSAX(final PagingEresourceList list, final XMLConsumer xmlConsumer) {
        int start = list.getStart();
        int length = list.getLength();
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping(NO_PREFIX, NAMESPACE);
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, SIZE, SIZE, CDATA, Integer.toString(list.size()));
            atts.addAttribute(EMPTY_NS, START, START, CDATA, Integer.toString(start));
            atts.addAttribute(EMPTY_NS, LENGTH, LENGTH, CDATA, Integer.toString(length));
            atts.addAttribute(EMPTY_NS, PAGE, PAGE, CDATA, Integer.toString(list.getPage()));
            atts.addAttribute(EMPTY_NS, PAGES, PAGES, CDATA, Integer.toString(list.getPages()));
            XMLUtils.startElement(xmlConsumer, NAMESPACE, RESOURCES, atts);
            for (ListIterator<PagingLabel> it = list.getPagingLabels().listIterator(); it.hasNext();) {
                PagingLabel pagingLabel = it.next();
                AttributesImpl plAtts = new AttributesImpl();
                plAtts.addAttribute(EMPTY_NS, START, START, CDATA, pagingLabel.getStart());
                plAtts.addAttribute(EMPTY_NS, END, END, CDATA, pagingLabel.getEnd());
                plAtts.addAttribute(EMPTY_NS, RESULTS, RESULTS, CDATA, Integer.toString(pagingLabel.getResults()));
                xmlConsumer.startElement(NAMESPACE, PAGING_LABEL, PAGING_LABEL, plAtts);
                XMLUtils.endElement(xmlConsumer, NAMESPACE, PAGING_LABEL);
            }
            int i = 0;
            xmlConsumer.startPrefixMapping(NO_PREFIX, XHTML_NS);
            for (ListIterator<Eresource> it = list.listIterator(start); it.hasNext() && i < length; i++) {
                XMLUtils.startElement(xmlConsumer, XHTML_NS, DD);
                StringBuilder sb = new StringBuilder("r-");
                sb.append(start + 1 + i);
                atts = new AttributesImpl();
                atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, sb.toString());
                XMLUtils.startElement(xmlConsumer, XHTML_NS, UL, atts);
                xmlConsumer.startPrefixMapping(NO_PREFIX, NAMESPACE);
                this.saxStrategy.toSAX(it.next(), xmlConsumer);
                xmlConsumer.endPrefixMapping(NO_PREFIX);
                XMLUtils.endElement(xmlConsumer, XHTML_NS, UL);
                XMLUtils.endElement(xmlConsumer, XHTML_NS, DD);
            }
            xmlConsumer.endPrefixMapping(NO_PREFIX);
            XMLUtils.endElement(xmlConsumer, NAMESPACE, RESOURCES);
            xmlConsumer.endPrefixMapping(NO_PREFIX);
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
