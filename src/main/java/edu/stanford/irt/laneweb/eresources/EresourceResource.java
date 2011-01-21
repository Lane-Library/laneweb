package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import org.apache.cocoon.xml.XMLUtils;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.Link;
import edu.stanford.irt.eresources.Version;
import edu.stanford.irt.laneweb.resource.Resource;

public class EresourceResource implements Resource {

    private static final Comparator<Version> VERSION_COMPARATOR = new EresourceVersionComparator();

    protected Eresource eresource;

    public EresourceResource(final Eresource eresource) {
        this.eresource = eresource;
    }

    public void toSAX(final ContentHandler handler) throws SAXException {
        handleEresource(handler);
    }

    @Override
    public String toString() {
        return this.eresource.toString();
    }

    private void handleEresource(final ContentHandler handler) throws SAXException {
        // TODO: returning result element for now ... turn into displayable?
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, SCORE, SCORE, "CDATA", Integer.toString(this.eresource.getScore()));
        atts.addAttribute(EMPTY_NS, TYPE, TYPE, "CDATA", "eresource");
        XMLUtils.startElement(handler, NAMESPACE, RESULT, atts);
        XMLUtils.createElementNS(handler, NAMESPACE, ID, Integer.toString(this.eresource.getId()));
        XMLUtils.createElementNS(handler, NAMESPACE, RECORD_ID, Integer.toString(this.eresource.getRecordId()));
        XMLUtils.createElementNS(handler, NAMESPACE, RECORD_TYPE, this.eresource.getRecordType());
        XMLUtils.createElementNS(handler, NAMESPACE, TITLE, this.eresource.getTitle());
        maybeCreateElement(handler, DESCRIPTION, this.eresource.getDescription());
        XMLUtils.startElement(handler, NAMESPACE, VERSIONS);
        Collection<Version> versions = new TreeSet<Version>(VERSION_COMPARATOR);
        versions.addAll(this.eresource.getVersions());
        for (Version version : versions) {
            handleVersion(handler, version);
        }
        XMLUtils.endElement(handler, NAMESPACE, VERSIONS);
        XMLUtils.endElement(handler, NAMESPACE, RESULT);
    }

    private void handleLink(final ContentHandler handler, final Link link) throws SAXException {
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
        XMLUtils.startElement(handler, NAMESPACE, LINK, atts);
        maybeCreateElement(handler, LABEL, label);
        maybeCreateElement(handler, URL, link.getUrl());
        maybeCreateElement(handler, INSTRUCTION, link.getInstruction());
        XMLUtils.endElement(handler, NAMESPACE, LINK);
    }

    private void handleVersion(final ContentHandler handler, final Version version) throws SAXException {
        XMLUtils.startElement(handler, NAMESPACE, VERSION);
        maybeCreateElement(handler, SUMMARY_HOLDINGS, version.getSummaryHoldings());
        maybeCreateElement(handler, DATES, version.getDates());
        maybeCreateElement(handler, PUBLISHER, version.getPublisher());
        maybeCreateElement(handler, DESCRIPTION, version.getDescription());
        XMLUtils.startElement(handler, NAMESPACE, LINKS);
        for (Link link : version.getLinks()) {
            handleLink(handler, link);
        }
        XMLUtils.endElement(handler, NAMESPACE, LINKS);
        XMLUtils.endElement(handler, NAMESPACE, VERSION);
    }

    private void maybeCreateElement(final ContentHandler handler, final String name, final String value) throws SAXException {
        if (value != null && !"".equals(value)) {
            XMLUtils.createElementNS(handler, NAMESPACE, name, value);
        }
    }
}
