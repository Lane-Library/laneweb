package edu.stanford.irt.laneweb.metasearch;

import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.PagingData;
import edu.stanford.irt.laneweb.resource.PagingList;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.search.impl.Result;

public class ClinicalSearchResultsSAXStrategy implements SAXStrategy<ClinicalSearchResults> {

    private static final String CDATA = "CDATA";

    private static final String COUNT = "count";

    private static final String EMPTY_NS = "";

    private static final String END = "end";

    private static final String HITS = "hits";

    private static final String ID = "id";

    private static final String NAMESPACE = "http://lane.stanford.edu/results/1.0";

    private static final String PAGE = "page";

    private static final String PAGES = "pages";

    private static final String RESOURCE = "resource";

    private static final String RESULTS = "results";

    private static final String SIZE = "size";

    private static final String START = "start";

    private static final String TOTAL = "total";

    private static final String URL = "url";

    private SAXStrategy<SearchResult> saxStrategy = new SearchResultSAXStrategy();

    private static void resourceToSAX(final XMLConsumer xmlConsumer, final Result result) {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, ID, ID, CDATA, result.getId());
        atts.addAttribute(EMPTY_NS, HITS, HITS, CDATA, result.getHits());
        atts.addAttribute(EMPTY_NS, URL, URL, CDATA, result.getURL());
        atts.addAttribute(EMPTY_NS, COUNT, COUNT, CDATA, Integer.toString(result.getChildren().size()));
        try {
            XMLUtils.startElement(xmlConsumer, NAMESPACE, RESOURCE, atts);
            XMLUtils.endElement(xmlConsumer, NAMESPACE, RESOURCE);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    @Override
    public void toSAX(final ClinicalSearchResults results, final XMLConsumer xmlConsumer) {
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping("", NAMESPACE);
            PagingList<SearchResult> searchResults = results.getSearchResults();
            PagingData pd = searchResults.getPagingData();
            int length = pd.getLength();
            int page = pd.getPage() + 1;
            int pages = pd.getPages();
            int size = searchResults.size();
            int start = pd.getStart();
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, PAGE, PAGE, CDATA, Integer.toString(page));
            atts.addAttribute(EMPTY_NS, PAGES, PAGES, CDATA, Integer.toString(pages));
            atts.addAttribute(EMPTY_NS, SIZE, SIZE, CDATA, Integer.toString(size));
            atts.addAttribute(EMPTY_NS, START, START, CDATA, Integer.toString(start + 1));
            atts.addAttribute(EMPTY_NS, END, END, CDATA, Integer.toString(start + length));
            atts.addAttribute(EMPTY_NS, TOTAL, TOTAL, CDATA, Integer.toString(results.getTotal()));
            XMLUtils.startElement(xmlConsumer, NAMESPACE, RESULTS, atts);
            results.getResourceResults().stream().forEach((final Result r) -> resourceToSAX(xmlConsumer, r));
            searchResultsToSAX(xmlConsumer, searchResults.subList(start, start + length));
            XMLUtils.endElement(xmlConsumer, NAMESPACE, RESULTS);
            xmlConsumer.endPrefixMapping("");
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void searchResultsToSAX(final XMLConsumer xmlConsumer, final List<SearchResult> searchResults) {
        searchResults.stream().forEach((final SearchResult s) -> this.saxStrategy.toSAX(s, xmlConsumer));
    }
}
