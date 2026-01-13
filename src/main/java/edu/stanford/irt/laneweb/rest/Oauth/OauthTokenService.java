package edu.stanford.irt.laneweb.rest.Oauth;

import java.net.URI;
import java.time.Instant;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.LanewebException;

public class OauthTokenService {

    private String accessToken;
    private Instant expirationTime;

    private RestClient restClient;
    private URI tokenEndpoint;
    private String userInfo;
    private static final String GRANT_TYPE = "client_credentials";

    public OauthTokenService(RestClient restClient, URI tokenEndpoint, String userInfo) {
        this.restClient = restClient;
        this.tokenEndpoint = tokenEndpoint;
        this.userInfo = userInfo;
        this.accessToken = null;
        this.expirationTime = Instant.EPOCH;
    }

    public void resetToken() {
        this.accessToken = null;
        this.expirationTime = Instant.EPOCH;
    }

    public synchronized String getAccessToken() {
        if (expirationTime.isBefore(Instant.now())) {
            requestAccessToken();
        }
        return this.accessToken;
    }

    private void requestAccessToken() {
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
            this.accessToken = jsonResponse.get("access_token").asString();
            this.expirationTime = Instant.now().plusSeconds(jsonResponse.get("expires_in").asLong());
        } catch (Exception e) {
            throw new LanewebException(e);
        }
    }
}