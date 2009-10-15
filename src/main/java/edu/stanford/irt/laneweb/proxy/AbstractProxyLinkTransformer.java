package edu.stanford.irt.laneweb.proxy;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;

import edu.stanford.irt.laneweb.user.User;

public abstract class AbstractProxyLinkTransformer extends AbstractTransformer {

    private static final String EMPTY_STRING = "";

    private static final String EZPROXY_LINK = "http://laneproxy.stanford.edu/login?user=";

    private static final String TICKET = "&ticket=";

    private static final String URL = "&url=";

    private static final String WEBAUTH_LINK = "/secure/login.html?url=";

    private String basePath;

    private String ipGroup;

    private String sunetid;

    private String ticket;

    protected ProxyHostManager proxyHostManager;

    protected boolean proxyLinks;

    public AbstractProxyLinkTransformer() {
        super();
    }

    public void setProxyHostManager(final ProxyHostManager proxyHostManager) {
        this.proxyHostManager = proxyHostManager;
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters params) {
        this.sunetid = params.getParameter(User.SUNETID, EMPTY_STRING);
        this.ticket = params.getParameter(User.TICKET, EMPTY_STRING);
        this.proxyLinks = params.getParameterAsBoolean(User.PROXY_LINKS, false);
        this.ipGroup = params.getParameter(User.IPGROUP, "OTHER");
        this.basePath = params.getParameter("base-path", "");
    }

    protected String createProxyLink(final String link) {
        StringBuilder sb = new StringBuilder(128);
        if ("SHC".equals(this.ipGroup) || "LPCH".equals(this.ipGroup)) {
            sb.append("http://laneproxy.stanford.edu/login?url=");
        } else if (EMPTY_STRING.equals(this.ticket) || EMPTY_STRING.equals(this.sunetid)) {
            sb.append(this.basePath).append(WEBAUTH_LINK);
        } else {
            sb.append(EZPROXY_LINK).append(this.sunetid).append(TICKET).append(this.ticket).append(URL);
        }
        sb.append(link);
        return sb.toString();
    }
}