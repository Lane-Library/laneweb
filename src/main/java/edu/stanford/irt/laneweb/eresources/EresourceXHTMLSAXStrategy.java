package edu.stanford.irt.laneweb.eresources;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class EresourceXHTMLSAXStrategy extends AbstractXHTMLSAXStrategy<Eresource> {

    private static final String GET_PASSWORD = "Get Password";

    private static final String HVRTARG = "hvrTarg";

    private static final String PRIMARY_LINK = "primaryLink";

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
        startDiv(xmlConsumer);
        if (first) {
            createAnchorWithClassAndTitle(xmlConsumer, link.getUrl(), PRIMARY_LINK, linkText, linkText);
        } else {
            createAnchorWithTitle(xmlConsumer, link.getUrl(), link.getLabel(), linkText);
        }
        String text = link.getAdditionalText();
        if (text != null && text.length() > 0) {
            XMLUtils.data(xmlConsumer, text);
        }
        if (LinkType.GETPASSWORD.equals(link.getType())) {
            if (!first) {
                //the one getpassword link that is not a first link doesn't have trailing space in additional_text
                XMLUtils.data(xmlConsumer, " ");
            }
            createAnchorWithTitle(xmlConsumer, "/secure/ejpw.html", GET_PASSWORD, GET_PASSWORD);
        }
        endDiv(xmlConsumer);
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
}
