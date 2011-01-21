package edu.stanford.irt.laneweb.servlet.binding;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;

/**
 * @author ceyates $Id: ProxyLinks.java 80142 2010-11-21 21:27:38Z
 *         ceyates@stanford.edu $
 */
public class ProxyLinks {

    private List<Pattern> noProxyRegex;

    private List<Pattern> proxyRegex;

    public Boolean getProxyLinks(final IPGroup ipGroup, final String remoteAddress) {
        if (null != ipGroup && (IPGroup.SHC.equals(ipGroup) || IPGroup.LPCH.equals(ipGroup))) {
            return Boolean.TRUE;
        }
        return Boolean.valueOf(proxyLinks(remoteAddress));
    }

    public void setNoProxyRegex(final List<String> noProxyRegex) {
        this.noProxyRegex = new LinkedList<Pattern>();
        for (String pattern : noProxyRegex) {
            this.noProxyRegex.add(Pattern.compile(pattern));
        }
    }

    public void setProxyRegex(final List<String> proxyRegex) {
        this.proxyRegex = new LinkedList<Pattern>();
        for (String pattern : proxyRegex) {
            this.proxyRegex.add(Pattern.compile(pattern));
        }
    }

    private boolean isNoProxy(final String ip) {
        for (Pattern pattern : this.noProxyRegex) {
            if (pattern.matcher(ip).matches()) {
                return true;
            }
        }
        return false;
    }

    private boolean isProxy(final String ip) {
        for (Pattern pattern : this.proxyRegex) {
            if (pattern.matcher(ip).matches()) {
                return true;
            }
        }
        return false;
    }

    protected boolean proxyLinks(final String ip) {
        return !isNoProxy(ip) || isProxy(ip);
    }
}
