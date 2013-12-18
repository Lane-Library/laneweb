package edu.stanford.irt.laneweb.search;

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
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.search.legacy.Result;

public class PagingSearchResultListSAXStrategy implements SAXStrategy<PagingSearchResultList>, Resource {

    private static final String CDATA = "CDATA";

    private static final String LENGTH = "length";

    private static final String PAGE = "page";

    private static final String PAGES = "pages";

    private static final String START = "start";

    private SAXStrategy<SearchResult> saxStrategy;

    public PagingSearchResultListSAXStrategy(final SAXStrategy<SearchResult> saxStrategy) {
        this.saxStrategy = saxStrategy;
    }

    public void toSAX(final PagingSearchResultList list, final XMLConsumer xmlConsumer) {
        PagingData pagingData = list.getPagingData();
        int start = pagingData.getStart();
        int length = pagingData.getLength();
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping("", NAMESPACE);
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, SIZE, SIZE, CDATA, Integer.toString(list.size()));
            atts.addAttribute(EMPTY_NS, START, START, CDATA, Integer.toString(start));
            atts.addAttribute(EMPTY_NS, LENGTH, LENGTH, CDATA, Integer.toString(length));
            atts.addAttribute(EMPTY_NS, PAGE, PAGE, CDATA, Integer.toString(pagingData.getPage()));
            atts.addAttribute(EMPTY_NS, PAGES, PAGES, CDATA, Integer.toString(pagingData.getPages()));
            XMLUtils.startElement(xmlConsumer, NAMESPACE, RESOURCES, atts);
            String query = list.getQuery();
            if (query != null) {
                XMLUtils.startElement(xmlConsumer, NAMESPACE, QUERY);
                XMLUtils.data(xmlConsumer, query);
                XMLUtils.endElement(xmlConsumer, NAMESPACE, QUERY);
            }
            XMLUtils.startElement(xmlConsumer, NAMESPACE, CONTENT_HIT_COUNTS);
            Set<Result> countedResources = new HashSet<Result>();
            for (SearchResult resource : list) {
                if (resource instanceof ContentResultSearchResult) {
                    Result resourceResult = ((ContentResultSearchResult) resource).getResourceResult();
                    if (!countedResources.contains(resourceResult)) {
                        countedResources.add(resourceResult);
                        atts = new AttributesImpl();
                        atts.addAttribute(EMPTY_NS, RESOURCE_ID, RESOURCE_ID, CDATA, resourceResult.getId());
                        atts.addAttribute(EMPTY_NS, RESOURCE_HITS, RESOURCE_HITS, CDATA, resourceResult.getHits());
                        atts.addAttribute(EMPTY_NS, RESOURCE_URL, RESOURCE_URL, CDATA, resourceResult.getURL());
                        XMLUtils.startElement(xmlConsumer, NAMESPACE, RESOURCE, atts);
                        XMLUtils.endElement(xmlConsumer, NAMESPACE, RESOURCE);
                    }
                }
            }
            XMLUtils.endElement(xmlConsumer, NAMESPACE, CONTENT_HIT_COUNTS);
            int i = 0;
            for (ListIterator<SearchResult> it = list.listIterator(start); it.hasNext() && i < length; i++) {
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
