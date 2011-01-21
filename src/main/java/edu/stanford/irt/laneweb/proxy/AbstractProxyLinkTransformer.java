package edu.stanford.irt.laneweb.proxy;

import edu.stanford.irt.laneweb.cocoon.AbstractTransformer;
import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

// $Id: AbstractProxyLinkTransformer.java 80764 2010-12-15 21:47:39Z
// ceyates@stanford.edu $
public abstract class AbstractProxyLinkTransformer extends AbstractTransformer {

    private static final String EZPROXY_LINK = "http://laneproxy.stanford.edu/login?user=";

    private static final String TICKET = "&ticket=";

    private static final String URL = "&url=";

    private static final String WEBAUTH_LINK = "/secure/apps/proxy/credential?url=";

    private String basePath;

    private IPGroup ipGroup;

    private String sunetid;

    private Ticket ticket;

    protected ProxyHostManager proxyHostManager;

    protected boolean proxyLinks;

    public void setProxyHostManager(final ProxyHostManager proxyHostManager) {
        this.proxyHostManager = proxyHostManager;
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

    @Override
    protected void initialize() {
        this.sunetid = ModelUtil.getString(this.model, Model.SUNETID);
        this.ticket = ModelUtil.getObject(this.model, Model.TICKET, Ticket.class);
        this.proxyLinks = ModelUtil.getObject(this.model, Model.PROXY_LINKS, Boolean.class, Boolean.FALSE);
        this.ipGroup = ModelUtil.getObject(this.model, Model.IPGROUP, IPGroup.class, IPGroup.OTHER);
        this.basePath = this.parameterMap.containsKey(Model.BASE_PATH) ? this.parameterMap.get(Model.BASE_PATH) : ModelUtil
                .getString(this.model, Model.BASE_PATH);
    }
}