package edu.stanford.irt.laneweb.rest;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.LanewebException;

public class OauthRESTService {

    private static final String GRANT_TYPE = "client_credentials";

    private String accessToken = null;

    private String userInfo;

    private RestClient restClient;

    private URI tokenEndpoint;

    public OauthRESTService(final RestClient restClient, final String userInfo, final URI tokenEndpoint) {
        this.restClient = restClient;
        this.userInfo = userInfo;
        this.tokenEndpoint = tokenEndpoint;
    }

    private void authenticate() {
        String[] userInfoArray = this.userInfo.split(":");
        LinkedMultiValueMap<String, String> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.add("client_id", userInfoArray[0]);
        bodyMap.add("client_secret", userInfoArray[1]);
        bodyMap.add("grant_type", GRANT_TYPE);
        if (userInfoArray.length > 2 && !userInfoArray[2].isEmpty()) {
            bodyMap.add("scope", userInfoArray[2]);
        }
        ResponseEntity<String> response = restClient.post().uri(this.tokenEndpoint)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED).body(bodyMap).retrieve().toEntity(String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonResponse;
        try {
            jsonResponse = mapper.readTree(response.getBody());
            this.accessToken = jsonResponse.get("access_token").asText();
        } catch (Exception e) {
            throw new LanewebException(e);
        }
    }

    public InputStream getInputStream(final URI uri) throws RESTException {
        if (this.accessToken == null) {
            authenticate();
        }
        try {
            return this.restClient.get().uri(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .retrieve().body(Resource.class).getInputStream();
        } catch (IOException e) {
            throw new RESTException(e);
        }
    }

    public <T> T getObject(final URI uri, final Class<T> type) throws RESTException {
        try {
            return this.restClient.get().uri(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .retrieve().body(type);
        } catch (RestClientException e) {
            if (e instanceof HttpClientErrorException) {
                HttpClientErrorException hce = (HttpClientErrorException) e;
                if (HttpStatus.UNAUTHORIZED.equals(hce.getStatusCode())) {
                    this.authenticate();
                    return getObject(uri, type);
                } else {
                    throw new RESTException(e);
                }
            } else {
                throw new RESTException(e);
            }
        }
    }

    public <T> T getObject(final URI uri, final TypeReference<T> type) throws RESTException {
        try {
            return this.restClient.get().uri(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .retrieve().body(type);
        } catch (RestClientException e) {
            if (e instanceof HttpClientErrorException) {
                HttpClientErrorException hce = (HttpClientErrorException) e;
                if (HttpStatus.UNAUTHORIZED.equals(hce.getStatusCode())) {
                    this.authenticate();
                    return getObject(uri, type);
                } else {
                    throw new RESTException(e);
                }
            } else {
                throw new RESTException(e);
            }
        }
    }

    public <T> T postObject(final URI uri, final Object object, final Class<T> responseType) throws RESTException {
        try {
            return this.restClient.post().uri(uri).header(HttpHeaders.AUTHORIZATION, "Bearer " + this.accessToken)
                    .contentType(MediaType.APPLICATION_JSON).body(object).retrieve().body(responseType);
        } catch (RestClientException e) {
            if (e instanceof HttpClientErrorException) {
                HttpClientErrorException hce = (HttpClientErrorException) e;
                if (HttpStatus.UNAUTHORIZED.equals(hce.getStatusCode())) {
                    this.authenticate();
                    return postObject(uri, object, responseType);
                } else {
                    throw new RESTException(e);
                }
            } else {
                throw new RESTException(e);
            }
        }
    }
}