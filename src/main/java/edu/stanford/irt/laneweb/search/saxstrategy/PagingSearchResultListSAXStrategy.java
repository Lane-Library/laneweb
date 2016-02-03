package edu.stanford.irt.laneweb.search.saxstrategy;

import java.util.HashSet;
import java.util.ListIterator;
import java.util.Set;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.PagingData;
import edu.stanford.irt.laneweb.resource.Resource;
import edu.stanford.irt.laneweb.search.ContentResultSearchResult;
import edu.stanford.irt.laneweb.search.PagingSearchResultList;
import edu.stanford.irt.laneweb.search.SearchResult;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.search.impl.Result;

public class PagingSearchResultListSAXStrategy implements SAXStrategy<PagingSearchResultList> {

    private static final String LENGTH = "length";

    private static final String PAGE = "page";

    private static final String PAGES = "pages";

    private static final String START = "start";

    private SAXStrategy<SearchResult> saxStrategy;

    public PagingSearchResultListSAXStrategy(final SAXStrategy<SearchResult> saxStrategy) {
        this.saxStrategy = saxStrategy;
    }

    @Override
    public void toSAX(final PagingSearchResultList list, final XMLConsumer xmlConsumer) {
        PagingData pagingData = list.getPagingData();
        int start = pagingData.getStart();
        int length = pagingData.getLength();
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping("", Resource.NAMESPACE);
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(Resource.EMPTY_NS, Resource.SIZE, Resource.SIZE, Resource.CDATA,
                    Integer.toString(list.size()));
            atts.addAttribute(Resource.EMPTY_NS, START, START, Resource.CDATA, Integer.toString(start));
            atts.addAttribute(Resource.EMPTY_NS, LENGTH, LENGTH, Resource.CDATA, Integer.toString(length));
            atts.addAttribute(Resource.EMPTY_NS, PAGE, PAGE, Resource.CDATA, Integer.toString(pagingData.getPage()));
            atts.addAttribute(Resource.EMPTY_NS, PAGES, PAGES, Resource.CDATA, Integer.toString(pagingData.getPages()));
            XMLUtils.startElement(xmlConsumer, Resource.NAMESPACE, Resource.RESOURCES, atts);
            String query = list.getQuery();
            if (query != null) {
                XMLUtils.startElement(xmlConsumer, Resource.NAMESPACE, Resource.QUERY);
                XMLUtils.data(xmlConsumer, query);
                XMLUtils.endElement(xmlConsumer, Resource.NAMESPACE, Resource.QUERY);
            }
            hitCountsToSAX(xmlConsumer, list);
            int i = 0;
            for (ListIterator<SearchResult> it = list.listIterator(start); it.hasNext() && i < length; i++) {
                this.saxStrategy.toSAX(it.next(), xmlConsumer);
            }
            XMLUtils.endElement(xmlConsumer, Resource.NAMESPACE, Resource.RESOURCES);
            xmlConsumer.endPrefixMapping("");
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void hitCountsToSAX(final XMLConsumer xmlConsumer, final PagingSearchResultList list) throws SAXException {
        XMLUtils.startElement(xmlConsumer, Resource.NAMESPACE, Resource.CONTENT_HIT_COUNTS);
        Set<Result> countedResources = new HashSet<>();
        for (SearchResult resource : list) {
            if (resource instanceof ContentResultSearchResult) {
                Result resourceResult = ((ContentResultSearchResult) resource).getResourceResult();
                if (!countedResources.contains(resourceResult)) {
                    countedResources.add(resourceResult);
                    AttributesImpl atts = new AttributesImpl();
                    atts.addAttribute(Resource.EMPTY_NS, Resource.RESOURCE_ID, Resource.RESOURCE_ID, Resource.CDATA,
                            resourceResult.getId());
                    atts.addAttribute(Resource.EMPTY_NS, Resource.RESOURCE_HITS, Resource.RESOURCE_HITS, Resource.CDATA,
                            resourceResult.getHits());
                    atts.addAttribute(Resource.EMPTY_NS, Resource.RESOURCE_URL, Resource.RESOURCE_URL, Resource.CDATA,
                            resourceResult.getURL());
                    XMLUtils.startElement(xmlConsumer, Resource.NAMESPACE, Resource.RESOURCE, atts);
                    XMLUtils.endElement(xmlConsumer, Resource.NAMESPACE, Resource.RESOURCE);
                }
            }
        }
        XMLUtils.endElement(xmlConsumer, Resource.NAMESPACE, Resource.CONTENT_HIT_COUNTS);
    }
}
