package edu.stanford.irt.laneweb.httpclient;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

public class ConnectionManager extends MultiThreadedHttpConnectionManager {

    public void setConnectionTimeout(final int connectionTimeout) {
        super.getParams().setConnectionTimeout(connectionTimeout);
    }

    /**
     * sets the maximum connections per host.
     * 
     * @param defaultMaxConnectionsPerHost
     *            duh
     */
    public void setDefaultMaxConnectionsPerHost(final int defaultMaxConnectionsPerHost) {
        super.getParams().setDefaultMaxConnectionsPerHost(defaultMaxConnectionsPerHost);
    }

    /**
     * sets the maximum total connections.
     * 
     * @param maxTotalConnections
     *            the max number of connections
     */
    @Override
    public void setMaxTotalConnections(final int maxTotalConnections) {
        super.getParams().setMaxTotalConnections(maxTotalConnections);
    }
}
