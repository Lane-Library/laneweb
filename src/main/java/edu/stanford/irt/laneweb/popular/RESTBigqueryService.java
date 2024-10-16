package edu.stanford.irt.laneweb.popular;

import java.net.URI;
import java.util.List;
import java.util.Map;

import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.rest.TypeReference;

public class RESTBigqueryService implements BigqueryService {

    private static final String POPULAR_ENDPOINT_PATH = "bigquery/popular/%s";

    private static final TypeReference<List<Map<String, String>>> TYPE = new TypeReference<>() {
    };

    private URI bigqueryServiceURI;

    private RESTService restService;

    public RESTBigqueryService(final URI bigqueryServiceURI, final RESTService restService) {
        this.bigqueryServiceURI = bigqueryServiceURI;
        this.restService = restService;
    }

    @Override
    public List<Map<String, String>> getPopularResources(final String resourceType) {
        String pathWithTypeParam = String.format(POPULAR_ENDPOINT_PATH, resourceType);
        URI uri = this.bigqueryServiceURI.resolve(pathWithTypeParam);
        return this.restService.getObject(uri, TYPE);
    }
}
