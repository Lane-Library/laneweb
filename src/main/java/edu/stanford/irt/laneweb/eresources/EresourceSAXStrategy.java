package edu.stanford.irt.laneweb.eresources;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.Resource;
import edu.stanford.irt.laneweb.util.XMLUtils;

/**
 * A SAXStrategy that converts an Eresource into http://lane.stanford.edu/resources/1.0
 * namespaced SAX events.
 */
public class EresourceSAXStrategy implements SAXStrategy<Eresource>, Resource {

    /**
     * Produce SAX events from a given Eresource to the given XMLConsumer
     */
    public void toSAX(final Eresource eresource, final XMLConsumer xmlConsumer) {
        try {
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, SCORE, SCORE, "CDATA", Integer.toString(eresource.getScore()));
            atts.addAttribute(EMPTY_NS, TYPE, TYPE, "CDATA", "eresource");
            XMLUtils.startElement(xmlConsumer, NAMESPACE, RESULT, atts);
            XMLUtils.createElementNS(xmlConsumer, NAMESPACE, ID, Integer.toString(eresource.getId()));
            XMLUtils.createElementNS(xmlConsumer, NAMESPACE, RECORD_ID, Integer.toString(eresource.getRecordId()));
            XMLUtils.createElementNS(xmlConsumer, NAMESPACE, RECORD_TYPE, eresource.getRecordType());
            XMLUtils.createElementNS(xmlConsumer, NAMESPACE, TITLE, eresource.getTitle());
            maybeCreateElement(xmlConsumer, DESCRIPTION, eresource.getDescription());
            for (Version version : eresource.getVersions()) {
                handleVersion(xmlConsumer, version);
            }
            XMLUtils.endElement(xmlConsumer, NAMESPACE, RESULT);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void handleLink(final XMLConsumer xmlConsumer, final Link link) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, TYPE, TYPE, "CDATA", link.getType().toString());
        XMLUtils.startElement(xmlConsumer, NAMESPACE, LINK, atts);
        maybeCreateElement(xmlConsumer, LABEL, link.getLabel());
        maybeCreateElement(xmlConsumer, URL, link.getUrl());
        maybeCreateElement(xmlConsumer, INSTRUCTION, link.getInstruction());
        XMLUtils.endElement(xmlConsumer, NAMESPACE, LINK);
    }

    private void handleVersion(final XMLConsumer xmlConsumer, final Version version) throws SAXException {
        XMLUtils.startElement(xmlConsumer, NAMESPACE, VERSION);
        maybeCreateElement(xmlConsumer, SUMMARY_HOLDINGS, version.getSummaryHoldings());
        maybeCreateElement(xmlConsumer, DATES, version.getDates());
        maybeCreateElement(xmlConsumer, PUBLISHER, version.getPublisher());
        maybeCreateElement(xmlConsumer, DESCRIPTION, version.getDescription());
        for (Link link : version.getLinks()) {
            handleLink(xmlConsumer, link);
        }
        XMLUtils.endElement(xmlConsumer, NAMESPACE, VERSION);
    }

    private void maybeCreateElement(final XMLConsumer xmlConsumer, final String name, final String value)
            throws SAXException {
        if (value != null && !"".equals(value)) {
            XMLUtils.createElementNS(xmlConsumer, NAMESPACE, name, value);
        }
    }
}
