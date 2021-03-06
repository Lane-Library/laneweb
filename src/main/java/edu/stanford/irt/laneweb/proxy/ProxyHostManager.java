package edu.stanford.irt.laneweb.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.LanewebException;

public class ProxyHostManager {

    private static final String COLON_DOUBLE_SLASH = "://";

    private static final int COLON_DOUBLE_SLASH_LENGTH = COLON_DOUBLE_SLASH.length();

    private static final long DEFAULT_DELAY = 120L;

    private static final Logger log = LoggerFactory.getLogger(ProxyHostManager.class);

    private ScheduledExecutorService executor;

    private Set<String> proxyHosts;

    public ProxyHostManager(final ProxyServersService service, final ScheduledExecutorService executor) {
        this.executor = executor;
        this.proxyHosts = new HashSet<>();
        String proxyHost = null;
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
                this.proxyHosts = service.getHosts();
                log.info("successfully retrieved proxy hosts from {}", service.getClass().getName());
            } catch (LanewebException e) {
                log.error("failed to retrieve proxy host hosts from {}: {}", service.getClass().getName(),
                        e.getMessage());
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
        boolean proxyable = false;
        if (link != null) {
            int doubleSlashIndex = link.indexOf(COLON_DOUBLE_SLASH);
            if (doubleSlashIndex > -1) {
                String hostAndPath = link.substring(doubleSlashIndex + COLON_DOUBLE_SLASH_LENGTH);
                int slashIndex = hostAndPath.indexOf('/');
                if (slashIndex == -1) {
                    slashIndex = hostAndPath.length();
                }
                String host = hostAndPath.substring(0, slashIndex);
                proxyable = isProxyableHost(host);
            }
        }
        return proxyable;
    }
}
