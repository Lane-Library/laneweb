package edu.stanford.irt.laneweb.eresources;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.Link;
import edu.stanford.irt.eresources.Version;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class EresourceXHTMLSAXStrategy extends AbstractXHTMLSAXStrategy<Eresource> {

    private static final String HVRTARG = "hvrTarg";

    private static final String PRIMARY_LINK = "primaryLink";

    public void toSAX(final Eresource eresource, final XMLConsumer xmlConsumer) {
        try {
            boolean processedFirstLink = false;
            for (Version version : eresource.getVersions()) {
                for (Link link : version.getLinks()) {
                    if (!processedFirstLink) {
                        processFirstLink(xmlConsumer, (TypedLink) link, version, eresource.getTitle());
                        processedFirstLink = true;
                    } else {
                        createSecondaryLink(xmlConsumer, version, (TypedLink) link);
                    }
                }
            }
            createMoreResultsLink(xmlConsumer, eresource);
            String description = eresource.getDescription();
            if (description != null && description.length() > 0) {
                createDivWithClass(xmlConsumer, HVRTARG, description);
            }
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private void createMoreResultsLink(final XMLConsumer xmlConsumer, final Eresource eresource) throws SAXException {
        startDivWithClass(xmlConsumer, "moreResults");
        String recordType = eresource.getRecordType();
        if ("bib".equals(recordType)) {
            StringBuilder sb = new StringBuilder();
            sb.append("http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=").append(eresource.getRecordId());
            createAnchor(xmlConsumer, sb.toString(), "Lane Catalog record");
        } else if ("auth".equals(recordType)) {
            createSpanWithClass(xmlConsumer, "sourceLink", "Lane Community Info File");
        } else if ("web".equals(recordType)) {
            createSpanWithClass(xmlConsumer, "sourceLink", "Lane Web Page");
        } else if ("class".equals(recordType)) {
            createSpanWithClass(xmlConsumer, "sourceLink", "Lane Class");
        }
        endDiv(xmlConsumer);
    }

    private void createSecondaryLink(final XMLConsumer xmlConsumer, final Version version, final TypedLink link)
            throws SAXException {
        boolean impactFactor = LinkType.IMPACTFACTOR.equals(link.getType());
        startDiv(xmlConsumer);
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
        if (!impactFactor) {
            createAnchorWithTitle(xmlConsumer, link.getUrl(), link.getLabel(), sb.toString());
        } else {
            createAnchor(xmlConsumer, link.getUrl(), sb.toString());
        }
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
        if (LinkType.GETPASSWORD.equals(link.getType())) {
            XMLUtils.data(xmlConsumer, " ");
            createAnchorWithTitle(xmlConsumer, "/secure/ejpw.html", "Get Password", "Get Password");
        }
        endDiv(xmlConsumer);
    }

    private void maybeAppend(final StringBuilder sb, final String string) {
        if (string != null && string.length() > 0) {
            if (sb.length() > 1) {
                sb.append(", ");
            }
            sb.append(string);
        }
    }

    private void processFirstLink(final XMLConsumer xmlConsumer, final TypedLink link, final Version firstVersion,
            final String title) throws SAXException {
        startDiv(xmlConsumer);
        createAnchorWithClassAndTitle(xmlConsumer, link.getUrl(), PRIMARY_LINK, title, title);
        StringBuilder sb = new StringBuilder(" ");
        String summaryHoldings = firstVersion.getSummaryHoldings();
        if (summaryHoldings != null) {
            sb.append(summaryHoldings);
        }
        maybeAppend(sb, firstVersion.getDates());
        maybeAppend(sb, firstVersion.getPublisher());
        maybeAppend(sb, firstVersion.getDescription());
        if (sb.length() == 1 && link.getLabel() != null) {
            sb.append(link.getLabel());
        }
        maybeAppend(sb, link.getInstruction());
        if (sb.length() > 1) {
            sb.append(" ");
            XMLUtils.data(xmlConsumer, sb.toString());
        }
        if (LinkType.GETPASSWORD.equals(link.getType())) {
            createAnchorWithTitle(xmlConsumer, "/secure/ejpw.html", "Get Password", "Get Password");
        }
        endDiv(xmlConsumer);
    }
}
