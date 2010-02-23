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
import edu.stanford.irt.laneweb.Resource;
import edu.stanford.irt.laneweb.search.QueryTermPattern;


public class EresourceResource implements Resource {
    
    private static final Comparator<Version> VERSION_COMPARATOR = new EresourceVersionComparator();
    
    private Eresource eresource;

    public EresourceResource(Eresource eresource) {
        this.eresource = eresource;
    }

    public void toSAX(ContentHandler handler) throws SAXException {
        handleEresource(handler);
    }


    private void handleEresource(final ContentHandler handler) throws SAXException {
        // TODO: returning result element for now ... turn into displayable?
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, SCORE, SCORE, "CDATA", Integer.toString(this.eresource.getScore()));
        atts.addAttribute(EMPTY_NS, TYPE, TYPE, "CDATA", "eresource");
        XMLUtils.startElement(handler, NAMESPACE, RESULT, atts);
        XMLUtils.createElementNS(handler, NAMESPACE, ID, Integer.toString(this.eresource.getId()));
        XMLUtils.createElementNS(handler, NAMESPACE, TITLE, this.eresource.getTitle());
        XMLUtils.startElement(handler, NAMESPACE, VERSIONS);
        Collection<Version> versions = new TreeSet<Version>(VERSION_COMPARATOR);
        versions.addAll(this.eresource.getVersions());
        for (Version version : versions) {
            handleVersion(handler, version);
        }
        XMLUtils.endElement(handler, VERSIONS);
        XMLUtils.endElement(handler, RESULT);
    }

    private void handleVersion(final ContentHandler handler, final Version version) throws SAXException {
        XMLUtils.startElement(handler, NAMESPACE, VERSION);
        if (null != version.getSummaryHoldings()) {
            XMLUtils.createElementNS(handler, NAMESPACE, SUMMARY_HOLDINGS, version.getSummaryHoldings());
        }
        if (null != version.getDates()) {
            XMLUtils.createElementNS(handler, NAMESPACE, DATES, version.getDates());
        }
        if (null != version.getPublisher()) {
            XMLUtils.createElementNS(handler, NAMESPACE, PUBLISHER, version.getPublisher());
        }
        if (null != version.getDescription()) {
            XMLUtils.createElementNS(handler, NAMESPACE, DESCRIPTION, version.getDescription());
        }
        XMLUtils.startElement(handler, NAMESPACE, LINKS);
        for (Link link : version.getLinks()) {
            handleLink(handler, link);
        }
        XMLUtils.endElement(handler, LINKS);
        XMLUtils.endElement(handler, VERSION);
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
        if (null != link.getLabel()) {
            XMLUtils.createElementNS(handler, NAMESPACE, LABEL, label);
        }
        if (null != link.getUrl()) {
            XMLUtils.createElementNS(handler, NAMESPACE, URL, link.getUrl());
        }
        if (null != link.getInstruction()) {
            XMLUtils.createElementNS(handler, NAMESPACE, INSTRUCTION, link.getInstruction());
        }
        XMLUtils.endElement(handler, LINK);
    }
}
