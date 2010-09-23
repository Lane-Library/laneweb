package edu.stanford.irt.laneweb.search;

import java.util.ArrayList;
import java.util.TreeSet;

import org.apache.cocoon.xml.XMLUtils;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.laneweb.Resource;

/**
 * @author ryanmax $Id$
 */
@SuppressWarnings("serial")
public class XMLizableSearchResultSet extends TreeSet<SearchResult> implements Resource, XMLizable {

    private String query = null;

    public XMLizableSearchResultSet(final String query) {
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
        handleSearchContentCounts(handler);
        for (SearchResult resource : this) {
            resource.toSAX(handler);
        }
        XMLUtils.endElement(handler, NAMESPACE, RESOURCES);
        handler.endPrefixMapping("");
    }

    private void handleSearchContentCounts(final ContentHandler handler) throws SAXException {
        XMLUtils.startElement(handler, NAMESPACE, CONTENT_HIT_COUNTS);
        ArrayList<String> countedResources = new ArrayList<String>();
        for (SearchResult resource : this) {
            if (resource instanceof ContentResultSearchResult) {
                ContentResultSearchResult cr = (ContentResultSearchResult) resource;
                if (null != cr.getResourceId() && !countedResources.contains(cr.getResourceId())) {
                    countedResources.add(cr.getResourceId());
                    AttributesImpl atts = new AttributesImpl();
                    atts.addAttribute(EMPTY_NS, RESOURCE_ID, RESOURCE_ID, "CDATA", cr.getResourceId());
                    atts.addAttribute(EMPTY_NS, RESOURCE_HITS, RESOURCE_HITS, "CDATA", cr.getResourceHits());
                    atts.addAttribute(EMPTY_NS, RESOURCE_URL, RESOURCE_URL, "CDATA", cr.getResourceUrl());
                    XMLUtils.startElement(handler, NAMESPACE, RESOURCE, atts);
                    XMLUtils.endElement(handler, NAMESPACE, RESOURCE);
                }
            }
        }
        XMLUtils.endElement(handler, NAMESPACE, CONTENT_HIT_COUNTS);
    }
}
