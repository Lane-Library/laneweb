package edu.stanford.irt.laneweb.metasearch;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.rest.BasicAuthRESTService;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.status.ApplicationStatus;

public class RESTMetaSearchService implements MetaSearchService {

    private static final int BUFFER_SIZE = 4096;

    private static final String UTF8 = StandardCharsets.UTF_8.name();

    private URI metaSearchURI;

    private BasicAuthRESTService restService;

    public RESTMetaSearchService(final URI metaSearchURL, final BasicAuthRESTService restService) {
        this.metaSearchURI = metaSearchURL;
        this.restService = restService;
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

    private static byte[] getByteArrayFromStream(final InputStream input) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        while (true) {
            int i = input.read(buffer);
            if (i == -1) {
                break;
            }
            baos.write(buffer, 0, i);
        }
        return baos.toByteArray();
    }

    @Override
    public void clearAllCaches() {
        getResponse(this.metaSearchURI.resolve("clearCache"), String.class);
    }

    @Override
    public void clearCache(final String query) {
        String path = null;
        try {
            path = new StringBuilder("clearCache?query=").append(URLEncoder.encode(query, UTF8)).toString();
        } catch (UnsupportedEncodingException e) {
            // ignore, won't happen
        }
        getResponse(this.metaSearchURI.resolve(path), String.class);
    }

    @Override
    public Result describe(final String query, final Collection<String> engines) {
        StringBuilder requestURI = new StringBuilder("describe");
        addQueryString(requestURI, query, engines);
        return getResponse(this.metaSearchURI.resolve(requestURI.toString()), Result.class);
    }

    @Override
    public ApplicationStatus getStatus() {
        return getResponse(this.metaSearchURI.resolve("status.json"), ApplicationStatus.class);
    }

    @Override
    public Result search(final String query, final Collection<String> engines, final long wait) {
        StringBuilder requestURI = new StringBuilder("search");
        addQueryString(requestURI, query, engines);
        requestURI.append("&timeout=").append(wait);
        return getResponse(this.metaSearchURI.resolve(requestURI.toString()), Result.class);
    }

    @Override
    public byte[] testURL(final String url) {
        URI uri = this.metaSearchURI.resolve("test-url?url=" + url);
        try {
            try (InputStream input = this.restService.getInputStream(uri)) {
                return getByteArrayFromStream(input);
            }
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }

    private <T> T getResponse(final URI uri, final Class<T> clazz) {
        return this.restService.getObject(uri, clazz);
    }
}
