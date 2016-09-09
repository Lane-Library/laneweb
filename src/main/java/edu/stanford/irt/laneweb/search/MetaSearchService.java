package edu.stanford.irt.laneweb.search;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.Result;

public class MetaSearchService {

    private URL metaSearchURL;

    private ObjectMapper objectMapper;

    public MetaSearchService(final URL metaSearchURL, final ObjectMapper objectMapper) {
        this.metaSearchURL = metaSearchURL;
        this.objectMapper = objectMapper;
    }

    private static void addQueryString(final StringBuilder requestURI, final Query query, final Collection<String> engines) {
        requestURI.append("?query=").append(query.getURLEncodedText());
        if (engines != null && !engines.isEmpty()) {
            requestURI.append("&engines=");
            engines.stream().forEach(e -> requestURI.append(e).append(','));
            requestURI.setLength(requestURI.length() - 1);
        }
    }

    public void clearAllCaches() {
        throw new UnsupportedOperationException();
    }

    public void clearCache(final String q) {
        throw new UnsupportedOperationException();
    }

    public Result describe(final Query query, final Collection<String> engines) {
        StringBuilder requestURI = new StringBuilder("/describe");
        addQueryString(requestURI, query, engines);
        return getResult(requestURI.toString());
    }

    public Result search(final Query query, final Collection<String> engines, final long wait) {
        StringBuilder requestURI = new StringBuilder("/search");
        addQueryString(requestURI, query, engines);
        requestURI.append("&timeout=").append(wait);
        return getResult(requestURI.toString());
    }

    private Result getResult(final String requestURI) {
        try (InputStream input = new URL(this.metaSearchURL, requestURI).openStream()) {
            return this.objectMapper.readValue(input, Result.class);
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }
}