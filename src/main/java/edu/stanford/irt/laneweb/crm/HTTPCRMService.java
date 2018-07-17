package edu.stanford.irt.laneweb.crm;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;

public class HTTPCRMService implements CRMService {

    /**
     * A factory for URLConnections that can be mocked in tests.
     */
    static class URLConnectionFactory {

        public URLConnection getConnection(final URI uri) throws IOException {
            return uri.toURL().openConnection();
        }
    }

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    private URI acquisitionURI;

    private URLConnectionFactory connectionFactory;

    public HTTPCRMService(final URI acquisitionURI) {
        this(acquisitionURI, new URLConnectionFactory());
    }

    HTTPCRMService(final URI acquisitionURI, final URLConnectionFactory connectionFactory) {
        this.acquisitionURI = acquisitionURI;
        this.connectionFactory = connectionFactory;
    }

    @Override
    public int submitRequest(final Map<String, Object> feedback) throws IOException {
        StringBuilder queryString = new StringBuilder();
        for (Entry<String, Object> entry : feedback.entrySet()) {
            queryString.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue().toString(), UTF_8))
                    .append('&');
        }
        queryString.append("id=");
        HttpsURLConnection con = (HttpsURLConnection) this.connectionFactory.getConnection(this.acquisitionURI);
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(queryString.toString());
        wr.close();
        return con.getResponseCode();
    }
}
