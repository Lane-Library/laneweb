package edu.stanford.irt.laneweb.servlet.binding;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import edu.stanford.irt.laneweb.ipgroup.IPGroup;

public class ProxyLinks {

    private List<Pattern> noProxyRegex;

    private List<Pattern> proxyRegex;

    public ProxyLinks(final List<String> proxyRegex, final List<String> noProxyRegex) {
        this.proxyRegex = new LinkedList<Pattern>();
        for (String pattern : proxyRegex) {
            this.proxyRegex.add(Pattern.compile(pattern));
        }
        this.noProxyRegex = new LinkedList<Pattern>();
        for (String pattern : noProxyRegex) {
            this.noProxyRegex.add(Pattern.compile(pattern));
        }
    }

    public Boolean getProxyLinks(final IPGroup ipGroup, final String remoteAddress) {
        if (IPGroup.SHC.equals(ipGroup) || IPGroup.LPCH.equals(ipGroup)) {
            return Boolean.TRUE;
        }
        return Boolean.valueOf(proxyLinks(remoteAddress));
    }

    protected boolean proxyLinks(final String ip) {
        return !isNoProxy(ip) || isProxy(ip);
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
}
