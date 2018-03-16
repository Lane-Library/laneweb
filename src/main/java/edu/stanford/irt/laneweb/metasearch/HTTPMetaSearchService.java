package edu.stanford.irt.laneweb.metasearch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.search.impl.Result;

public class HTTPMetaSearchService implements MetaSearchService {

    private static final String UTF8 = StandardCharsets.UTF_8.name();

    private URL metaSearchURL;

    private ObjectMapper objectMapper;

    private int readTimeout;

    public HTTPMetaSearchService(final URL metaSearchURL, final ObjectMapper objectMapper, final int readTimeout) {
        this.metaSearchURL = metaSearchURL;
        this.objectMapper = objectMapper;
        this.readTimeout = readTimeout;
    }

    private static void addQueryString(final StringBuilder requestURI, final String query,
            final Collection<String> engines) {
        try {
            requestURI.append("?query=").append(URLEncoder.encode(query, UTF8));
        } catch (UnsupportedEncodingException e) {
            // ignore, won't happen
        }
        if (engines != null && !engines.isEmpty()) {
            requestURI.append("&engines=");
            engines.stream().forEach((final String e) -> requestURI.append(e).append(','));
            requestURI.setLength(requestURI.length() - 1);
        }
    }

    public void clearAllCaches() {
        getResponse("clearCache", String.class);
    }

    public void clearCache(final String query) {
        String requestURI = null;
        try {
            requestURI = new StringBuilder("clearCache?query=").append(URLEncoder.encode(query, UTF8)).toString();
        } catch (UnsupportedEncodingException e) {
            // ignore, won't happen
        }
        getResponse(requestURI, String.class);
    }

    public Result describe(final String query, final Collection<String> engines) {
        StringBuilder requestURI = new StringBuilder("describe");
        addQueryString(requestURI, query, engines);
        return getResponse(requestURI.toString(), Result.class);
    }

    public Result search(final String query, final Collection<String> engines, final long wait) {
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

    @Override
    public byte[] testURL(String url) {
        try {
            URLConnection connection = new URL(this.metaSearchURL, "url-tester?url=" + url).openConnection();
            connection.setReadTimeout(this.readTimeout);
            try (InputStream input = connection.getInputStream()) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[4096];
                while (true) {
                    int i = input.read(buffer);
                    if (i == -1) {
                        break;
                    }
                    baos.write(buffer, 0, i);
                }
                return baos.toByteArray();
            }
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }
}
