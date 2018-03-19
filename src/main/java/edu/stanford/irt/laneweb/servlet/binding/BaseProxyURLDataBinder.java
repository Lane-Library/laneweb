package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.proxy.Ticket;

/**
 * A DataBinder that binds the base proxy url to the Model. It requires that the userid, ticket, proxyLink, ipgroup and
 * basePath, if present, have already been put in the model.
 */
public class BaseProxyURLDataBinder implements DataBinder {

    private static final String EZPROXY = "https://login.laneproxy.stanford.edu/login?user=";

    private static final String HOSPITAL = "https://login.laneproxy.stanford.edu/login?url=";

    private static final String PROXY_CREDENTIAL = "/secure/apps/proxy/credential?url=";

    private static final String TICKET = "&ticket=";

    private static final String URL = "&url=";

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        Boolean proxyLinks = ModelUtil.getObject(model, Model.PROXY_LINKS, Boolean.class, Boolean.FALSE);
        Boolean disasterMode = ModelUtil.getObject(model, Model.DISASTER_MODE, Boolean.class, Boolean.FALSE);
        if (disasterMode.equals(Boolean.TRUE) || proxyLinks.equals(Boolean.TRUE)) {
            StringBuilder baseProxyURL = new StringBuilder();
            IPGroup ipgroup = ModelUtil.getObject(model, Model.IPGROUP, IPGroup.class);
            if (disasterMode.equals(Boolean.TRUE) || IPGroup.SHC.equals(ipgroup) || IPGroup.LPCH.equals(ipgroup)) {
                baseProxyURL.append(HOSPITAL);
            } else {
                String userid = ModelUtil.getString(model, Model.USER_ID);
                Ticket ticket = ModelUtil.getObject(model, Model.TICKET, Ticket.class);
                if (ticket == null || userid == null) {
                    baseProxyURL.append(ModelUtil.getString(model, Model.BASE_PATH)).append(PROXY_CREDENTIAL);
                } else {
                    baseProxyURL.append(EZPROXY).append(userid).append(TICKET).append(ticket).append(URL);
                }
            }
            model.put(Model.BASE_PROXY_URL, baseProxyURL.toString());
        }
    }
}
