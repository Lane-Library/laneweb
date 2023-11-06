package edu.stanford.irt.laneweb.folio;

import java.net.URI;
import java.util.Map;

import edu.stanford.irt.laneweb.rest.RESTService;

public class RESTUserService implements UserService {

    private static final String ENDPOINT_PATH_FORMAT = "users/user";

    private URI catalogServiceURI;

    private RESTService restService;

    public RESTUserService(final URI catalogServiceURI, final RESTService restService) {
        this.catalogServiceURI = catalogServiceURI.resolve(ENDPOINT_PATH_FORMAT);
        this.restService = restService;
    }

    @Override
    public boolean addUser(final Map<String, Object> user) {
        return this.restService.postObject(this.catalogServiceURI, user, Boolean.class).booleanValue();
    }
}
