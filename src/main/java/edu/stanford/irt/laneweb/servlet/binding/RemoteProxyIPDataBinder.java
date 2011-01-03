package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;
import edu.stanford.irt.laneweb.model.Model;

/**
 * This DataBinder handles Model attributes that are related to the remote ip address
 * combined here in order to accommodate a change in the client's ip during a session
 * @author ceyates
 *
 */
public class RemoteProxyIPDataBinder implements DataBinder {

    private static final String PAVA_IP = "152.131.10.128";
    
    private static final String X_FORWARDED_FOR = "X-FORWARDED-FOR";
    
    private ProxyLinks proxyLinks;

    public void bind(Map<String, Object> model, HttpServletRequest request) {
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
        Boolean proxyLinks = null;
        String requestParameter = request.getParameter(Model.PROXY_LINKS);
        if (requestParameter != null) {
            proxyLinks = Boolean.parseBoolean(requestParameter);
            session.setAttribute(Model.PROXY_LINKS, proxyLinks);
        } else if (isSameIP) {
            proxyLinks = (Boolean) session.getAttribute(Model.PROXY_LINKS);
        } else {
            proxyLinks = this.proxyLinks.getProxyLinks(ipGroup, currentIP);
            session.setAttribute(Model.PROXY_LINKS, proxyLinks);
        }
        model.put(Model.PROXY_LINKS, proxyLinks);
    }
    
    protected String getRemoteAddress(HttpServletRequest request) {
        // mod_proxy puts the real remote address in an x-forwarded-for
        // header
        // Load balancer also does this
        String header = request.getHeader(X_FORWARDED_FOR);
        if (header == null) {
            return request.getRemoteAddr();
        }
        // PAVA proxy address is not first IP in x-forwarded-for list 
        else if (header.indexOf(PAVA_IP) > -1) {
            return PAVA_IP;
        }
        else if (header.indexOf(',') > 0) {
            return header.substring(0, header.indexOf(','));
        } else {
            return header;
        }
    }

    public void setProxyLinks(ProxyLinks proxyLinks) {
        this.proxyLinks = proxyLinks;
    }
}
