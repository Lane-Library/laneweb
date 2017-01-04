package edu.stanford.irt.laneweb.metasearch;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.Result;

public class MetaSearchService {

    private URL metaSearchURL;

    private ObjectMapper objectMapper;

    private int readTimeout;

    public MetaSearchService(final URL metaSearchURL, final ObjectMapper objectMapper, final int readTimeout) {
        this.metaSearchURL = metaSearchURL;
        this.objectMapper = objectMapper;
        this.readTimeout = readTimeout;
    }

    private static void addQueryString(final StringBuilder requestURI, final Query query,
            final Collection<String> engines) {
        requestURI.append("?query=").append(query.getURLEncodedText());
        if (engines != null && !engines.isEmpty()) {
            requestURI.append("&engines=");
            engines.stream().forEach(e -> requestURI.append(e).append(','));
            requestURI.setLength(requestURI.length() - 1);
        }
    }

    public String clearAllCaches() {
        return getResponse("clearCache", String.class);
    }

    public String clearCache(final Query query) {
        String requestURI = new StringBuilder("clearCache?query=").append(query.getURLEncodedText()).toString();
        return getResponse(requestURI, String.class);
    }

    public Result describe(final Query query, final Collection<String> engines) {
        StringBuilder requestURI = new StringBuilder("describe");
        addQueryString(requestURI, query, engines);
        return getResponse(requestURI.toString(), Result.class);
    }

    public Result search(final Query query, final Collection<String> engines, final long wait) {
        StringBuilder requestURI = new StringBuilder("search");
        addQueryString(requestURI, query, engines);
        requestURI.append("&timeout=").append(wait);
        return getResponse(requestURI.toString(), Result.class);
    }

    private <T> T getResponse(final String requestURI, final Class<T> clazz) {
        try {
            URLConnection connection = new URL(this.metaSearchURL, requestURI).openConnection();
            connection.setReadTimeout(this.readTimeout);
            try (InputStream input = connection.getInputStream()) {
                return this.objectMapper.readValue(input, clazz);
            }
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }
}