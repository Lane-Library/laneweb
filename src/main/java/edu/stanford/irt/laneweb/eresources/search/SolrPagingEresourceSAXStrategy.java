package edu.stanford.irt.laneweb.eresources.search;

import java.util.List;

import org.springframework.data.domain.Page;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.model.Eresource;
import edu.stanford.irt.laneweb.eresources.model.solr.RestResult;
import edu.stanford.irt.laneweb.resource.Resource;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class SolrPagingEresourceSAXStrategy implements SAXStrategy<RestResult<Eresource>> {

    private static final String LENGTH = "length";

    private static final String PAGE = "page";

    private static final String PAGES = "pages";

    private static final String START = "start";

    private SAXStrategy<Eresource> eresourceSaxStrategy;

    public SolrPagingEresourceSAXStrategy(final SAXStrategy<Eresource> saxStrategy) {
        this.eresourceSaxStrategy = saxStrategy;
    }

    @Override
    public void toSAX(final RestResult<Eresource> object, final XMLConsumer xmlConsumer) {
        Page<Eresource> page = object.getPage();
        List<Eresource> eresources = page.getContent();
        String query = object.getQuery();
        int pageSize = page.getSize();
        int pageNumber = page.getNumber();
        long start = pageSize * (long) pageNumber;
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
