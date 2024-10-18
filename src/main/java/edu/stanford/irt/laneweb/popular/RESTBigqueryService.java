package edu.stanford.irt.laneweb.popular;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.rest.TypeReference;

public class RESTBigqueryService implements BigqueryService {

    private static final String POPULAR_ALL_ENDPOINT_PATH = "popular/all";

    private static final String POPULAR_BY_TYPE_ENDPOINT_PATH = "popular/type/%s";

    private static final TypeReference<List<Map<String, String>>> TYPE = new TypeReference<>() {
    };

    private URI bigqueryServiceURI;

    private RESTService restService;

    public RESTBigqueryService(final URI bigqueryServiceURI, final RESTService restService) {
        this.bigqueryServiceURI = bigqueryServiceURI;
        this.restService = restService;
    }

    @Override
    public List<Map<String, String>> getAllPopularResources() {
        return this.restService.getObject(this.bigqueryServiceURI.resolve(POPULAR_ALL_ENDPOINT_PATH), TYPE);
    }

    @Override
    public List<Map<String, String>> getPopularResources(final String resourceType) {
        String type = "";
        try {
            type = URLEncoder.encode(resourceType, StandardCharsets.UTF_8.displayName()).replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            // won't happen
        }
        String pathWithTypeParam = String.format(POPULAR_BY_TYPE_ENDPOINT_PATH, type);
        URI uri = this.bigqueryServiceURI.resolve(pathWithTypeParam);
        return this.restService.getObject(uri, TYPE);
    }
}
