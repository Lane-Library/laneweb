package edu.stanford.irt.laneweb.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.IPGroup;
import edu.stanford.irt.laneweb.model.Model;

/**
 * This subclass of ProxyLinks doesn't look for or store the result in the session.
 * 
 * @author ceyates
 */
public class MobileProxyLinks extends ProxyLinks {

    @Override
    public Boolean getProxyLinks(final HttpServletRequest request, final HttpSession session, final IPGroup ipGroup, final String remoteAddress) {
        // first see if there is a proxy-links parameter and use that:
        String parameter = request.getParameter(Model.PROXY_LINKS);
        if (parameter != null) {
            return Boolean.parseBoolean(parameter);
        }
        if (null != ipGroup && (IPGroup.SHC.equals(ipGroup) || IPGroup.LPCH.equals(ipGroup))) {
            return Boolean.TRUE;
        }
        // finally use the remote address
        return Boolean.valueOf(proxyLinks(remoteAddress));
    }
}
