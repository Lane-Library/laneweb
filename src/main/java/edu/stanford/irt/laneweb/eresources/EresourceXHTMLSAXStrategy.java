package edu.stanford.irt.laneweb.eresources;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class EresourceXHTMLSAXStrategy extends AbstractXHTMLSAXStrategy<Eresource> {

    private static final String CATALOG_BASE = "http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=";

    private static final String GET_PASSWORD = "Get Password";

    private static final String HVRTARG = "hvrTarg";

    private static final String PRIMARY_LINK = "primaryLink";

    private static final String SOURCE_LINK = "sourceLink";

    @Override
    public void toSAX(final Eresource eresource, final XMLConsumer xmlConsumer) {
        try {
            boolean first = true;
            for (Link link : eresource.getLinks()) {
                createLink(xmlConsumer, link, first);
                first = false;
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

    private void createLink(final XMLConsumer xmlConsumer, final Link link, final boolean first) throws SAXException {
        String linkText = link.getLinkText();
        String additionalText = link.getAdditionalText();
        startDiv(xmlConsumer);
        if (first) {
            createAnchorWithClassAndTitle(xmlConsumer, link.getUrl(), PRIMARY_LINK, linkText, linkText);
            if (link.getHoldingsAndDates() != null) {
                XMLUtils.data(xmlConsumer, " " + link.getHoldingsAndDates());
            }
            maybeCreateGetPasswordLink(xmlConsumer, link);
            if (additionalText != null && !additionalText.isEmpty()) {
                startDiv(xmlConsumer);
                XMLUtils.data(xmlConsumer, additionalText);
                endDiv(xmlConsumer);
            }
        } else {
            String url = link.getUrl();
            if (null != url && url.startsWith(CATALOG_BASE)) {
                XMLUtils.data(xmlConsumer, "Also available: ");
                linkText = "Print – " + linkText;
            }
            createAnchorWithTitle(xmlConsumer, url, link.getLabel(), linkText);
            if (additionalText != null && !additionalText.isEmpty()) {
                XMLUtils.data(xmlConsumer, " " + additionalText);
            }
            maybeCreateGetPasswordLink(xmlConsumer, link);
        }
        endDiv(xmlConsumer);
    }

    private void createMoreResultsLink(final XMLConsumer xmlConsumer, final Eresource eresource) throws SAXException {
        startDivWithClass(xmlConsumer, "moreResults");
        String recordType = eresource.getRecordType();
        if ("bib".equals(recordType)) {
            if (eresource.getPrimaryType().contains("Print")) {
                createSpanWithClass(xmlConsumer, SOURCE_LINK, "Print Material");
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(CATALOG_BASE).append(eresource.getRecordId());
                createAnchor(xmlConsumer, sb.toString(), "Lane Catalog record");
            }
        } else if ("auth".equals(recordType)) {
            createSpanWithClass(xmlConsumer, SOURCE_LINK, "Lane Community Info File");
        } else if ("web".equals(recordType)) {
            createSpanWithClass(xmlConsumer, SOURCE_LINK, "Lane Web Page");
        } else if ("class".equals(recordType)) {
            createSpanWithClass(xmlConsumer, SOURCE_LINK, "Lane Class");
        }
        endDiv(xmlConsumer);
    }

    private void maybeCreateGetPasswordLink(final XMLConsumer xmlConsumer, final Link link) throws SAXException {
        if (LinkType.GETPASSWORD.equals(link.getType())) {
            XMLUtils.data(xmlConsumer, " ");
            createAnchorWithTitle(xmlConsumer, "/secure/ejpw.html", GET_PASSWORD, GET_PASSWORD);
        }
    }
}
