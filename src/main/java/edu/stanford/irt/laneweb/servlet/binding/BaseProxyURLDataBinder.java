package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.proxy.Ticket;

/**
 * A DataBinder that binds the base proxy url to the Model. It requires that the
 * sunetid, ticket, proxyLink, ipgroup and basePath, if present, have already
 * been put in the model.
 */
public class BaseProxyURLDataBinder implements DataBinder {

    private static final String EZPROXY = "http://laneproxy.stanford.edu/login?user=";

    private static final String HOSPITAL = "http://laneproxy.stanford.edu/login?url=";

    private static final String TICKET = "&ticket=";

    private static final String URL = "&url=";

    private static final String WEBAUTH = "/secure/apps/proxy/credential?url=";
    
    private boolean disasterMode;

    public BaseProxyURLDataBinder(final boolean drMode) {
        this.disasterMode = drMode;
    }
    
    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        Boolean proxyLinks = ModelUtil.getObject(model, Model.PROXY_LINKS, Boolean.class, Boolean.FALSE);
        if (this.disasterMode || proxyLinks.equals(Boolean.TRUE)) {
            StringBuilder baseProxyURL = new StringBuilder();
            IPGroup ipgroup = ModelUtil.getObject(model, Model.IPGROUP, IPGroup.class);
            if (this.disasterMode || IPGroup.SHC.equals(ipgroup) || IPGroup.LPCH.equals(ipgroup)) {
                baseProxyURL.append(HOSPITAL);
            } else {
                String sunetid = ModelUtil.getString(model, Model.SUNETID);
                Ticket ticket = ModelUtil.getObject(model, Model.TICKET, Ticket.class);
                if (ticket == null || sunetid == null) {
                    baseProxyURL.append(ModelUtil.getString(model, Model.BASE_PATH)).append(WEBAUTH);
                } else {
                    baseProxyURL.append(EZPROXY).append(sunetid).append(TICKET).append(ticket).append(URL);
                }
            }
            model.put(Model.BASE_PROXY_URL, baseProxyURL.toString());
        }
    }
}
