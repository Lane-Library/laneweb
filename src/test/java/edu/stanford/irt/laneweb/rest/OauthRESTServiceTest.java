package edu.stanford.irt.laneweb.rest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

import edu.stanford.irt.laneweb.rest.Oauth.OauthRESTService;

@ActiveProfiles(profiles = "test") @ContextConfiguration(classes = { OauthTestConfiguration.class }) @RestClientTest({
        OauthRESTService.class })
class OauthRESTServiceTest {
    private static final String JSON = "{\"boolean\": true, \"color\": \"gold\"}";

    @Autowired
    private OauthRESTService service;

    @Autowired
    private MockRestServiceServer server;

    private final URI uri = URI.create("http://localhost:8080/");

    @BeforeEach
    void setUp() {
        // Reset token cache before each test
        this.service.resetToken();
    }

    @Test
    void testGetObjectReturnsObject() throws Exception {
        // Arrange: mock token endpoint
        URI tokenEndpointUri = URI.create("http://localhost:8080/oauth2/token");
        String tokenJson = "{\"access_token\":\"abc123\", \"expires_in\":3600}";
        this.server.expect(MockRestRequestMatchers.requestTo(tokenEndpointUri))
                .andRespond(MockRestResponseCreators.withSuccess(tokenJson, MediaType.APPLICATION_JSON));

        this.server.expect(MockRestRequestMatchers.requestTo(this.uri))
                .andRespond(MockRestResponseCreators.withSuccess().body(JSON));
        InputStream result = this.service.getInputStream(this.uri);
        // Assert
        assertEquals(new ByteArrayInputStream(JSON.getBytes()).getClass(), result.getClass());
        this.server.verify();
    }

    @Test
    void testGetObjectURIClass() {
        // Arrange: mock token endpoint first
        URI tokenEndpointUri = URI.create("http://localhost:8080/oauth2/token");
        String tokenJson = "{\"access_token\":\"abc123\", \"expires_in\":3600}";
        this.server.expect(MockRestRequestMatchers.requestTo(tokenEndpointUri))
                .andRespond(MockRestResponseCreators.withSuccess(tokenJson, MediaType.APPLICATION_JSON));

        this.server.expect(MockRestRequestMatchers.requestTo(this.uri)).andExpect(method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess().body(JSON));
        assertEquals(this.service.getObject(this.uri, String.class), JSON);
        this.server.verify();
    }

    @Test
    void testNotLoginGetObjectURIClass() {
        // First mock the token endpoint for initial token request
        URI tokenEndpointUri = URI.create("http://localhost:8080/oauth2/token");
        String tokenJson = "{\"access_token\":\"abc123\", \"expires_in\":3600}";
        this.server.expect(MockRestRequestMatchers.requestTo(tokenEndpointUri))
                .andRespond(MockRestResponseCreators.withSuccess(tokenJson, MediaType.APPLICATION_JSON));

        // Request to actual endpoint returns UNAUTHORIZED, which should result in RESTException
        this.server.expect(MockRestRequestMatchers.requestTo(this.uri)).andExpect(method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withStatus(HttpStatus.UNAUTHORIZED));

        // The 401 should cause a RESTException to be thrown
        assertThrows(RESTException.class, () -> this.service.getObject(this.uri, String.class));
        this.server.verify();
    }

    @Test
    void testIOExceptionGetObjectURIClass() {
        // Mock token endpoint first
        URI tokenEndpointUri = URI.create("http://localhost:8080/oauth2/token");
        String tokenJson = "{\"access_token\":\"abc123\", \"expires_in\":3600}";
        this.server.expect(MockRestRequestMatchers.requestTo(tokenEndpointUri))
                .andRespond(MockRestResponseCreators.withSuccess(tokenJson, MediaType.APPLICATION_JSON));

        this.server.expect(MockRestRequestMatchers.requestTo(this.uri)).andExpect(method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withException(new IOException("IO Exception")));
        assertThrows(RESTException.class, () -> this.service.getObject(this.uri, String.class));
        this.server.verify();
    }

}
