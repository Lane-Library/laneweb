package edu.stanford.irt.laneweb.eresources;

import java.util.ListIterator;

import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.Resource;
import edu.stanford.irt.laneweb.util.XMLUtils;


public class PagingEresourceListSAXStrategy implements SAXStrategy<PagingEresourceList>, Resource {

    private static final String CDATA = "CDATA";

    private static final String LENGTH = "length";

    private static final String PAGE = "page";

    private static final String PAGES = "pages";

    private static final String START = "start";

    private SAXStrategy<Eresource> saxStrategy;
    
    public PagingEresourceListSAXStrategy(SAXStrategy<Eresource> saxStrategy) {
        this.saxStrategy  = saxStrategy;
    }

    public void toSAX(PagingEresourceList list, XMLConsumer xmlConsumer) {
        int start = list.getStart();
        int length = list.getLength();
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping("", NAMESPACE);
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, SIZE, SIZE, CDATA, Integer.toString(list.size()));
            atts.addAttribute(EMPTY_NS, START, START, CDATA, Integer.toString(start));
            atts.addAttribute(EMPTY_NS, LENGTH, LENGTH, CDATA, Integer.toString(length));
            atts.addAttribute(EMPTY_NS, PAGE, PAGE, CDATA, Integer.toString(list.getPage()));
            atts.addAttribute(EMPTY_NS, PAGES, PAGES, CDATA, Integer.toString(list.getPages()));
            XMLUtils.startElement(xmlConsumer, NAMESPACE, RESOURCES, atts);
            int i = 0;
            for (ListIterator<Eresource> it = list.listIterator(start); it.hasNext() && i < length; i++) {
                this.saxStrategy.toSAX(it.next(), xmlConsumer);
            }
            XMLUtils.endElement(xmlConsumer, NAMESPACE, RESOURCES);
            xmlConsumer.endPrefixMapping("");
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
