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

    private static final String TITLE = "title";

    private static final Comparator<Version> VERSION_COMPARATOR = new EresourceVersionComparator();

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    public void toSAX(final Eresource eresource, final XMLConsumer xmlConsumer) {
        try {
            XMLUtils.startElement(xmlConsumer, XHTML_NS, LI);
            List<Version> versions = new LinkedList<Version>(eresource.getVersions());
            Collections.sort(versions, VERSION_COMPARATOR);
            Link getPassword = null;
            Link firstLink = null;
            Version firstVersion = versions.get(0);
            for (Link link : firstVersion.getLinks()) {
                if (getPassword == null && "get password".equalsIgnoreCase(link.getLabel())) {
                    getPassword = link;
                } else if (firstLink == null) {
                    firstLink = link;
                } else {
                    break;
                }
            }
            AttributesImpl atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, PRIMARY_LINK);
            atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, firstLink.getUrl());
            XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
            XMLUtils.data(xmlConsumer, eresource.getTitle());
            XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
            StringBuilder sb = new StringBuilder(" ");
            if (firstVersion.getSummaryHoldings() != null) {
                sb.append(firstVersion.getSummaryHoldings());
            }
            if (firstVersion.getDates() != null) {
                if (sb.length() > 1) {
                    sb.append(", ");
                }
                sb.append(firstVersion.getDates());
            }
            if (firstVersion.getPublisher() != null) {
                if (sb.length() > 1) {
                    sb.append(", ");
                }
                sb.append(firstVersion.getPublisher());
            }
            if (firstVersion.getDescription() != null) {
                if (sb.length() > 1) {
                    sb.append(", ");
                }
                sb.append(firstVersion.getDescription());
            }
            if (sb.length() == 1) {
                sb.append(firstLink.getLabel());
            }
            if (firstLink.getInstruction() != null) {
                if (sb.length() > 1) {
                    sb.append(", ");
                }
                sb.append(firstLink.getInstruction());
            }
            if (sb.length() > 1) {
                sb.append(" ");
                XMLUtils.data(xmlConsumer, sb.toString());
            }
            if (getPassword != null) {
                XMLUtils.data(xmlConsumer, " ");
                atts = new AttributesImpl();
                atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, getPassword.getUrl());
                atts.addAttribute(EMPTY_NS, TITLE, TITLE, CDATA, getPassword.getLabel());
                XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
                XMLUtils.data(xmlConsumer, getPassword.getLabel());
                XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
            }
            for (Version version : eresource.getVersions()) {
                for (Link link : version.getLinks()) {
                    if (!(link.equals(firstLink)) && !(link.equals(getPassword))) {
                        boolean impactFactor = "impact factor".equalsIgnoreCase(link.getLabel());
                        XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV);
                        atts = new AttributesImpl();
                        atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, link.getUrl());
                        if (!impactFactor) {
                            atts.addAttribute(EMPTY_NS, TITLE, TITLE, CDATA, link.getLabel());
                        }
                        XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
                        sb.setLength(0);
                        if (impactFactor) {
                            sb.append("Impact Factor");
                        } else {
                            if (version.getSummaryHoldings() != null) {
                                sb.append(version.getSummaryHoldings());
                            }
                            if (sb.length() > 0 && version.getDates() != null) {
                                sb.append(", ").append(version.getDates());
                            } else if (link.getLabel() != null) {
                                sb.append(link.getLabel());
                            }
                            if (sb.length() == 0) {
                                sb.append(link.getUrl());
                            }
                            if (version.getDescription() != null) {
                                sb.append(" ").append(version.getDescription());
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
                }
            }
            atts = new AttributesImpl();
            atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, "moreResults");
            XMLUtils.startElement(xmlConsumer, XHTML_NS, DIV, atts);
            atts = new AttributesImpl();
            sb.setLength(0);
            sb.append("http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=").append(eresource.getRecordId());
            atts.addAttribute(EMPTY_NS, HREF, HREF, CDATA, sb.toString());
            XMLUtils.startElement(xmlConsumer, XHTML_NS, A, atts);
            XMLUtils.data(xmlConsumer, "Lane Catalog record");
            XMLUtils.endElement(xmlConsumer, XHTML_NS, A);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, DIV);
            XMLUtils.endElement(xmlConsumer, XHTML_NS, LI);
            String description = eresource.getDescription();
            if (description != null && description.length() > 0) {
                atts = new AttributesImpl();
                atts.addAttribute(EMPTY_NS, CLASS, CLASS, CDATA, HVRTARG);
                XMLUtils.startElement(xmlConsumer, XHTML_NS, LI, atts);
                XMLUtils.data(xmlConsumer, description);
                XMLUtils.endElement(xmlConsumer, XHTML_NS, LI);
            }
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
