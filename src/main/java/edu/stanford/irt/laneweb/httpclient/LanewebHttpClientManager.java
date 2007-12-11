package edu.stanford.irt.laneweb.httpclient;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

public class LanewebHttpClientManager implements HttpClientManager, Parameterizable, Initializable, ThreadSafe {

    /** the HttpClient. */
    protected HttpClient httpClient;

    /** the total number of connections. */
    protected int maxTotalConnections;

    /** the total connection per host. */
    protected int defaultMaxConnectionsPerHost;

    /** the time in milliseconds after which idle connection are closed */
    protected long idleTimeout;

    protected int connectionTimeout;

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

    /**
     * sets the idle timeout.
     * 
     * @param idleTimeout
     *            the idleTimeout
     */
    public void setIdleTimeout(final long idleTimeout) {
        this.idleTimeout = idleTimeout;
    }

    public void setConnectionTimeout(final int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * this initializes the HttpClient with the various parameters.
     */
    public void init() {
        this.httpClient.getHttpConnectionManager().getParams().setMaxTotalConnections(this.maxTotalConnections);
        this.httpClient.getHttpConnectionManager().getParams().setDefaultMaxConnectionsPerHost(this.defaultMaxConnectionsPerHost);
        this.httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(this.connectionTimeout);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.avalon.framework.parameters.Parameterizable#parameterize(org.apache.avalon.framework.parameters.Parameters)
     */
    public void parameterize(final Parameters params) throws ParameterException {
        this.maxTotalConnections = params.getParameterAsInteger("max-total-connections", 30);
        this.defaultMaxConnectionsPerHost = params.getParameterAsInteger("max-host-connections", 5);
        this.connectionTimeout = params.getParameterAsInteger("connection-timeout", 20000);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.avalon.framework.activity.Initializable#initialize()
     */
    public void initialize() throws Exception {
        this.httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
        init();
    }

}
