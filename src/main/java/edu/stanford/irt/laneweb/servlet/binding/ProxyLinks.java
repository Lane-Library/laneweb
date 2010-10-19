package edu.stanford.irt.laneweb.servlet.binding;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.IPGroup;
import edu.stanford.irt.laneweb.model.Model;

/**
 * @author ceyates $Id$
 */
public class ProxyLinks {

    private List<String> noProxyRegex;

    private List<String> proxyRegex;

    public Boolean getProxyLinks(final HttpServletRequest request, final HttpSession session, final IPGroup ipGroup,
            final String remoteAddress) {
        Boolean proxyLinks = null;
        Boolean sessionProxyLinks = null;
        // first see if there is a proxy-links parameter and use that:
        String parameter = request.getParameter(Model.PROXY_LINKS);
        if (parameter != null) {
            proxyLinks = Boolean.parseBoolean(parameter);
        }
        // if not see if it is in the session:
        if (proxyLinks == null) {
            sessionProxyLinks = (Boolean) session.getAttribute(Model.PROXY_LINKS);
            proxyLinks = sessionProxyLinks;
        }
        // if not see if the ip group is set to one of the hospital groups
        if (proxyLinks == null) {
            if (null != ipGroup && (IPGroup.SHC.equals(ipGroup) || IPGroup.LPCH.equals(ipGroup))) {
                proxyLinks = Boolean.TRUE;
            }
        }
        // finally use the remote address
        if (proxyLinks == null) {
            proxyLinks = Boolean.valueOf(proxyLinks(remoteAddress));
        }
        // put it in the session if it wasn't there or is different
        if (!proxyLinks.equals(sessionProxyLinks)) {
            session.setAttribute(Model.PROXY_LINKS, proxyLinks);
        }
        return proxyLinks;
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
