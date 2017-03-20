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
        Collection<Result> children = result.getChildren();
        String description = result.getDescription();
        Exception exception = result.getException();
        String hits = result.getHits();
        String id = result.getId();
        SearchStatus status = result.getStatus();
        String time = result.getTime();
        String url = result.getURL();
        atts.addAttribute("", ID, ID, CDATA, id);
        if (status != null) {
            atts.addAttribute("", STATUS, STATUS, CDATA, status.toString().toLowerCase());
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
            xmlConsumer.endElement("", ENGINE, ENGINE);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
