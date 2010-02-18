package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.TreeSet;

import org.apache.cocoon.xml.XMLUtils;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.Link;
import edu.stanford.irt.eresources.Version;

public class XHTMLizableEresourceList implements XMLizable {

    private static final String A = "a";

    private static final String BODY = "body";

    private static final String DD = "dd";

    private static final String DL = "dl";

    private static final String DT = "dt";

    private static final String EMPTY_NS = "";

    private static final String HEAD = "head";

    private static final String HTML = "html";

    private static final String LI = "li";

    private static final String TITLE = "title";

    private static final String UL = "ul";

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    private Collection<Eresource> eresources;
    
    private static final EresourceVersionComparator VERSION_COMPARATOR = new EresourceVersionComparator();

    public XHTMLizableEresourceList(final Collection<Eresource> eresources) {
        if (null == eresources) {
            throw new IllegalArgumentException("null eresources");
        }
        this.eresources = eresources;
    }

    public void toSAX(final ContentHandler handler) throws SAXException {
        if (null == handler) {
            throw new IllegalArgumentException("null handler");
        }
        handler.startPrefixMapping("", XHTML_NS);
        XMLUtils.startElement(handler, XHTML_NS, HTML);
        XMLUtils.startElement(handler, XHTML_NS, HEAD);
        XMLUtils.startElement(handler, XHTML_NS, TITLE);
        XMLUtils.data(handler, "EresourcesList");
        XMLUtils.endElement(handler, XHTML_NS, TITLE);
        XMLUtils.endElement(handler, XHTML_NS, HEAD);
        XMLUtils.startElement(handler, XHTML_NS, BODY);
        XMLUtils.startElement(handler, XHTML_NS, DL);
        for (Eresource eresource : this.eresources) {
            handleEresource(handler, eresource);
        }
        XMLUtils.endElement(handler, XHTML_NS, DL);
        XMLUtils.endElement(handler, XHTML_NS, BODY);
        XMLUtils.endElement(handler, XHTML_NS, HTML);
        handler.endPrefixMapping("");
    }

    private void appendText(final StringBuffer sb, final String text, final String separator) {
        if (null == text || text.length() == 0) {
            return;
        }
        if (sb.length() > 0) {
            sb.append(separator);
        }
        sb.append(text);
    }

    private String getLinkText(final Version version, final Link link, final boolean hasGetPassword) {
        StringBuffer sb = new StringBuffer();
        if ((hasGetPassword && version.getLinks().size() == 2) || version.getLinks().size() == 1) {
            appendText(sb, version.getSummaryHoldings(), null);
            appendText(sb, version.getDates(), ", ");
        }
        if (sb.length() == 0) {
            appendText(sb, link.getLabel(), null);
        }
        if (sb.length() == 0) {
            appendText(sb, link.getUrl(), null);
        }
        appendText(sb, version.getDescription(), " ");
        return sb.toString();
    }

    /**
     * @param handler
     * @param eresource
     * @param version
     * @param link
     * @throws SAXException
     */
    private void handleAnchor(final ContentHandler handler, final Eresource eresource, final Version version,
            final Link link, final boolean hasGetPassword) throws SAXException {
        AttributesImpl attributes = new AttributesImpl();
        String proxyValue = version.isProxy() ? "proxy" : "noproxy";
        attributes.addAttribute(EMPTY_NS, "class", "class", "CDATA", proxyValue);
        String url = null != link.getUrl() ? link.getUrl() : "";
        attributes.addAttribute(EMPTY_NS, "href", "href", "CDATA", url);
        StringBuffer sb = new StringBuffer();
        sb.append(eresource.getTitle());
        if (null != version.getPublisher()) {
            sb.append(':').append(version.getPublisher());
        }
        if ((hasGetPassword && (version.getLinks().size() > 2)) || (!hasGetPassword && (version.getLinks().size() > 1))) {
            sb.append(':').append(link.getLabel());
        }
        attributes.addAttribute(EMPTY_NS, "title", "title", "CDATA", sb.toString());
        XMLUtils.startElement(handler, XHTML_NS, A, attributes);
        XMLUtils.data(handler, getLinkText(version, link, hasGetPassword));
        XMLUtils.endElement(handler, XHTML_NS, A);
    }

    private void handleEresource(final ContentHandler handler, final Eresource eresource) throws SAXException {
        Collection<Version> versions = new TreeSet<Version>(VERSION_COMPARATOR);
        String title = eresource.getTitle();
        // TODO shouldn't have to check for this here
        if (null == title) {
            title = "";
        }
        XMLUtils.startElement(handler, XHTML_NS, DT);
        XMLUtils.data(handler, title);
        XMLUtils.endElement(handler, XHTML_NS, DT);
        XMLUtils.startElement(handler, XHTML_NS, DD);
        XMLUtils.startElement(handler, XHTML_NS, UL);
        Version impactFactor = null;
        versions.addAll(eresource.getVersions());
        for (Version version : versions) {
            if (null == impactFactor && version.getLinks().size() == 1) {
                Link link = version.getLinks().iterator().next();
                if ("Impact Factor".equals(link.getLabel())) {
                    impactFactor = version;
                    continue;
                }
            }
            Link passwordLink = null;
            for (Link link : version.getLinks()) {
                String label = link.getLabel();
                if ((null != label) && "get password".equalsIgnoreCase(label)) {
                    passwordLink = link;
                    break;
                }
            }
            for (Link link : version.getLinks()) {
                if (!link.equals(passwordLink)) {
                    handleLink(handler, eresource, version, passwordLink, link);
                }
            }
        }
        if (null != impactFactor) {
            for (Link link : impactFactor.getLinks()) {
                handleLink(handler, eresource, impactFactor, null, link);
            }
        }
        XMLUtils.endElement(handler, XHTML_NS, UL);
        XMLUtils.endElement(handler, XHTML_NS, DD);
    }

    private void handleLink(final ContentHandler handler, final Eresource eresource, final Version version,
            final Link passwordLink, final Link link) throws SAXException {
        StringBuffer sb = new StringBuffer();
        XMLUtils.startElement(handler, XHTML_NS, LI);
        handleAnchor(handler, eresource, version, link, passwordLink != null);
        sb.setLength(0);
        String instruction = link.getInstruction();
        if ((null != instruction) && (instruction.length() > 0)) {
            sb.append(' ').append(instruction);
        }
        String publisher = version.getPublisher();
        if ((null != publisher) && (publisher.length() > 0)) {
            sb.append(' ').append(publisher);
        }
        if (null != passwordLink) {
            sb.append(' ');
        }
        if (sb.length() > 0) {
            XMLUtils.data(handler, sb.toString());
        }
        if (null != passwordLink) {
            AttributesImpl attributes = new AttributesImpl();
            attributes.addAttribute(EMPTY_NS, "href", "href", "CDATA", passwordLink.getUrl());
            XMLUtils.startElement(handler, XHTML_NS, A, attributes);
            XMLUtils.data(handler, "get password");
            XMLUtils.endElement(handler, XHTML_NS, A);
        }
        XMLUtils.endElement(handler, LI);
    }
}

