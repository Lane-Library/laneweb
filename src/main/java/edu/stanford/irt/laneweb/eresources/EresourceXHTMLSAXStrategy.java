package edu.stanford.irt.laneweb.eresources;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.Link;
import edu.stanford.irt.eresources.Version;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class EresourceXHTMLSAXStrategy implements SAXStrategy<Eresource> {

    private static final String A = "a";

    private static final String CDATA = "CDATA";

    private static final String CLASS = "class";

    private static final String DIV = "div";

    private static final String EMPTY_NS = "";

    private static final String HREF = "href";

    private static final String HVRTARG = "hvrTarg";

    private static final String LI = "li";

    private static final String PRIMARY_LINK = "primaryLink";

    private static final String SPAN = "span";

    private static final String TITLE = "title";

    private static final Comparator<Version> VERSION_COMPARATOR = new EresourceVersionComparator();

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    public void toSAX(final Eresource eresource, final XMLConsumer xmlConsumer) {
        try {
            XMLUtils.startElement(xmlConsumer, XHTML_NS, LI);
            List<Version> versions = new LinkedList<Version>(eresource.getVersions());
            Collections.sort(versions, VERSION_COMPARATOR);
            Version firstVersion = versions.get(0);
            createFirstVersionLinks(xmlConsumer, firstVersion, eresource.getTitle());
            for (int i = 1; i < versions.size(); i++) {
                Version version = versions.get(i);
                for (Link link : version.getLinks()) {
                    createSecondaryLink(xmlConsumer, version, link);
                }
            }
            createMoreResultsLink(xmlConsumer, eresource);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, LI);
            String description = eresource.getDescription();
            if (description != null && description.length() > 0) {
                AttributesImpl atts = new AttributesImpl();
                atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, HVRTARG);
                XMLUtils.startElement(xmlConsumer, XHTML_NS, LI, atts);
                XMLUtils.data(xmlConsumer, description);
                XMLUtils.endElement(xmlConsumer, XHTML_NS, LI);
            }
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void createFirstVersionLinks(final XMLConsumer xmlConsumer, final Version firstVersion, final String title)
            throws SAXException {
        Link getPassword = null;
        Link firstLink = null;
        List<Link> remainderLinks = new LinkedList<Link>();
        for (Link link : firstVersion.getLinks()) {
            if (getPassword == null && "get password".equalsIgnoreCase(link.getLabel())) {
                getPassword = link;
            } else if (firstLink == null) {
                firstLink = link;
            } else {
                remainderLinks.add(link);
            }
        }
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, PRIMARY_LINK);
        atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, firstLink.getUrl());
        XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
        XMLUtils.data(xmlConsumer, title);
        XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
        StringBuilder sb = new StringBuilder(" ");
        String summaryHoldings = firstVersion.getSummaryHoldings();
        if (summaryHoldings != null) {
            sb.append(summaryHoldings);
        }
        maybeAppend(sb, firstVersion.getDates());
        maybeAppend(sb, firstVersion.getPublisher());
        maybeAppend(sb, firstVersion.getDescription());
        if (sb.length() == 1 && firstLink.getLabel() != null) {
            sb.append(firstLink.getLabel());
        }
        maybeAppend(sb, firstLink.getInstruction());
        if (sb.length() > 1) {
            sb.append(" ");
            XMLUtils.data(xmlConsumer, sb.toString());
        }
        if (getPassword != null) {
            String label = getPassword.getLabel();
            atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, getPassword.getUrl());
            atts.addAttribute(EMPTY_NS, TITLE, TITLE, CDATA, label);
            XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
            XMLUtils.data(xmlConsumer, label);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
        }
        for (Link link : remainderLinks) {
            createSecondaryLink(xmlConsumer, firstVersion, link);
        }
    }

    private void createMoreResultsLink(final XMLConsumer xmlConsumer, final Eresource eresource) throws SAXException {
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "moreResults");
        XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
        atts = new AttributesImpl();
        String recordType = eresource.getRecordType();
        if ("bib".equals(recordType)) {
            StringBuilder sb = new StringBuilder();
            sb.append("http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=").append(eresource.getRecordId());
            atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, sb.toString());
            XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
            XMLUtils.data(xmlConsumer, "Lane Catalog record");
            XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
        } else {
            atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "sourceLink");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, SPAN, atts);
            if ("auth".equals(recordType)) {
                XMLUtils.data(xmlConsumer, "Lane Community Info File");
            } else if ("web".equals(recordType)) {
                XMLUtils.data(xmlConsumer, "Lane Web Page");
            } else if ("class".equals(recordType)) {
                XMLUtils.data(xmlConsumer, "Lane Class");
            }
            XMLUtils.endElement(xmlConsumer, XHTML_NS, SPAN);
        }
        XMLUtils.endElement(xmlConsumer, XHTML_NS, DIV);
    }

    private void createSecondaryLink(final XMLConsumer xmlConsumer, final Version version, final Link link)
            throws SAXException {
        boolean impactFactor = "impact factor".equalsIgnoreCase(link.getLabel());
        XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV);
        AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, link.getUrl());
        if (!impactFactor) {
            atts.addAttribute(EMPTY_NS, TITLE, TITLE, CDATA, link.getLabel());
        }
        XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
        StringBuilder sb = new StringBuilder();
        if (impactFactor) {
            sb.append("Impact Factor");
        } else {
            String summaryHoldings = version.getSummaryHoldings();
            if (summaryHoldings != null && version.getLinks().size() == 1) {
                sb.append(summaryHoldings);
                String dates = version.getDates();
                if (dates != null) {
                    sb.append(", ").append(dates);
                }
            } else {
                String label = link.getLabel();
                if (label != null) {
                    sb.append(link.getLabel());
                }
            }
            if (sb.length() == 0) {
                sb.append(link.getUrl());
            }
            String description = version.getDescription();
            if (description != null) {
                sb.append(" ").append(description);
            }
        }
        XMLUtils.data(xmlConsumer, sb.toString());
        XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
        sb.setLength(0);
        if (link.getInstruction() != null) {
            sb.append(" ").append(link.getInstruction());
        }
        if (version.getPublisher() != null) {
            sb.append(" ").append(version.getPublisher());
        }
        if (sb.length() > 0) {
            XMLUtils.data(xmlConsumer, sb.toString());
        }
        XMLUtils.endElement(xmlConsumer, XHTML_NS, DIV);
    }

    private void maybeAppend(final StringBuilder sb, final String string) {
        if (string != null && string.length() > 0) {
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(string);
        }
    }
}
