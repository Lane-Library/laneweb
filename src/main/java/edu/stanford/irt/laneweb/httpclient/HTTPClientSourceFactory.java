package edu.stanford.irt.laneweb.httpclient;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceFactory;

/**
 * {@link HTTPClientSource} Factory class.
 */
public class HTTPClientSourceFactory implements SourceFactory {

    private ExecutorService executor = Executors.newFixedThreadPool(5);

    private HttpClient httpClient;
    
    public void destroy() {
        this.executor.shutdownNow();
        MultiThreadedHttpConnectionManager.shutdownAll();
    }

    /**
     * Creates a {@link HTTPClientSource} instance.
     */
    @SuppressWarnings("rawtypes")
    public Source getSource(final String uri, final Map sourceParams) {
        return new HTTPClientSource(uri, this.httpClient, this.executor);
    }

    /**
     * Releases the given {@link Source} object.
     * 
     * @param source
     *            {@link Source} object to be released
     */
    public void release(final Source source) {
    }

    public void setHttpClient(final HttpClient httpClient) {
        if (null == httpClient) {
            throw new IllegalArgumentException("null httpClient");
        }
        this.httpClient = httpClient;
    }
}
