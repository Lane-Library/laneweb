/**
 * 
 */
package edu.stanford.irt.laneweb.httpclient;

import org.apache.commons.httpclient.HttpClient;

/**
 * @author ceyates
 */
public interface HttpClientManager {

    public static final String ROLE = HttpClientManager.class.getName();

    HttpClient getHttpClient();

}
