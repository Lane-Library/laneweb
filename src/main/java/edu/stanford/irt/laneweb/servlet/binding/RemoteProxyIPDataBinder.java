package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import edu.stanford.irt.laneweb.model.Model;

/**
 * This DataBinder handles Model attributes that are related to the remote ip address combined here in order to
 * accommodate a change in the client's ip during a session
 *
 * @author ceyates
 */
public class RemoteProxyIPDataBinder implements DataBinder {

    private static final String X_FORWARDED_FOR = "X-FORWARDED-FOR";

    private ProxyLinks proxyLinks;

    public RemoteProxyIPDataBinder(final ProxyLinks proxyLinks) {
        this.proxyLinks = proxyLinks;
    }

    @Override
    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String currentIP = getRemoteAddress(request);
        IPGroup ipGroup;
        Boolean proxy;
        String requestParameter = request.getParameter(Model.PROXY_LINKS);
        HttpSession session = request.getSession();
        boolean isSameIP = currentIP.equals(session.getAttribute(Model.REMOTE_ADDR));
        if (!isSameIP) {
            session.setAttribute(Model.REMOTE_ADDR, currentIP);
        }
        ipGroup = (IPGroup) session.getAttribute(Model.IPGROUP);
        if (ipGroup == null || !isSameIP) {
            ipGroup = IPGroup.getGroupForIP(currentIP);
            session.setAttribute(Model.IPGROUP, ipGroup);
        }
        proxy = (Boolean) session.getAttribute(Model.PROXY_LINKS);
        if (requestParameter != null || proxy == null || !isSameIP) {
            if (requestParameter == null) {
                proxy = this.proxyLinks.getProxyLinks(ipGroup, currentIP);
            } else {
                proxy = Boolean.valueOf(requestParameter);
            }
            session.setAttribute(Model.PROXY_LINKS, proxy);
        }
        model.put(Model.REMOTE_ADDR, currentIP);
        model.put(Model.IPGROUP, ipGroup);
        model.put(Model.PROXY_LINKS, proxy);
    }

    protected String getRemoteAddress(final HttpServletRequest request) {
        // mod_proxy puts the real remote address in an x-forwarded-for
        // header
        // Load balancer also does this
        String header = request.getHeader(X_FORWARDED_FOR);
        if (header == null) {
            return request.getRemoteAddr();
        } else {
            return header.split(",")[0];
        }
    }
}
