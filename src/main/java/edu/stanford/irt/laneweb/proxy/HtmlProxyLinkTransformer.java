package edu.stanford.irt.laneweb.proxy;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class HtmlProxyLinkTransformer extends AbstractProxyLinkTransformer {

    private static final String ANCHOR = "a";

    private static final String CLASS = "class";

    private static final String HREF = "href";

    private static final String HTTP_SCHEME = "http";

    private ProxyHostManager proxyHostManager;

    private XMLConsumer xmlConsumer;

    public HtmlProxyLinkTransformer(final ProxyHostManager proxyHostManager) {
        this.proxyHostManager = proxyHostManager;
    }

    @Override
    public void setXMLConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
        super.setXMLConsumer(xmlConsumer);
    }

    @Override
    public void startElement(final String uri, final String localName, final String name, final Attributes atts)
            throws SAXException {
        if (ANCHOR.equals(localName)) {
            String link = atts.getValue(HREF);
            // internal links not proxied
            if (null != link && link.indexOf(HTTP_SCHEME) == 0) {
                String clazz = atts.getValue(CLASS);
                // don't proxy if class contains noproxy
                if (null != clazz && clazz.contains("noproxy")) {
                    this.xmlConsumer.startElement(uri, localName, name, atts);
                    return;
                }
                // proxy if isProxyableLink
                if (this.proxyHostManager.isProxyableLink(link)) {
                    AttributesImpl newAttributes = new AttributesImpl(atts);
                    newAttributes.setValue(newAttributes.getIndex(HREF), createProxyLink(link));
                    this.xmlConsumer.startElement(uri, localName, name, newAttributes);
                    return;
                }
            }
        }
        this.xmlConsumer.startElement(uri, localName, name, atts);
    }
}
