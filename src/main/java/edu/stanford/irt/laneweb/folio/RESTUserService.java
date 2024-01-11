package edu.stanford.irt.laneweb.folio;

import java.net.URI;
import java.util.List;
import java.util.Map;

import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.rest.TypeReference;

public class RESTUserService implements UserService {

    private static final String ENDPOINT_PATH_FORMAT = "users/user";

    private static final String ENDPOINT_QUERY_FORMAT = ENDPOINT_PATH_FORMAT
            + "?username=%s&externalSystemId=%s&email=%s";

    private static final TypeReference<List<Map<String, Object>>> TYPE = new TypeReference<>() {
    };

    private URI catalogServiceURI;

    private RESTService restService;

    public RESTUserService(final URI catalogServiceURI, final RESTService restService) {
        this.catalogServiceURI = catalogServiceURI;
        this.restService = restService;
    }

    @Override
    public boolean addUser(final Map<String, Object> user) {
        URI uri = this.catalogServiceURI.resolve(ENDPOINT_PATH_FORMAT);
        return this.restService.postObject(uri, user, Boolean.class).booleanValue();
    }

    @Override
    public List<Map<String, Object>> getUser(final String username, final String externalSystemId, final String email) {
        String pathAndQuery = String.format(ENDPOINT_QUERY_FORMAT, username, externalSystemId, email);
        URI uri = this.catalogServiceURI.resolve(pathAndQuery);
        return this.restService.getObject(uri, TYPE);
    }
}
