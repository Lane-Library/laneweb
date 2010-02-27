package edu.stanford.irt.laneweb.servlet;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.IPGroup;
import edu.stanford.irt.laneweb.LanewebConstants;
import edu.stanford.irt.laneweb.model.Model;

/**
 * @author ceyates $Id$
 */
public class ProxyLinks {

    private List<String> noProxyRegex;

    private List<String> proxyRegex;

    public void setNoProxyRegex(final List<String> noProxyRegex) {
        this.noProxyRegex = noProxyRegex;
    }

    public void setProxyRegex(final List<String> proxyRegex) {
        this.proxyRegex = proxyRegex;
    }

    public void setupProxyLinks(final HttpServletRequest request) {
        Boolean proxyLinks = null;
        // first see if there is a proxy-links parameter and use that:
        String parameter = request.getParameter(Model.PROXY_LINKS);
        if (parameter != null) {
            proxyLinks = Boolean.parseBoolean(parameter);
        }
        // if not see if it is in the session:
        HttpSession session = request.getSession(false);
        if (proxyLinks == null && session != null) {
            proxyLinks = (Boolean) session.getAttribute(Model.PROXY_LINKS);
        }
        // if not see if the ip group is set to one of the hospital groups
        if (proxyLinks == null && session != null) {
            IPGroup ipGroup = (IPGroup) session.getAttribute(Model.IPGROUP);
            if (null != ipGroup && (IPGroup.SHC.equals(ipGroup) || IPGroup.LPCH.equals(ipGroup))) {
                proxyLinks = Boolean.TRUE;
            }
        }
        // finally use the ip address
        if (proxyLinks == null) {
            String ip = request.getRemoteAddr();
            // mod_proxy puts the real remote address in an x-forwarded-for
            // header
            // Load balancer also does this
            String header = request.getHeader(LanewebConstants.X_FORWARDED_FOR);
            if (header != null) {
                ip = header;
            }
            proxyLinks = Boolean.valueOf(proxyLinks(ip));
        }
        // if there is a session put it there.
        if (session != null) {
            session.setAttribute(Model.PROXY_LINKS, proxyLinks);
        }
        // put it as a request attribute for later use . . .
        request.setAttribute(Model.PROXY_LINKS, proxyLinks);
    }

    private boolean isNoProxy(final String ip) {
        for (String regex : this.noProxyRegex) {
            if (ip.matches(regex)) {
                return true;
            }
        }
        return false;
    }

    private boolean isProxy(final String ip) {
        for (String regex : this.proxyRegex) {
            if (ip.matches(regex)) {
                return true;
            }
        }
        return false;
    }

    protected boolean proxyLinks(final String ip) {
        return !isNoProxy(ip) || isProxy(ip);
    }
}
