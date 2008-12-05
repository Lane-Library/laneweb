package edu.stanford.irt.laneweb.httpclient;

import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

public class ConnectionManager extends MultiThreadedHttpConnectionManager {


    /**
     * sets the maximum total connections.
     * 
     * @param maxTotalConnections
     *            the max number of connections
     */
    public void setMaxTotalConnections(final int maxTotalConnections) {
        super.getParams().setMaxTotalConnections(maxTotalConnections);
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

    public void setConnectionTimeout(final int connectionTimeout) {
        super.getParams().setConnectionTimeout(connectionTimeout);
    }

}
