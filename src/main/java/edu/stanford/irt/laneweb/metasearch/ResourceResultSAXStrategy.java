package edu.stanford.irt.laneweb.metasearch;

import java.util.Locale;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

public class ResourceResultSAXStrategy extends AbstractResultSAXStrategy<Result> {

    /** the String 'resource'. */
    private static final String RESOURCE = "resource";

    private SAXStrategy<ContentResult> contentSAXStrategy;

    public ResourceResultSAXStrategy(final SAXStrategy<ContentResult> contentSAXStrategy) {
        this.contentSAXStrategy = contentSAXStrategy;
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
        SearchStatus status = result.getStatus();
        atts.addAttribute(NAMESPACE, ID, ID, CDATA, result.getId());
        if (status != null) {
            atts.addAttribute(NAMESPACE, STATUS, STATUS, CDATA, status.toString().toLowerCase(Locale.US));
        }
        try {
            xmlConsumer.startElement(NAMESPACE, RESOURCE, RESOURCE, atts);
            handleElement(xmlConsumer, URL, result.getURL());
            handleElement(xmlConsumer, HITS, result.getHits());
            handleElement(xmlConsumer, TIME, result.getTime());
            handleElement(xmlConsumer, DESCRIPTION, result.getDescription());
            doToSAXException(xmlConsumer, result.getException());
            for (Result child : result.getChildren()) {
                this.contentSAXStrategy.toSAX((ContentResult) child, xmlConsumer);
            }
            xmlConsumer.endElement(NAMESPACE, RESOURCE, RESOURCE);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
