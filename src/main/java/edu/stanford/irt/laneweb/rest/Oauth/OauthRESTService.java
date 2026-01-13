package edu.stanford.irt.laneweb.rest.Oauth;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import edu.stanford.irt.laneweb.rest.RESTException;
import edu.stanford.irt.laneweb.rest.TypeReference;

public class OauthRESTService {

    private OauthTokenService oauthTokenService;

    private RestClient restClient;

    public OauthRESTService(final RestClient restClient, final OauthTokenService oauthTokenService) {
        this.restClient = restClient;
        this.oauthTokenService = oauthTokenService;
    }

    @Retryable(retryFor = { RESTException.class }, maxAttempts = 2)
    public InputStream getInputStream(final URI uri) throws RESTException {
        String token = this.oauthTokenService.getAccessToken();
        try {
            return this.restClient.get().uri(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + token).retrieve()
                    .body(Resource.class).getInputStream();
        } catch (IOException e) {
            this.oauthTokenService.resetToken();
            throw new RESTException(e);
        }
    }

    @Retryable(retryFor = { RESTException.class }, maxAttempts = 2)
    public <T> T getObject(final URI uri, final Class<T> type) throws RESTException {
        String token = this.oauthTokenService.getAccessToken();
        try {
            return (T) this.restClient.get().uri(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + token).retrieve()
                    .body(type);
        } catch (RestClientException e) {
            this.oauthTokenService.resetToken();
            throw new RESTException(e);
        }
    }

    @Retryable(retryFor = { RESTException.class }, maxAttempts = 2)
    public <T> T getObject(final URI uri, final TypeReference<T> type) throws RESTException {
        String token = this.oauthTokenService.getAccessToken();
        try {
            return this.restClient.get().uri(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + token).retrieve()
                    .body(type);
        } catch (RestClientException e) {
            this.oauthTokenService.resetToken();
            throw new RESTException(e);
        }
    }

    @Retryable(retryFor = { RESTException.class }, maxAttempts = 2)
    public <T> T postObject(final URI uri, final Object object, final Class<T> responseType) throws RESTException {
        String token = this.oauthTokenService.getAccessToken();
        try {
            return this.restClient.post().uri(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON).body(object).retrieve().body(responseType);
        } catch (RestClientException e) {
            this.oauthTokenService.resetToken();
            throw new RESTException(e);
        }
    }

    // For testing purposes
    public void resetToken() {
        this.oauthTokenService.resetToken();
    }

}