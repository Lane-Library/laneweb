package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.eresources.model.Eresource;
import edu.stanford.irt.laneweb.eresources.model.Link;
import edu.stanford.irt.laneweb.eresources.model.Version;
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
            atts.addAttribute(Resource.EMPTY_NS, Resource.TYPE, Resource.TYPE, Resource.CDATA, "eresource");
            XMLUtils.startElement(xmlConsumer, Resource.NAMESPACE, Resource.RESULT, atts);
            XMLUtils.createElementNS(xmlConsumer, Resource.NAMESPACE, Resource.ID, eresource.getId());
            XMLUtils.createElementNS(xmlConsumer, Resource.NAMESPACE, Resource.RECORD_ID, eresource.getRecordId());
            XMLUtils.createElementNS(xmlConsumer, Resource.NAMESPACE, Resource.RECORD_TYPE, eresource.getRecordType());
            XMLUtils.createElementNS(xmlConsumer, Resource.NAMESPACE, Resource.TITLE, eresource.getTitle());
            XMLUtils.createElementNS(xmlConsumer, Resource.NAMESPACE, Resource.IS_AN_EXACT_MATCH, eresource.getIsAnExactMatch());
            maybeCreateElement(xmlConsumer, "primaryType", eresource.getPrimaryType());
            XMLUtils.createElementNS(xmlConsumer, Resource.NAMESPACE, "total", Integer.toString(eresource.getTotal()));
            XMLUtils.createElementNS(xmlConsumer, Resource.NAMESPACE, "available",
                    Integer.toString(eresource.getAvailable()));
            maybeCreateElement(xmlConsumer, Resource.DESCRIPTION, eresource.getDescription());
            maybeCreateElement(xmlConsumer, Resource.AUTHOR, eresource.getPublicationAuthorsText());
            maybeCreateElement(xmlConsumer, Resource.PUBLICATION_TEXT, eresource.getPublicationText());
            maybeCreateElement(xmlConsumer, "doi", eresource.getDois());
            maybeCreateElement(xmlConsumer, "isbn", eresource.getIsbns());
            maybeCreateElement(xmlConsumer, "issn", eresource.getIssns());
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
        Version version = link.getVersion();
        maybeCreateElement(xmlConsumer, "holdings-dates", version.getHoldingsAndDates());
        maybeCreateElement(xmlConsumer, "publisher", version.getPublisher());
        maybeCreateElement(xmlConsumer, "version-text", version.getAdditionalText());
        maybeCreateElement(xmlConsumer, "callnumber", version.getCallnumber());
        maybeCreateElement(xmlConsumer, "locationCode", version.getLocationCode());
        maybeCreateElement(xmlConsumer, "locationName", version.getLocationName());
        maybeCreateElement(xmlConsumer, "locationUrl", version.getLocationUrl());
        int[] itemCount = version.getItemCount();
        if (null != itemCount) {
            maybeCreateElement(xmlConsumer, "total", Integer.toString(itemCount[0]));
            maybeCreateElement(xmlConsumer, "available", Integer.toString(itemCount[1]));
            maybeCreateElement(xmlConsumer, "checkedOut", Integer.toString(itemCount[2]));
        }
        XMLUtils.endElement(xmlConsumer, Resource.NAMESPACE, Resource.LINK);
    }

    private void maybeCreateElement(final XMLConsumer xmlConsumer, final String name, final Collection<String> values)
            throws SAXException {
        if (values != null && !values.isEmpty()) {
            for (String value : values) {
                maybeCreateElement(xmlConsumer, name, value);
            }
        }
    }

    private void maybeCreateElement(final XMLConsumer xmlConsumer, final String name, final String value)
            throws SAXException {
        if (value != null && !"".equals(value)) {
            XMLUtils.createElementNS(xmlConsumer, Resource.NAMESPACE, name, value);
        }
    }
}
