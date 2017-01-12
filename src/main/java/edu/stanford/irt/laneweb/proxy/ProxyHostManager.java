package edu.stanford.irt.laneweb.proxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.LanewebException;

public class ProxyHostManager {

    private static final long DEFAULT_DELAY = 2L;

    private static final long FAILURE_DELAY = 10L;

    private static final Logger log = LoggerFactory.getLogger(ProxyHostManager.class);

    private DataSource dataSource;

    private ScheduledExecutorService executor;

    private Future<Set<String>> futureProxyHosts;

    private Object lock = new Object();

    private Set<String> proxyHosts;

    public ProxyHostManager(final DataSource dataSource, final ScheduledExecutorService executor) {
        this.dataSource = dataSource;
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
        this.futureProxyHosts = executor.submit(() -> new DatabaseProxyHostSet(dataSource));
    }

    public void destroy() {
        this.executor.shutdownNow();
    }

    public boolean isProxyableHost(final String host) {
        if (host == null) {
            return false;
        }
        checkForUpdate();
        return this.proxyHosts.contains(host);
    }

    public boolean isProxyableLink(final String link) {
        if (link == null) {
            return false;
        }
        try {
            URL url = new URL(link);
            return isProxyableHost(url.getHost());
        } catch (MalformedURLException e) {
            return false;
        }
    }

    private void checkForUpdate() {
        synchronized (this.lock) {
            if (this.futureProxyHosts.isDone()) {
                try {
                    this.proxyHosts = this.futureProxyHosts.get();
                    scheduleUpdate(DEFAULT_DELAY);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new LanewebException(e);
                } catch (ExecutionException e) {
                    log.error("failed to retrieve proxy hosts: {}", e.getMessage());
                    scheduleUpdate(FAILURE_DELAY);
                }
            }
        }
    }

    private void scheduleUpdate(final long delay) {
        this.futureProxyHosts = this.executor.schedule(() -> new DatabaseProxyHostSet(this.dataSource), delay,
                TimeUnit.MINUTES);
    }
}
