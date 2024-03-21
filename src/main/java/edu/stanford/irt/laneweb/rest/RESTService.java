package edu.stanford.irt.laneweb.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

public class RESTService {

    private RestClient restClient;

    public RESTService(final RestClient restClient) {
        this.restClient = restClient;
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
            return this.restClient.get().uri(uri).retrieve().body(type);
        } catch (RestClientException e) {
            throw new RESTException(e);
        }
    }

    public <T> T getObject(final URI uri, final TypeReference<T> type) throws RESTException {
        try {
            return this.restClient.get().uri(uri).retrieve().body(type);
        } catch (RestClientException e) {
            throw new RESTException(e);
        }
    }

    public int postURLEncodedString(final URI uri, final String object) throws RESTException {
        try {
            return this.restClient.post().uri(uri).contentType(MediaType.APPLICATION_FORM_URLENCODED).body(object)
                    .retrieve().toEntity(String.class).getStatusCode().value();
        } catch (RestClientException e) {
            throw new RESTException(e);
        }
    }

    public <T> T postObject(final URI uri, final Object object, Class<T> responseType) throws RESTException {
        try {
            return this.restClient.post().uri(uri).contentType(MediaType.APPLICATION_JSON).body(object).retrieve()
                    .body(responseType);
        } catch (RestClientException e) {
            throw new RESTException(e);
        }
    }

    public void putObject(final URI uri, final Object object) throws RESTException {
        try {
            this.restClient.put().uri(uri).body(object);
        } catch (RestClientException e) {
            throw new RESTException(e);
        }
    }
}
