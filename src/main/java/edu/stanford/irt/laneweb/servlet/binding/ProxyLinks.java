package edu.stanford.irt.laneweb.servlet.binding;

import java.util.List;

import edu.stanford.irt.laneweb.ipgroup.CIDRRange;
import edu.stanford.irt.laneweb.ipgroup.IPGroup;

public class ProxyLinks {

    private List<CIDRRange> noProxyCIDRRange;

    private List<CIDRRange> proxyCIDRRange;

    public ProxyLinks(final List<CIDRRange> proxyCIDRRange, final List<CIDRRange> noProxyCIDRRange) {
        this.proxyCIDRRange = proxyCIDRRange;
        this.noProxyCIDRRange = noProxyCIDRRange;
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
        for (CIDRRange cidrRange : this.noProxyCIDRRange) {
            if (cidrRange.contains(ip)) {
                return true;
            }
        }
        return false;
    }

    private boolean isProxy(final String ip) {
        for (CIDRRange cidrRange : this.proxyCIDRRange) {
            if (cidrRange.contains(ip)) {
                return true;
            }
        }
        return false;
    }
}
