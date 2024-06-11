package edu.stanford.irt.laneweb.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

@ActiveProfiles(profiles = "test")
@ContextConfiguration(classes = { RESTClientTestConfiguration.class })
@RestClientTest({ RESTService.class })
class RESTServiceTest {

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private RESTService service;

    private final URI uri = URI.create("http://localhost:8080/");

    private TypeReference<String> type = new TypeReference<String>() {
    };

    private String json = "{\"publicationType\":[{\"valueCount\":1477475,\"value\":\"Research Support, Non-U.S. Gov't\",\"field\":{\"name\":\"publicationType\"},\"key\":{\"name\":\"publicationType\"}}]}";

    @Test
    void testGetInputStream() throws IOException, RESTException, URISyntaxException {
        this.server.expect(MockRestRequestMatchers.requestTo(uri))
                .andRespond(MockRestResponseCreators.withSuccess().body(this.json));
        assertSame(new ByteArrayInputStream(json.getBytes()).getClass(), this.service.getInputStream(uri).getClass());
        this.server.verify();
    }

    @Test
    void testGetInputStreamThrows() {
        this.server.expect(MockRestRequestMatchers.requestTo(uri)).andExpect(method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withServerError());
        try {
            this.service.getInputStream(this.uri);
        } catch (RESTException e) {
            assertSame(RESTException.class, e.getClass());
        }
        this.server.verify();
    }

    @Test
    void testGetObjectURIClass() {
        this.server.expect(MockRestRequestMatchers.requestTo(uri)).andExpect(method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess().body(this.json));
        assertEquals(this.service.getObject(uri, String.class), json);
        this.server.verify();
    }

    @Test
    void testGetObjectURIClassThrows() {
        this.server.expect(MockRestRequestMatchers.requestTo(uri)).andExpect(method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withServerError().body(this.json));
        try {
            this.service.getObject(this.uri, String.class);
        } catch (RESTException e) {
            assertSame(RESTException.class, e.getClass());
        }
        this.server.verify();
    }

    @Test
    void testGetObjectURITypeReference() {
        this.server.expect(MockRestRequestMatchers.requestTo(uri)).andExpect(method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess().body(this.json));
        assertEquals(this.json, this.service.getObject(this.uri, this.type));
        this.server.verify();
    }

    @Test
    void testGetObjectURITypeReferenceThrows() {
        this.server.expect(MockRestRequestMatchers.requestTo(uri)).andExpect(method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withServerError());
        try {
            this.service.getObject(this.uri, this.type);
        } catch (RESTException e) {
            assertSame(RESTException.class, e.getClass());
        }
        this.server.verify();
    }

    @Test
    void testPostURLEncodedString() {
        this.server.expect(MockRestRequestMatchers.requestTo(uri)).andExpect(method(HttpMethod.POST))
                .andExpect(content().string(this.json)).andRespond(MockRestResponseCreators.withSuccess());
        assertEquals(200, this.service.postURLEncodedString(uri, this.json));
        this.server.verify();
    }

    @Test
    void testPostURLEncodedStringThrows() {
        this.server.expect(MockRestRequestMatchers.requestTo(uri)).andExpect(method(HttpMethod.POST))
                .andExpect(content().string(this.json)).andRespond(MockRestResponseCreators.withServerError());
        try {
            this.service.postURLEncodedString(uri, this.json);
        } catch (RESTException e) {
            assertSame(RESTException.class, e.getClass());
        }
        this.server.verify();
    }

    @Test
    void testPutObject() {
        this.server.expect(MockRestRequestMatchers.requestTo(this.uri)).andExpect(method(HttpMethod.PUT))
                .andExpect(content().string(this.json)).andRespond(withSuccess());
        this.service.putObject(this.uri, this.json);
        this.server.verify();
    }

    @Test
    void testPutObjectThrows() {
        this.server.expect(MockRestRequestMatchers.requestTo(uri)).andExpect(method(HttpMethod.PUT))
                .andExpect(content().string(this.json)).andRespond(MockRestResponseCreators.withSuccess());
        try {
            this.service.putObject(this.uri, this.json);
        } catch (RESTException e) {
            assertSame(RESTException.class, e.getClass());
        }
        this.server.verify();
    }
}
