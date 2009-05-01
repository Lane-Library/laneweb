package edu.stanford.irt.laneweb.proxy;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.Transformer;
import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.laneweb.user.User;

public class ProxyLinkTransformer implements Transformer {
    
    private static final String EMPTY_STRING = "";

    private static final String EZPROXY_LINK = "http://laneproxy.stanford.edu/login?user=";

    private static final String HREF = "href";

    private static final String TICKET = "&ticket=";

    private static final String URL = "&url=";

    private static final String WEBAUTH_LINK = "/secure/login.html?url=";

    private ProxyHostManager proxyHostManager;

    private boolean proxyLinks;

    private String sunetid;

    private String ticket;

    private XMLConsumer xmlConsumer;

    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        this.xmlConsumer.characters(ch, start, length);
    }

    public void comment(final char[] ch, final int start, final int length) throws SAXException {
        this.xmlConsumer.comment(ch, start, length);
    }

    public void endCDATA() throws SAXException {
        this.xmlConsumer.endCDATA();
    }

    public void endDocument() throws SAXException {
        this.xmlConsumer.endDocument();
    }

    public void endDTD() throws SAXException {
        this.xmlConsumer.endDTD();
    }

    public void endElement(final String uri, final String localName, final String name) throws SAXException {
        this.xmlConsumer.endElement(uri, localName, name);
    }

    public void endEntity(final String name) throws SAXException {
        this.xmlConsumer.endEntity(name);
    }

    public void endPrefixMapping(final String prefix) throws SAXException {
        this.xmlConsumer.endPrefixMapping(prefix);
    }

    public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException {
        this.xmlConsumer.ignorableWhitespace(ch, start, length);
    }

    public void processingInstruction(final String target, final String data) throws SAXException {
        this.xmlConsumer.processingInstruction(target, data);
    }

    public void setConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
    }

    public void setDocumentLocator(final Locator locator) {
        this.xmlConsumer.setDocumentLocator(locator);
    }

    public void setProxyHostManager(final ProxyHostManager proxyHostManager) {
        this.proxyHostManager = proxyHostManager;
    }

    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters params) {
        this.sunetid = params.getParameter(User.SUNETID, EMPTY_STRING);
        this.ticket = params.getParameter(User.TICKET, EMPTY_STRING);
        this.proxyLinks = params.getParameterAsBoolean(User.PROXY_LINKS, false);
    }

    public void skippedEntity(final String name) throws SAXException {
        this.xmlConsumer.skippedEntity(name);
    }

    public void startCDATA() throws SAXException {
        this.xmlConsumer.startCDATA();
    }

    public void startDocument() throws SAXException {
        this.xmlConsumer.startDocument();
    }

    public void startDTD(final String name, final String publicId, final String systemId) throws SAXException {
        this.xmlConsumer.startDTD(name, publicId, systemId);
    }

    public void startElement(final String uri, final String localName, final String name, final Attributes atts) throws SAXException {
        if (this.proxyLinks) {
            if ("a".equals(localName)) {
                String link = atts.getValue(HREF);
                if (null != link && link.indexOf("http") == 0) {
                    if (this.proxyHostManager.isProxyableLink(link)) {
                        AttributesImpl newAttributes = new AttributesImpl(atts);
                        newAttributes.setValue(newAttributes.getIndex(HREF), createProxyLink(link));
                        this.xmlConsumer.startElement(uri, localName, name, newAttributes);
                        return;
                    }
                }
            }
        }
        this.xmlConsumer.startElement(uri, localName, name, atts);
    }

    public void startEntity(final String name) throws SAXException {
        this.xmlConsumer.startEntity(name);
    }

    public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
        this.xmlConsumer.startPrefixMapping(prefix, uri);
    }

    private String createProxyLink(final String link) {
        StringBuffer sb = new StringBuffer();
        if (EMPTY_STRING.equals(this.ticket) || EMPTY_STRING.equals(this.sunetid)) {
            sb.append(WEBAUTH_LINK);
        } else {
            sb.append(EZPROXY_LINK).append(this.sunetid).append(TICKET).append(this.ticket).append(URL);
        }
        sb.append(link);
        return sb.toString();
    }
}
