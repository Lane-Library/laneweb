package edu.stanford.irt.laneweb.config;

import java.util.concurrent.TimeUnit;

import org.apache.http.conn.HttpClientConnectionManager;

/**
 * Monitor for expired and idle {@code HttpClientConnectionManager} connections. When an HttpClient connection is
 * released back to the ConnectionManager and is subsequently closed on the server side, the client may try to re-use
 * the same connection. This presented a problem with the Google Cloud NAT service as Google reuses ip/port routes very
 * quickly (w/in 30 seconds instead of a more standard 4 mins).
 * <p>
 * Relevant cases: HelpSU INC00964780, Google Support Case #19681802
 * </p>
 * <p>
 * More info and example: https://hc.apache.org/httpcomponents-client-ga/tutorial/html/connmgmt.html#d5e418
 * </p>
 */
public class IdleConnectionMonitorThread extends Thread {

    private static final int FIVE_SECONDS = 5_000;

    private static final int THIRTY_SECONDS = 30_000;

    private final HttpClientConnectionManager connectionManager;

    private int idleConnectionTime = THIRTY_SECONDS;

    private int pollingTime = FIVE_SECONDS;

    private volatile boolean shutdown;

    public IdleConnectionMonitorThread(final HttpClientConnectionManager connectionManager) {
        super();
        this.connectionManager = connectionManager;
    }

    /**
     * @return the idleConnectionTime
     */
    public int getIdleConnectionTime() {
        return this.idleConnectionTime;
    }

    /**
     * @return the pollingTime
     */
    public int getPollingTime() {
        return this.pollingTime;
    }

    /**
     * @return the shutdown
     */
    public boolean isShutdown() {
        return this.shutdown;
    }

    @Override
    public void run() {
        try {
            while (!this.shutdown) {
                synchronized (this) {
                    wait(this.pollingTime);
                    this.connectionManager.closeExpiredConnections();
                    this.connectionManager.closeIdleConnections(this.idleConnectionTime, TimeUnit.MILLISECONDS);
                }
            }
        } catch (InterruptedException ex) {
            shutdown();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Set the idle connection time (in milliseconds) to be used when calling {@code HttpClientConnectionManager}'s
     * closeIdleConnections method
     *
     * @param idleConnectionTimeMilliseconds
     */
    public void setIdleConnectionTime(final int idleConnectionTimeMilliseconds) {
        this.idleConnectionTime = idleConnectionTimeMilliseconds;
    }

    /**
     * Set the the frequency (in milliseconds) to check for idle and expire connections
     *
     * @param pollingTimeMilliseconds
     */
    public void setPollingTime(final int pollingTimeMilliseconds) {
        this.pollingTime = pollingTimeMilliseconds;
    }

    /**
     * Stop checking for idle and expired connections
     */
    public void shutdown() {
        this.shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }
}
