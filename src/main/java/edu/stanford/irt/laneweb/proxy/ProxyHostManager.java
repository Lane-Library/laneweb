package edu.stanford.irt.laneweb.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class ProxyHostManager {

    private Set<String> proxyHosts;

    public ProxyHostManager() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("ezproxy-servers.txt")));
        this.proxyHosts = new HashSet<String>();
        String proxyHost = null;
        try {
            while ((proxyHost = reader.readLine()) != null) {
                this.proxyHosts.add(proxyHost);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isProxyableHost(final String host) {
        if (null == host) {
            throw new IllegalArgumentException("null host");
        }
        return this.proxyHosts.contains(host);
    }

    public boolean isProxyableLink(final String link) {
        if (null == link) {
            throw new IllegalArgumentException("null link");
        }
        try {
            URL url = new URL(link);
            return isProxyableHost(url.getHost());
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
