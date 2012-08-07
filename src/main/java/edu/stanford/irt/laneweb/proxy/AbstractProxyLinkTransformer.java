package edu.stanford.irt.laneweb.proxy;

import java.util.Map;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.pipeline.transform.AbstractTransformer;
import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public abstract class AbstractProxyLinkTransformer extends AbstractTransformer implements ModelAware {

    private static final String EZPROXY_LINK = "http://laneproxy.stanford.edu/login?user=";

    private static final String TICKET = "&ticket=";

    private static final String URL = "&url=";

    private static final String WEBAUTH_LINK = "/secure/apps/proxy/credential?url=";

    private String basePath;

    private IPGroup ipGroup;

    private ProxyHostManager proxyHostManager;

    private String sunetid;

    private Ticket ticket;

    public AbstractProxyLinkTransformer(final ProxyHostManager proxyHostManager) {
        this.proxyHostManager = proxyHostManager;
    }

    public void setModel(final Map<String, Object> model) {
        this.sunetid = ModelUtil.getString(model, Model.SUNETID);
        this.ticket = ModelUtil.getObject(model, Model.TICKET, Ticket.class);
        this.ipGroup = ModelUtil.getObject(model, Model.IPGROUP, IPGroup.class, IPGroup.OTHER);
        this.basePath = ModelUtil.getString(model, Model.BASE_PATH);
    }

    protected String createProxyLink(final String link) {
        StringBuilder sb = new StringBuilder(128);
        if (IPGroup.SHC.equals(this.ipGroup) || IPGroup.LPCH.equals(this.ipGroup)) {
            sb.append("http://laneproxy.stanford.edu/login?url=");
        } else if (null == this.ticket || null == this.sunetid) {
            sb.append(this.basePath).append(WEBAUTH_LINK);
        } else {
            sb.append(EZPROXY_LINK).append(this.sunetid).append(TICKET).append(this.ticket).append(URL);
        }
        sb.append(link);
        return sb.toString();
    }

    protected boolean isProxyableLink(final String link) {
        return this.proxyHostManager.isProxyableLink(link);
    }
}
