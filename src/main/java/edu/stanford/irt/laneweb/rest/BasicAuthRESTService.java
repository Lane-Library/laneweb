package edu.stanford.irt.laneweb.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Base64;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

public class BasicAuthRESTService {

    private static final String AUTHORIZATION = "Authorization";

    private static final String BASIC = "Basic ";

    private String base64Creds;

    private RestClient restClient;

    public BasicAuthRESTService(final RestClient restClient, final String userInfo) {
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
            return this.restClient.get().uri(uri).header(AUTHORIZATION, BASIC + this.base64Creds).retrieve().body(type);
        } catch (RestClientException e) {
            throw new RESTException(e);
        }
    }

    public <T> T getObject(final URI uri, final TypeReference<T> type) throws RESTException {
        try {
            return this.restClient.get().uri(uri).header(AUTHORIZATION, BASIC + this.base64Creds).retrieve().body(type);
        } catch (RestClientException e) {
            throw new RESTException(e);
        }
    }

    public <T> T postObject(final URI uri, final Object object, final Class<T> responseType) throws RESTException {
        try {
            return this.restClient.post().uri(uri).header(AUTHORIZATION, BASIC + this.base64Creds)
                    .contentType(MediaType.APPLICATION_JSON).body(object).retrieve().body(responseType);
        } catch (RestClientException e) {
            throw new RESTException(e);
        }
    }
}
