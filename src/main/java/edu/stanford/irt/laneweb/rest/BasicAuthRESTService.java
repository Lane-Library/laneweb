package edu.stanford.irt.laneweb.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Base64;

import org.springframework.core.io.Resource;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

public class BasicAuthRESTService {

    private RestClient restClient;

    private String base64Creds;

    private static final String AUTHORIZATION = "Authorization";

    private static final String BASIC = "Basic ";

    public BasicAuthRESTService(final RestClient restClient, String userInfo) {
        this.restClient = restClient;
        this.base64Creds = Base64.getEncoder().encodeToString(userInfo.getBytes());
    }

    public InputStream getInputStream(final URI uri) throws RESTException {
        try {
            return getObject(uri, Resource.class).getInputStream();
        } catch (IOException e) {
            throw new RESTException(e);
        }
    }

    public <T> T getObject(final URI uri, final Class<T> type) throws RESTException {
        try {
            return this.restClient.get().uri(uri).header(AUTHORIZATION, BASIC + base64Creds).retrieve().body(type);
        } catch (RestClientException e) {
            throw new RESTException(e);
        }
    }

    public <T> T getObject(final URI uri, final TypeReference<T> type) throws RESTException {
        try {
            return this.restClient.get().uri(uri).header(AUTHORIZATION, BASIC + base64Creds).retrieve().body(type);
        } catch (RestClientException e) {
            throw new RESTException(e);
        }
    }
}
