package edu.stanford.irt.laneweb.httpclient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceException;
import org.apache.excalibur.source.SourceFactory;

/**
 * {@link HTTPClientSource} Factory class.
 * 
 */
public class HTTPClientSourceFactory implements SourceFactory {

    private HttpClient httpClient;

    /**
     * Creates a {@link HTTPClientSource} instance.
     */
    public Source getSource(final String uri, final Map sourceParams) throws MalformedURLException, IOException {
        try {
            final HTTPClientSource source = new HTTPClientSource(uri, sourceParams, this.httpClient);
            return source;
        } catch (final MalformedURLException e) {
            throw e;
        } catch (final IOException e) {
            throw e;
        } catch (final Exception e) {
            final StringBuffer message = new StringBuffer();
            message.append("Exception thrown while creating ");
            message.append(HTTPClientSource.class.getName());

            throw new SourceException(message.toString(), e);
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

    public void setHttpClientManager(final HttpClientManager manager) {
        if (null == manager) {
            throw new IllegalArgumentException("null httpClientManager");
        }
        this.httpClient = manager.getHttpClient();
    }
}
