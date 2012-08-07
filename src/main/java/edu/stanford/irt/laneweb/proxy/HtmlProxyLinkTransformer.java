package edu.stanford.irt.laneweb.proxy;

import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class HtmlProxyLinkTransformer extends AbstractProxyLinkTransformer {

    private static final String ANCHOR = "a";

    private static final String CLASS = "class";

    private static final String HREF = "href";

    private static final String HTTP_SCHEME = "http";

    private XMLConsumer xmlConsumer;

    public HtmlProxyLinkTransformer(final ProxyHostManager proxyHostManager) {
        super(proxyHostManager);
    }

    @Override
    public void setConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
        super.setConsumer(xmlConsumer);
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
                // proxy if class contains proxy or isProxyableLink
                if ((null != clazz && clazz.contains("proxy")) || isProxyableLink(link)) {
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
