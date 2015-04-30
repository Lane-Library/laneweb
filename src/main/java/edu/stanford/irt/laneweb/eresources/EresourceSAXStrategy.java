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
public class EresourceSAXStrategy implements SAXStrategy<Eresource> {

    /**
     * Produce SAX events from a given Eresource to the given XMLConsumer
     */
    @Override
    public void toSAX(final Eresource eresource, final XMLConsumer xmlConsumer) {
        try {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(Resource.EMPTY_NS, Resource.SCORE, Resource.SCORE, Resource.CDATA,
                    Integer.toString(eresource.getScore()));
            atts.addAttribute(Resource.EMPTY_NS, Resource.TYPE, Resource.TYPE, Resource.CDATA, "eresource");
            XMLUtils.startElement(xmlConsumer, Resource.NAMESPACE, Resource.RESULT, atts);
            XMLUtils.createElementNS(xmlConsumer, Resource.NAMESPACE, Resource.ID, Integer.toString(eresource.getId()));
            XMLUtils.createElementNS(xmlConsumer, Resource.NAMESPACE, Resource.RECORD_ID,
                    Integer.toString(eresource.getRecordId()));
            XMLUtils.createElementNS(xmlConsumer, Resource.NAMESPACE, Resource.RECORD_TYPE, eresource.getRecordType());
            XMLUtils.createElementNS(xmlConsumer, Resource.NAMESPACE, Resource.TITLE, eresource.getTitle());
            maybeCreateElement(xmlConsumer, "primaryType", eresource.getPrimaryType());
            XMLUtils.createElementNS(xmlConsumer, Resource.NAMESPACE, "total", Integer.toString(eresource.getTotal()));
            XMLUtils.createElementNS(xmlConsumer, Resource.NAMESPACE, "available",
                    Integer.toString(eresource.getAvailable()));
            maybeCreateElement(xmlConsumer, Resource.DESCRIPTION, eresource.getDescription());
            for (Link link : eresource.getLinks()) {
                handleLink(xmlConsumer, link);
            }
            XMLUtils.endElement(xmlConsumer, Resource.NAMESPACE, Resource.RESULT);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void handleLink(final XMLConsumer xmlConsumer, final Link link) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(Resource.EMPTY_NS, Resource.TYPE, Resource.TYPE, Resource.CDATA, link.getType().toString());
        XMLUtils.startElement(xmlConsumer, Resource.NAMESPACE, Resource.LINK, atts);
        maybeCreateElement(xmlConsumer, Resource.LABEL, link.getLabel());
        maybeCreateElement(xmlConsumer, Resource.LINK_TEXT, link.getLinkText());
        maybeCreateElement(xmlConsumer, Resource.URL, link.getUrl());
        maybeCreateElement(xmlConsumer, Resource.ADDITIONAL_TEXT, link.getAdditionalText());
        maybeCreateElement(xmlConsumer, "holdings-dates", link.getHoldingsAndDates());
        XMLUtils.endElement(xmlConsumer, Resource.NAMESPACE, Resource.LINK);
    }

    private void maybeCreateElement(final XMLConsumer xmlConsumer, final String name, final String value)
            throws SAXException {
        if (value != null && !"".equals(value)) {
            XMLUtils.createElementNS(xmlConsumer, Resource.NAMESPACE, name, value);
        }
    }
}
