package edu.stanford.irt.laneweb.httpclient;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

public class HttpClientManager {

    /** the HttpClient. */
    private HttpClient httpClient;

    /** the total number of connections. */
    private int maxTotalConnections;

    /** the total connection per host. */
    private int defaultMaxConnectionsPerHost;

    private int connectionTimeout;

    /**
     * accessor for the HttpClient.
     * 
     * @return the HttpClient
     */
    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    /**
     * sets the HttpClient.
     * 
     * @param httpClient
     *            the HttpClient
     */
    public void setHttpClient(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * sets the maximum total connections.
     * 
     * @param maxTotalConnections
     *            the max number of connections
     */
    public void setMaxTotalConnections(final int maxTotalConnections) {
        this.maxTotalConnections = maxTotalConnections;
    }

    /**
     * sets the maximum connections per host.
     * 
     * @param defaultMaxConnectionsPerHost
     *            duh
     */
    public void setDefaultMaxConnectionsPerHost(final int defaultMaxConnectionsPerHost) {
        this.defaultMaxConnectionsPerHost = defaultMaxConnectionsPerHost;
    }

    public void setConnectionTimeout(final int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * this initializes the HttpClient with the various parameters.
     */
    public void init() {
        this.httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        this.httpClient.getHttpConnectionManager().getParams().setMaxTotalConnections(this.maxTotalConnections);
        this.httpClient.getHttpConnectionManager().getParams().setDefaultMaxConnectionsPerHost(this.defaultMaxConnectionsPerHost);
        this.httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(this.connectionTimeout);
    }

}
