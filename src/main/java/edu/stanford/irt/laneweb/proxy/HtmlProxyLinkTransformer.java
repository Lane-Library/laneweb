package edu.stanford.irt.laneweb.proxy;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class HtmlProxyLinkTransformer extends AbstractProxyLinkTransformer {

    private static final String ANCHOR = "a";

    private static final String CLASS = "class";

    private static final String HREF = "href";

    private static final String HTTP_SCHEME = "http";

    @Override
    public void startElement(final String uri, final String localName, final String name, final Attributes atts) throws SAXException {
        if (this.proxyLinks) {
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
                    if ((null != clazz && clazz.contains("proxy")) || this.proxyHostManager.isProxyableLink(link)) {
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
}
