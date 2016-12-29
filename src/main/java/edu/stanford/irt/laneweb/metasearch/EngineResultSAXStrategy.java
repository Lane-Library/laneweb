package edu.stanford.irt.laneweb.metasearch;

import java.util.Collection;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.Result;

public class EngineResultSAXStrategy extends AbstractResultSAXStrategy<Result> {

    /** the String 'engine'. */
    private static final String ENGINE = "engine";

    private SAXStrategy<Result> resourceSAXStrategy;

    public EngineResultSAXStrategy(final SAXStrategy<Result> resourceSAXStrategy) {
        this.resourceSAXStrategy = resourceSAXStrategy;
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
        Collection<Result> children;
        String description;
        Exception exception;
        String hits;
        String id;
        SearchStatus status;
        String time;
        String url;
        synchronized (result) {
            children = result.getChildren();
            description = result.getDescription();
            exception = result.getException();
            hits = result.getHits();
            id = result.getId();
            status = result.getStatus();
            time = result.getTime();
            url = result.getURL();
        }
        atts.addAttribute(NAMESPACE, ID, ID, CDATA, id);
        if (status != null) {
            atts.addAttribute(NAMESPACE, STATUS, STATUS, CDATA, status.toString().toLowerCase());
        }
        try {
            xmlConsumer.startElement(NAMESPACE, ENGINE, ENGINE, atts);
            handleElement(xmlConsumer, URL, url);
            handleElement(xmlConsumer, HITS, hits);
            handleElement(xmlConsumer, TIME, time);
            handleElement(xmlConsumer, DESCRIPTION, description);
            doToSAXException(xmlConsumer, exception);
            for (Result child : children) {
                this.resourceSAXStrategy.toSAX(child, xmlConsumer);
            }
            xmlConsumer.endElement(NAMESPACE, ENGINE, ENGINE);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
