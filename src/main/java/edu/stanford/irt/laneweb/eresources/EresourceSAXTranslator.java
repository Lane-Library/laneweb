/**
 * 
 */
package edu.stanford.irt.laneweb.eresources;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.Link;
import edu.stanford.irt.eresources.Version;

/**
 * @author ceyates
 * 
 */
public class EresourceSAXTranslator {

    private static final String XHTML_NS = "http://www.w3.org/1999/xhtml";

    private static final String EMPTY_NS = "";

    private static final String DD = "dd";

    private static final String DT = "dt";

    private static final String UL = "ul";

    private static final String LI = "li";

    private static final String A = "a";

    private static final Attributes EMPTY_ATTS = new AttributesImpl();

    public void translate(final ContentHandler handler,
            final Eresource eresource) throws SAXException {
        if (null == handler) {
            throw new IllegalArgumentException("null handler");
        }
        if (null == eresource) {
            throw new IllegalArgumentException("null eresource");
        }
        handler.startElement(XHTML_NS, DT, DT, EMPTY_ATTS);
        char[] title = null != eresource.getTitle() ? eresource.getTitle()
                .toCharArray() : new char[0];
        handler.characters(title, 0, title.length);
        handler.endElement(XHTML_NS, DT, DT);
        handler.startElement(XHTML_NS, DD, DD, EMPTY_ATTS);
        handler.startElement(XHTML_NS, UL, UL, EMPTY_ATTS);
        StringBuffer sb = new StringBuffer();
        for (Version version : eresource.getVersions()) {
            Link getPasswordLink = null;
            for (Link link : version.getLinks()) {
                String label = link.getLabel();
                if ((null != label) && "get password".equalsIgnoreCase(label)) {
                    getPasswordLink = link;
                    break;
                }
            }
            for (Link link : version.getLinks()) {
                if (!link.equals(getPasswordLink)) {
                    handler.startElement(XHTML_NS, LI, LI, EMPTY_ATTS);
                    handleAnchor(handler, eresource, version, link,
                            getPasswordLink != null);
                    sb.setLength(0);
                    String instruction = link.getInstruction();
                    if ((null != instruction) && (instruction.length() > 0)) {
                        sb.append(' ').append(instruction);
                    }
                    String publisher = version.getPublisher();
                    if ((null != publisher) && (publisher.length() > 0)) {
                        sb.append(' ').append(publisher);
                    }
                    if (null != getPasswordLink) {
                        sb.append(' ');
                    }
                    if (sb.length() > 0) {
                        handler.characters(sb.toString().toCharArray(), 0, sb
                                .length());
                    }
                    if (null != getPasswordLink) {
                        AttributesImpl attributes = new AttributesImpl();
                        attributes.addAttribute(EMPTY_NS, "href", "href",
                                "CDATA", getPasswordLink.getUrl());
                        handler.startElement(XHTML_NS, A, A, attributes);
                        char[] getPassword = "get password".toCharArray();
                        handler.characters(getPassword, 0, getPassword.length);
                        handler.endElement(XHTML_NS, A, A);
                    }
                    handler.endElement(XHTML_NS, LI, LI);
                }
            }
        }
        handler.endElement(XHTML_NS, UL, UL);
        handler.endElement(XHTML_NS, DD, DD);
    }

    /**
     * @param handler
     * @param eresource
     * @param version
     * @param link
     * @throws SAXException
     */
    private void handleAnchor(final ContentHandler handler,
            final Eresource eresource, final Version version, final Link link,
            boolean hasGetPassword) throws SAXException {
        AttributesImpl attributes = new AttributesImpl();
        String proxyValue = version.isProxy() ? "proxy" : "noproxy";
        attributes
                .addAttribute(EMPTY_NS, "class", "class", "CDATA", proxyValue);
        String url = null != link.getUrl() ? link.getUrl() : "";
        attributes.addAttribute(EMPTY_NS, "href", "href", "CDATA", url);
        StringBuffer sb = new StringBuffer();
        sb.append(eresource.getTitle());
        if (null != version.getPublisher()) {
            sb.append(':').append(version.getPublisher());
        }
        if ((hasGetPassword && (version.getLinks().size() > 2))
                || (!hasGetPassword && (version.getLinks().size() > 1))) {
            sb.append(':').append(link.getLabel());
        }
        attributes.addAttribute(EMPTY_NS, "title", "title", "CDATA", sb
                .toString());
        handler.startElement(XHTML_NS, A, A, attributes);
        char[] linkText = getLinkText(eresource, version, link, hasGetPassword);
        handler.characters(linkText, 0, linkText.length);
        handler.endElement(XHTML_NS, A, A);
    }

    private char[] getLinkText(final Eresource eresource,
            final Version version, final Link link, final boolean hasGetPassword) {
        StringBuffer sb = new StringBuffer();
        if ((hasGetPassword && (version.getLinks().size() == 2))
                || (version.getLinks().size() == 1)) {
            String holdings = version.getSummaryHoldings();
            if ((null != holdings) && (holdings.length() > 0)) {
                sb.append(holdings);
            }
            String dates = version.getDates();
            if ((null != dates) && (dates.length() > 0)) {
                if (sb.length() != 0) {
                    sb.append(", ");
                }
                sb.append(dates);
            }
        }
        if (sb.length() == 0) {
            String label = link.getLabel();
            if ((null != label) && (label.length() > 0)) {
                sb.append(label);
            } else {
                sb.append(link.getUrl());
            }
        }
        String description = version.getDescription();
        if (null != description) {
            sb.append(' ').append(description);
        }
        return sb.toString().toCharArray();
    }

    // <li>
    // <a href="{sql:url}" title="{$title-publisher}" class="{$proxy_class}">
    // <xsl:choose>
    // <xsl:when
    // test="$holdings-length &gt; 0 and $dates-length &gt; 0">
    // <xsl:apply-templates select="sql:holdings"/>, <xsl:value-of
    // select="sql:dates"/>
    // </xsl:when>
    // <xsl:when test="$holdings-length &gt; 0">
    // <xsl:apply-templates select="sql:holdings"/>
    // </xsl:when>
    // <xsl:when test="$dates-length &gt; 0">
    // <xsl:value-of select="sql:dates"/>
    // </xsl:when>
    // <xsl:when test="string-length(sql:label) &gt; 0">
    // <xsl:value-of select="sql:label"/>
    // </xsl:when>
    // <xsl:otherwise>
    // <xsl:value-of select="sql:url"/>
    // </xsl:otherwise>
    // </xsl:choose>
    // <xsl:apply-templates select="sql:description"/>
    // </a>
    // <xsl:apply-templates select="sql:instruction|sql:publisher"/>
    // </li>
}
