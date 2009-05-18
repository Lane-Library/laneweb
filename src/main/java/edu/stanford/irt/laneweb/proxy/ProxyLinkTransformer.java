package edu.stanford.irt.laneweb.proxy;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.laneweb.user.User;

public class ProxyLinkTransformer extends AbstractTransformer {

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

    public void setProxyHostManager(final ProxyHostManager proxyHostManager) {
        this.proxyHostManager = proxyHostManager;
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters params) {
        this.sunetid = params.getParameter(User.SUNETID, EMPTY_STRING);
        this.ticket = params.getParameter(User.TICKET, EMPTY_STRING);
        this.proxyLinks = params.getParameterAsBoolean(User.PROXY_LINKS, false);
    }

    @Override
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
