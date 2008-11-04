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

    /** the proxy host, if any */
    private String proxyHost;

    /** the proxy port if any */
    private int proxyPort;

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
     * set the proxy host
     * 
     * @parm proxyHost
     */
    public void setProxyHost(final String proxyHost) {
        this.proxyHost = proxyHost;
    }

    /**
     * set the proxy port
     * 
     * @param proxyPort
     */
    public void setProxyPort(final int proxyPort) {
        this.proxyPort = proxyPort;
    }

    /**
     * this initializes the HttpClient with the various parameters.
     */
    public void init() {
        this.httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        this.httpClient.getHttpConnectionManager().getParams().setMaxTotalConnections(this.maxTotalConnections);
        this.httpClient.getHttpConnectionManager().getParams().setDefaultMaxConnectionsPerHost(this.defaultMaxConnectionsPerHost);
        this.httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(this.connectionTimeout);
        // set up the proxy if parameters are available
        if ((null != this.proxyHost) && (this.proxyHost.length() > 0) && (this.proxyPort > 0)) {
            this.httpClient.getHostConfiguration().setProxy(this.proxyHost, this.proxyPort);
        }
    }

}
