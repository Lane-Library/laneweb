package edu.stanford.irt.laneweb.searchresults;

import java.util.TreeSet;

import org.apache.cocoon.xml.XMLUtils;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.laneweb.Resource;

/**
 * 
 * @author ryanmax
 * 
 * $Id$
 *
 */
public class XMLizableSearchResultSet extends TreeSet<SearchResult> implements Resource, XMLizable {

    private String query = null;

    public XMLizableSearchResultSet(String query) {
        this.query = query;
    }

    public void toSAX(final ContentHandler handler) throws SAXException {
        if (null == handler) {
            throw new IllegalArgumentException("null handler");
        }
        handler.startPrefixMapping("", NAMESPACE);
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, SIZE, SIZE, "CDATA", Integer.toString(size()));
        XMLUtils.startElement(handler, NAMESPACE, RESOURCES, atts);
        XMLUtils.startElement(handler, NAMESPACE, QUERY);
        XMLUtils.data(handler, this.query);
        XMLUtils.endElement(handler, NAMESPACE, QUERY);
        for (SearchResult resource : this) {
            resource.toSAX(handler);
        }
        XMLUtils.endElement(handler, NAMESPACE, RESOURCES);
        handler.endPrefixMapping("");
    }
}
