package edu.stanford.irt.laneweb.proxy;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;

import edu.stanford.irt.laneweb.model.LanewebObjectModel;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelAware;
import edu.stanford.irt.laneweb.user.IPGroup;
import edu.stanford.irt.laneweb.user.Ticket;

public abstract class AbstractProxyLinkTransformer extends AbstractTransformer implements ModelAware {

    private static final String EZPROXY_LINK = "http://laneproxy.stanford.edu/login?user=";

    private static final String TICKET = "&ticket=";

    private static final String URL = "&url=";

    private static final String WEBAUTH_LINK = "/secure/login.html?url=";

    private String basePath;

    private IPGroup ipGroup;

    private String sunetid;

    private Ticket ticket;

    protected ProxyHostManager proxyHostManager;

    protected boolean proxyLinks;
    
    protected Model model;

    public void setProxyHostManager(final ProxyHostManager proxyHostManager) {
        this.proxyHostManager = proxyHostManager;
    }
    
    public void setModel(final Model model) {
        this.model = model;
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters params) {
        this.sunetid = this.model.getString(LanewebObjectModel.SUNETID);
        this.ticket = this.model.getObject(LanewebObjectModel.TICKET, Ticket.class);
        this.proxyLinks = this.model.getObject(LanewebObjectModel.PROXY_LINKS, Boolean.class, Boolean.FALSE);
        this.ipGroup = this.model.getObject(LanewebObjectModel.IPGROUP, IPGroup.class, IPGroup.OTHER);
        this.basePath = params.getParameter(LanewebObjectModel.BASE_PATH, this.model.getString(LanewebObjectModel.BASE_PATH));
    }

    protected String createProxyLink(final String link) {
        StringBuilder sb = new StringBuilder(128);
        if ("SHC".equals(this.ipGroup) || "LPCH".equals(this.ipGroup)) {
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