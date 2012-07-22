package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import edu.stanford.irt.laneweb.model.Model;

/**
 * This DataBinder handles Model attributes that are related to the remote ip
 * address combined here in order to accommodate a change in the client's ip
 * during a session
 * 
 * @author ceyates
 */
public class RemoteProxyIPDataBinder implements DataBinder {

    private static final String X_FORWARDED_FOR = "X-FORWARDED-FOR";

    private ProxyLinks proxyLinks;

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String currentIP = getRemoteAddress(request);
        HttpSession session = request.getSession();
        boolean isSameIP = currentIP.equals(session.getAttribute(Model.REMOTE_ADDR));
        if (!isSameIP) {
            session.setAttribute(Model.REMOTE_ADDR, currentIP);
        }
        model.put(Model.REMOTE_ADDR, currentIP);
        IPGroup ipGroup = (IPGroup) session.getAttribute(Model.IPGROUP);
        if (ipGroup == null || !isSameIP) {
            ipGroup = IPGroup.getGroupForIP(currentIP);
            session.setAttribute(Model.IPGROUP, ipGroup);
        }
        model.put(Model.IPGROUP, ipGroup);
        Boolean proxy = null;
        String requestParameter = request.getParameter(Model.PROXY_LINKS);
        if (requestParameter != null) {
            proxy = Boolean.parseBoolean(requestParameter);
            session.setAttribute(Model.PROXY_LINKS, proxy);
        } else if (isSameIP) {
            proxy = (Boolean) session.getAttribute(Model.PROXY_LINKS);
        }
        if (proxy == null) {
            proxy = this.proxyLinks.getProxyLinks(ipGroup, currentIP);
            session.setAttribute(Model.PROXY_LINKS, proxy);
        }
        model.put(Model.PROXY_LINKS, proxy);
    }

    public void setProxyLinks(final ProxyLinks proxyLinks) {
        this.proxyLinks = proxyLinks;
    }

    protected String getRemoteAddress(final HttpServletRequest request) {
        // mod_proxy puts the real remote address in an x-forwarded-for
        // header
        // Load balancer also does this
        String header = request.getHeader(X_FORWARDED_FOR);
        if (header == null) {
            return request.getRemoteAddr();
        } else if (header.indexOf(',') > 0) {
            return header.substring(header.lastIndexOf(",") + 1, header.length()).trim();
        } else {
            return header;
        }
    }
}
