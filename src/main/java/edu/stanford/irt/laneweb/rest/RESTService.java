package edu.stanford.irt.laneweb.rest;

import java.net.URI;

import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestOperations;

public class RESTService {

    private RestOperations restOperations;

    public RESTService(final RestOperations restOperations) {
        this.restOperations = restOperations;
    }

    public <T> T getObject(final URI uri, final Class<T> type) {
        return this.restOperations.getForObject(uri, type);
    }

    public <T> T getObject(final URI uri, final TypeReference<T> type) {
        return this.restOperations.exchange(uri, HttpMethod.GET, null, type).getBody();
    }
}
