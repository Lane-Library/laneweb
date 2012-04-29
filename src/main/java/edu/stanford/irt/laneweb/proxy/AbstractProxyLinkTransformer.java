package edu.stanford.irt.laneweb.proxy;

import java.io.Serializable;
import java.util.Map;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;

import edu.stanford.irt.laneweb.cocoon.AbstractTransformer;
import edu.stanford.irt.laneweb.cocoon.ModelAware;
import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public abstract class AbstractProxyLinkTransformer extends AbstractTransformer implements CacheableProcessingComponent, ModelAware {

    private static final String EZPROXY_LINK = "http://laneproxy.stanford.edu/login?user=";

    private static final String TICKET = "&ticket=";

    private static final String URL = "&url=";

    private static final String WEBAUTH_LINK = "/secure/apps/proxy/credential?url=";

    private String basePath;

    private IPGroup ipGroup;

    private ProxyHostManager proxyHostManager;

    private boolean proxyLinks;

    private String sunetid;

    private Ticket ticket;

    public Serializable getKey() {
        return Boolean.toString(this.proxyLinks);
    }

    public SourceValidity getValidity() {
        SourceValidity validity = null;
        if (!this.proxyLinks) {
            validity = NOPValidity.SHARED_INSTANCE;
        }
        return validity;
    }

    public void setModel(final Map<String, Object> model) {
        this.sunetid = ModelUtil.getString(model, Model.SUNETID);
        this.ticket = ModelUtil.getObject(model, Model.TICKET, Ticket.class);
        this.proxyLinks = ModelUtil.getObject(model, Model.PROXY_LINKS, Boolean.class, Boolean.FALSE);
        this.ipGroup = ModelUtil.getObject(model, Model.IPGROUP, IPGroup.class, IPGroup.OTHER);
        this.basePath = ModelUtil.getString(model, Model.BASE_PATH);
    }

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

    protected ProxyHostManager getProxyHostManager() {
        return this.proxyHostManager;
    }

    protected boolean proxyLinks() {
        return this.proxyLinks;
    }
}
