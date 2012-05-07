package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.Link;
import edu.stanford.irt.eresources.Version;
import edu.stanford.irt.laneweb.resource.Resource;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class EresourceResource implements Resource {

    private static final Comparator<Version> VERSION_COMPARATOR = new EresourceVersionComparator();

    protected Eresource eresource;

    public EresourceResource(final Eresource eresource) {
        this.eresource = eresource;
    }

    public void toSAX(final XMLConsumer xmlConsumer) throws SAXException {
        handleEresource(xmlConsumer);
    }

    @Override
    public String toString() {
        return this.eresource.toString();
    }

    private void handleEresource(final XMLConsumer xmlConsumer) throws SAXException {
        // TODO: returning result element for now ... turn into displayable?
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, SCORE, SCORE, "CDATA", Integer.toString(this.eresource.getScore()));
        atts.addAttribute(EMPTY_NS, TYPE, TYPE, "CDATA", "eresource");
        XMLUtils.startElement(xmlConsumer, NAMESPACE, RESULT, atts);
        XMLUtils.createElementNS(xmlConsumer, NAMESPACE, ID, Integer.toString(this.eresource.getId()));
        XMLUtils.createElementNS(xmlConsumer, NAMESPACE, RECORD_ID, Integer.toString(this.eresource.getRecordId()));
        XMLUtils.createElementNS(xmlConsumer, NAMESPACE, RECORD_TYPE, this.eresource.getRecordType());
        XMLUtils.createElementNS(xmlConsumer, NAMESPACE, TITLE, this.eresource.getTitle());
        maybeCreateElement(xmlConsumer, DESCRIPTION, this.eresource.getDescription());
        XMLUtils.startElement(xmlConsumer, NAMESPACE, VERSIONS);
        Collection<Version> versions = new TreeSet<Version>(VERSION_COMPARATOR);
        versions.addAll(this.eresource.getVersions());
        for (Version version : versions) {
            handleVersion(xmlConsumer, version);
        }
        XMLUtils.endElement(xmlConsumer, NAMESPACE, VERSIONS);
        XMLUtils.endElement(xmlConsumer, NAMESPACE, RESULT);
    }

    private void handleLink(final XMLConsumer xmlConsumer, final Link link) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        String label = link.getLabel();
        String type;
        if ((null != label) && "get password".equalsIgnoreCase(label)) {
            type = "getPassword";
        } else if ((null != label) && "impact factor".equalsIgnoreCase(label)) {
            type = "impactFactor";
        } else {
            type = "normal";
        }
        atts.addAttribute(EMPTY_NS, TYPE, TYPE, "CDATA", type);
        XMLUtils.startElement(xmlConsumer, NAMESPACE, LINK, atts);
        maybeCreateElement(xmlConsumer, LABEL, label);
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
        XMLUtils.startElement(xmlConsumer, NAMESPACE, LINKS);
        for (Link link : version.getLinks()) {
            handleLink(xmlConsumer, link);
        }
        XMLUtils.endElement(xmlConsumer, NAMESPACE, LINKS);
        XMLUtils.endElement(xmlConsumer, NAMESPACE, VERSION);
    }

    private void maybeCreateElement(final XMLConsumer xmlConsumer, final String name, final String value) throws SAXException {
        if (value != null && !"".equals(value)) {
            XMLUtils.createElementNS(xmlConsumer, NAMESPACE, name, value);
        }
    }
}
