package edu.stanford.irt.laneweb.search.saxstrategy;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.Resource;
import edu.stanford.irt.laneweb.solr.Eresource;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class SolrPagingEresourceSAXStrategy implements SAXStrategy<Map<String, Object>> {

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
        @SuppressWarnings("unchecked")
        Page<Eresource> page = (Page<Eresource>) object.get("resultPage");
        List<Eresource> eresources = page.getContent();
        String query = (String) object.get("searchTerm");
        int pageSize = page.getSize();
        int pageNumber = page.getNumber();
        long start = pageSize * pageNumber;
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(Resource.EMPTY_NS, Resource.SIZE, Resource.SIZE, Resource.CDATA,
                    Long.toString(page.getTotalElements()));
            atts.addAttribute(Resource.EMPTY_NS, START, START, Resource.CDATA, Long.toString(start));
            atts.addAttribute(Resource.EMPTY_NS, LENGTH, LENGTH, Resource.CDATA, Integer.toString(pageSize));
            atts.addAttribute(Resource.EMPTY_NS, PAGE, PAGE, Resource.CDATA, Integer.toString(pageNumber));
            atts.addAttribute(Resource.EMPTY_NS, PAGES, PAGES, Resource.CDATA, Integer.toString(page.getTotalPages()));
            XMLUtils.startElement(xmlConsumer, Resource.NAMESPACE, Resource.RESOURCES, atts);
            if (query != null) {
                XMLUtils.startElement(xmlConsumer, Resource.NAMESPACE, Resource.QUERY);
                XMLUtils.data(xmlConsumer, query);
                XMLUtils.endElement(xmlConsumer, Resource.NAMESPACE, Resource.QUERY);
            }
            XMLUtils.startElement(xmlConsumer, Resource.NAMESPACE, Resource.CONTENT_HIT_COUNTS);
            XMLUtils.endElement(xmlConsumer, Resource.NAMESPACE, Resource.CONTENT_HIT_COUNTS);
            for (Eresource er : eresources) {
                this.eresourceSaxStrategy.toSAX(er, xmlConsumer);
            }
            XMLUtils.endElement(xmlConsumer, Resource.NAMESPACE, Resource.RESOURCES);
            xmlConsumer.endPrefixMapping("");
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
