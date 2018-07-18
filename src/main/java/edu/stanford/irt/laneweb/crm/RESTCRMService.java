package edu.stanford.irt.laneweb.crm;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Map.Entry;

import edu.stanford.irt.laneweb.rest.RESTService;

public class RESTCRMService implements CRMService {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    private RESTService restService;

    private URI uri;

    public RESTCRMService(final URI uri, final RESTService restService) {
        this.uri = uri;
        this.restService = restService;
    }

    @Override
    public int submitRequest(final Map<String, Object> feedback) throws UnsupportedEncodingException {
        StringBuilder queryString = new StringBuilder();
        for (Entry<String, Object> entry : feedback.entrySet()) {
            queryString.append(entry.getKey()).append('=').append(URLEncoder.encode(entry.getValue().toString(), UTF_8))
                    .append('&');
        }
        queryString.append("id=");
        return this.restService.postString(this.uri, queryString.toString());
    }
}
