package edu.stanford.irt.laneweb.search.saxstrategy;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.resource.Resource;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class SolrPagingEresourceSAXStrategy implements SAXStrategy<Map<String, Object>>, Resource {

    private static final String LENGTH = "length";

    private static final String PAGE = "page";

    private static final String PAGES = "pages";

    private static final String START = "start";

    private SAXStrategy<Eresource> eresourceSaxStrategy;

    public SolrPagingEresourceSAXStrategy(final SAXStrategy<Eresource> saxStrategy) {
        this.eresourceSaxStrategy = saxStrategy;
    }

    @Override
    public void toSAX(final Map<String, Object> object, final XMLConsumer xmlConsumer) {
        Page<Eresource> page = (Page<Eresource>) object.get("resultPage");
        List<Eresource> eresources = page.getContent();
        String query = (String) object.get("searchTerm");
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping("", NAMESPACE);
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, SIZE, SIZE, CDATA, Long.toString(page.getTotalElements()));
            atts.addAttribute(EMPTY_NS, START, START, CDATA, Integer.toString(page.getSize() * page.getNumber()));
            atts.addAttribute(EMPTY_NS, LENGTH, LENGTH, CDATA, Integer.toString(page.getSize()));
            atts.addAttribute(EMPTY_NS, PAGE, PAGE, CDATA, Integer.toString(page.getNumber() + 1));
            atts.addAttribute(EMPTY_NS, PAGES, PAGES, CDATA, Integer.toString(page.getTotalPages()));
            XMLUtils.startElement(xmlConsumer, NAMESPACE, RESOURCES, atts);
            if (query != null) {
                XMLUtils.startElement(xmlConsumer, NAMESPACE, QUERY);
                XMLUtils.data(xmlConsumer, query);
                XMLUtils.endElement(xmlConsumer, NAMESPACE, QUERY);
            }
            XMLUtils.startElement(xmlConsumer, NAMESPACE, CONTENT_HIT_COUNTS);
            XMLUtils.endElement(xmlConsumer, NAMESPACE, CONTENT_HIT_COUNTS);
            for (Eresource er : eresources) {
                this.eresourceSaxStrategy.toSAX(er, xmlConsumer);
            }
            XMLUtils.endElement(xmlConsumer, NAMESPACE, RESOURCES);
            xmlConsumer.endPrefixMapping("");
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
