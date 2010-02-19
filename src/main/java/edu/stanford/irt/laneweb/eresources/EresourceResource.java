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
import edu.stanford.irt.laneweb.AbstractResource;
import edu.stanford.irt.laneweb.search.QueryTermPattern;


public class EresourceResource extends AbstractResource {
    
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
        handleElement(handler, ID, Integer.toString(this.eresource.getId()));
        handleElement(handler, TITLE, this.eresource.getTitle());
        handleElement(handler, SORT_TITLE, QueryTermPattern.NON_FILING_PATTERN.matcher(
                this.eresource.getTitle()).replaceFirst(QueryTermPattern.EMPTY));
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
            handleElement(handler, SUMMARY_HOLDINGS, version.getSummaryHoldings());
        }
        if (null != version.getDates()) {
            handleElement(handler, DATES, version.getDates());
        }
        if (null != version.getPublisher()) {
            handleElement(handler, PUBLISHER, version.getPublisher());
        }
        if (null != version.getDescription()) {
            handleElement(handler, DESCRIPTION, version.getDescription());
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
            handleElement(handler, LABEL, label);
        }
        if (null != link.getUrl()) {
            handleElement(handler, URL, link.getUrl());
        }
        if (null != link.getInstruction()) {
            handleElement(handler, INSTRUCTION, link.getInstruction());
        }
        XMLUtils.endElement(handler, LINK);
    }
}
