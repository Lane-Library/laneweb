package edu.stanford.irt.laneweb.inputmodule;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.LanewebConstants;
import edu.stanford.irt.laneweb.user.User;


public class ProxyLinks extends AbstractInputModule {

    private List<String> noProxyRegex;

    private List<String> proxyRegex;

    public void setNoProxyRegex(final List<String> noProxyRegex) {
        this.noProxyRegex = noProxyRegex;
    }

    public void setProxyRegex(final List<String> proxyRegex) {
        this.proxyRegex = proxyRegex;
    }

    @Override
    protected Object doGetAttribute(String key, User user, HttpServletRequest request) {
        if (null != user.getProxyLinks()) {
            return user.getProxyLinks();
        }
        String ip = request.getRemoteAddr();
        // mod_proxy puts the real remote address in an x-forwarded-for
        // header
        // Load balancer also does this
        String header = request.getHeader(LanewebConstants.X_FORWARDED_FOR);
        if (header != null) {
            ip = header;
        }
        return new Boolean(proxyLinks(ip));
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
