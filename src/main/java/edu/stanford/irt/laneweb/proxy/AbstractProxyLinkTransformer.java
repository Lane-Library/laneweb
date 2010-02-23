package edu.stanford.irt.laneweb.proxy;

import edu.stanford.irt.laneweb.IPGroup;
import edu.stanford.irt.laneweb.cocoon.AbstractTransformer;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;

// $Id$
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
    
    protected void initialize() {
        this.sunetid = this.model.getString(LanewebObjectModel.SUNETID);
        this.ticket = this.model.getObject(LanewebObjectModel.TICKET, Ticket.class);
        this.proxyLinks = this.model.getObject(LanewebObjectModel.PROXY_LINKS, Boolean.class, Boolean.FALSE);
        this.ipGroup = this.model.getObject(LanewebObjectModel.IPGROUP, IPGroup.class, IPGroup.OTHER);
        this.basePath = this.parameterMap.containsKey(LanewebObjectModel.BASE_PATH) ? 
                this.parameterMap.get(LanewebObjectModel.BASE_PATH) :
                    this.model.getString(LanewebObjectModel.BASE_PATH);
    }

    protected String createProxyLink(final String link) {
        StringBuilder sb = new StringBuilder(128);
        if (IPGroup.SHC.equals(this.ipGroup) || IPGroup.LPCH.equals(this.ipGroup)) {
            sb.append("http://laneproxy.stanford.edu/login?url=");
        } else if (null == this.ticket || null == this.sunetid ) {
            sb.append(this.basePath).append(WEBAUTH_LINK);
        } else {
            sb.append(EZPROXY_LINK).append(this.sunetid).append(TICKET).append(this.ticket).append(URL);
        }
        sb.append(link);
        return sb.toString();
    }
}