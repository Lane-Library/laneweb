package edu.stanford.irt.laneweb.eresources;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.Resource;
import edu.stanford.irt.laneweb.util.XMLUtils;

/**
 * A SAXStrategy that converts an Eresource into http://lane.stanford.edu/resources/1.0 namespaced SAX events.
 */
public class EresourceSAXStrategy implements SAXStrategy<Eresource>, Resource {

    /**
     * Produce SAX events from a given Eresource to the given XMLConsumer
     */
    public void toSAX(final Eresource eresource, final XMLConsumer xmlConsumer) {
        try {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, SCORE, SCORE, CDATA, Integer.toString(eresource.getScore()));
            atts.addAttribute(EMPTY_NS, TYPE, TYPE, CDATA, "eresource");
            XMLUtils.startElement(xmlConsumer, NAMESPACE, RESULT, atts);
            XMLUtils.createElementNS(xmlConsumer, NAMESPACE, ID, Integer.toString(eresource.getId()));
            XMLUtils.createElementNS(xmlConsumer, NAMESPACE, RECORD_ID, Integer.toString(eresource.getRecordId()));
            XMLUtils.createElementNS(xmlConsumer, NAMESPACE, RECORD_TYPE, eresource.getRecordType());
            XMLUtils.createElementNS(xmlConsumer, NAMESPACE, TITLE, eresource.getTitle());
            maybeCreateElement(xmlConsumer, "primaryType", eresource.getPrimaryType());
            XMLUtils.createElementNS(xmlConsumer, NAMESPACE, "total", Integer.toString(eresource.getTotal()));
            XMLUtils.createElementNS(xmlConsumer, NAMESPACE, "available", Integer.toString(eresource.getAvailable()));
            maybeCreateElement(xmlConsumer, DESCRIPTION, eresource.getDescription());
            for (Link link : eresource.getLinks()) {
                handleLink(xmlConsumer, link);
            }
            XMLUtils.endElement(xmlConsumer, NAMESPACE, RESULT);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void handleLink(final XMLConsumer xmlConsumer, final Link link) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, TYPE, TYPE, CDATA, link.getType().toString());
        XMLUtils.startElement(xmlConsumer, NAMESPACE, LINK, atts);
        maybeCreateElement(xmlConsumer, LABEL, link.getLabel());
        maybeCreateElement(xmlConsumer, LINK_TEXT, link.getLinkText());
        maybeCreateElement(xmlConsumer, URL, link.getUrl());
        maybeCreateElement(xmlConsumer, ADDITIONAL_TEXT, link.getAdditionalText());
        maybeCreateElement(xmlConsumer, "publisher", link.getPublisher());
        XMLUtils.endElement(xmlConsumer, NAMESPACE, LINK);
    }

    private void maybeCreateElement(final XMLConsumer xmlConsumer, final String name, final String value)
            throws SAXException {
        if (value != null && !"".equals(value)) {
            XMLUtils.createElementNS(xmlConsumer, NAMESPACE, name, value);
        }
    }
}
