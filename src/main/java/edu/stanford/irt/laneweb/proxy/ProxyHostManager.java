package edu.stanford.irt.laneweb.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.LanewebException;

public class ProxyHostManager {

    private static final long DEFAULT_DELAY = 120L;

    private static final Logger log = LoggerFactory.getLogger(ProxyHostManager.class);

    private ScheduledExecutorService executor;

    private Set<String> proxyHosts;

    public ProxyHostManager(final ProxyHostSource hostSource, final ScheduledExecutorService executor) {
        this.executor = executor;
        this.proxyHosts = new HashSet<>();
        String proxyHost = null;
        new Thread();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("ezproxy-servers.txt"), StandardCharsets.UTF_8))) {
            while ((proxyHost = reader.readLine()) != null) {
                this.proxyHosts.add(proxyHost);
            }
        } catch (IOException e) {
            throw new LanewebException(e);
        }
        executor.scheduleAtFixedRate(() -> {
            try {
                this.proxyHosts = hostSource.getHosts();
                log.info("successfully retrieved proxy hosts from the voyager catalog");
            } catch (SQLException e) {
                log.error("failed to retrieve proxy host hosts from the voyager catalog: {}", e.getMessage());
            }
        }, 0L, DEFAULT_DELAY, TimeUnit.MINUTES);
    }

    public void destroy() {
        this.executor.shutdownNow();
    }

    public boolean isProxyableHost(final String host) {
        if (host == null) {
            return false;
        }
        return this.proxyHosts.contains(host);
    }

    public boolean isProxyableLink(final String link) {
        if (link == null) {
            return false;
        }
        try {
            URI url = new URI(link);
            return isProxyableHost(url.getHost());
        } catch (URISyntaxException e) {
            log.error("unable to determine host from link: {}, error: {}", link, e.getMessage());
            return false;
        }
    }
}
