package edu.stanford.irt.laneweb.model;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.IPGroup;
import edu.stanford.irt.laneweb.LanewebConstants;
import edu.stanford.irt.laneweb.user.User;

/**
 * 
 * @author ceyates
 * $Id$
 */
public class ProxyLinks {

    private List<String> noProxyRegex;

    private List<String> proxyRegex;

    public Boolean proxyLinks(final HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            Boolean proxyLinks = (Boolean) session.getAttribute(LanewebObjectModel.PROXY_LINKS);
            if (proxyLinks != null) {
                return proxyLinks;
            }
        }
        IPGroup ipGroup = (IPGroup) session.getAttribute(LanewebObjectModel.IPGROUP);
        if (null != ipGroup && (IPGroup.SHC.equals(ipGroup) || IPGroup.LPCH.equals(ipGroup))) {
            return Boolean.TRUE;
        }
        String ip = request.getRemoteAddr();
        // mod_proxy puts the real remote address in an x-forwarded-for
        // header
        // Load balancer also does this
        String header = request.getHeader(LanewebConstants.X_FORWARDED_FOR);
        if (header != null) {
            ip = header;
        }
        return Boolean.valueOf(proxyLinks(ip));
    }

    public void setNoProxyRegex(final List<String> noProxyRegex) {
        this.noProxyRegex = noProxyRegex;
    }

    public void setProxyRegex(final List<String> proxyRegex) {
        this.proxyRegex = proxyRegex;
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
