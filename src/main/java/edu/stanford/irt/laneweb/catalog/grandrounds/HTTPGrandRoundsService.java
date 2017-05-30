package edu.stanford.irt.laneweb.catalog.grandrounds;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.IOUtils;

public class HTTPGrandRoundsService extends AbstractGrandRoundsService {

    private static final String ENDPOINT_PATH_FORMAT = "grandrounds?department=%s&year=%s";

    private static final String UTF8 = StandardCharsets.UTF_8.name();

    private URI catalogServiceURI;

    public HTTPGrandRoundsService(final URI catalogServiceURI) {
        this.catalogServiceURI = catalogServiceURI;
    }

    @Override
    protected InputStream getInputStream(final String department, final String year) {
        try {
            String endpointPath = String.format(ENDPOINT_PATH_FORMAT, URLEncoder.encode(department, UTF8),
                    URLEncoder.encode(year, UTF8));
            return IOUtils.getStream(new URL(this.catalogServiceURI.toURL(), endpointPath));
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }
}
