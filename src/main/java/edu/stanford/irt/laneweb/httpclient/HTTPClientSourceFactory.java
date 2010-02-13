package edu.stanford.irt.laneweb.httpclient;

import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceFactory;

/**
 * {@link HTTPClientSource} Factory class.
 */
public class HTTPClientSourceFactory implements SourceFactory {

    private HttpClient httpClient;

    /**
     * Creates a {@link HTTPClientSource} instance.
     */
    @SuppressWarnings("unchecked")
    public Source getSource(final String uri, final Map sourceParams) {
            try {
                return new HTTPClientSource(uri, sourceParams, this.httpClient);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

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
