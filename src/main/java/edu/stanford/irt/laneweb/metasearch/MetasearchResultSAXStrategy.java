package edu.stanford.irt.laneweb.metasearch;

import java.util.Collection;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.Result;

public class MetasearchResultSAXStrategy extends AbstractResultSAXStrategy<Result> {

    /** the String 'query'. */
    private static final String QUERY = "query";

    /** the root element, the String 'search'. */
    private static final String SEARCH = "search";

    private SAXStrategy<Result> engineSAXStrategy;

    public MetasearchResultSAXStrategy(final SAXStrategy<Result> engineSAXStrategy) {
        this.engineSAXStrategy = engineSAXStrategy;
    }

    /**
     * @param xmlConsumer
     *            the XMLConsumer
     * @param result
     *            the Result to convert
     */
    @Override
    public void toSAX(final Result result, final XMLConsumer xmlConsumer) {
        AttributesImpl atts = new AttributesImpl();
        Collection<Result> children = result.getChildren();
        Query query = result.getQuery();
        SearchStatus status = result.getStatus();
        if (status != null) {
            atts.addAttribute(NAMESPACE, STATUS, STATUS, CDATA, status.toString().toLowerCase());
        }
        try {
            xmlConsumer.startDocument();
            xmlConsumer.startPrefixMapping("", MetasearchResultSAXStrategy.NAMESPACE);
            xmlConsumer.startElement(NAMESPACE, SEARCH, SEARCH, atts);
            handleElement(xmlConsumer, QUERY, query.getSearchText());
            for (Result child : children) {
                this.engineSAXStrategy.toSAX(child, xmlConsumer);
            }
            xmlConsumer.endElement(NAMESPACE, SEARCH, SEARCH);
            xmlConsumer.endPrefixMapping("");
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
