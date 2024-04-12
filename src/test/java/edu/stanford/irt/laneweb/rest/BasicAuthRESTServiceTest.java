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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.client.match.MockRestRequestMatchers;
import org.springframework.test.web.client.response.MockRestResponseCreators;

@ContextConfiguration(classes = { RESTClientTestConfiguration.class })
@RestClientTest({ BasicAuthRESTService.class })
class BasicAuthRESTServiceTest {

    private String json = "{\"boolean\": true, \"color\": \"gold\"}";

    @Autowired
    private MockRestServiceServer server;

    @Autowired
    private BasicAuthRESTService service;

    private TypeReference<String> type = new TypeReference<>() {
    };

    private final URI uri = URI.create("http://localhost:8080/");

    @Test
    void testGetInputStream() throws IOException, RESTException, URISyntaxException {
        this.server.expect(MockRestRequestMatchers.requestTo(this.uri))
                .andRespond(MockRestResponseCreators.withSuccess().body(this.json));
        assertSame(new ByteArrayInputStream(this.json.getBytes()).getClass(),
                this.service.getInputStream(this.uri).getClass());
        this.server.verify();
    }

    @Test
    void testGetInputStreamThrows() {
        this.server.expect(MockRestRequestMatchers.requestTo(this.uri)).andExpect(method(HttpMethod.GET))
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
        this.server.expect(MockRestRequestMatchers.requestTo(this.uri)).andExpect(method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess().body(this.json));
        assertEquals(this.service.getObject(this.uri, String.class), this.json);
        this.server.verify();
    }

    @Test
    void testGetObjectURIClassThrows() {
        this.server.expect(MockRestRequestMatchers.requestTo(this.uri)).andExpect(method(HttpMethod.GET))
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
        this.server.expect(MockRestRequestMatchers.requestTo(this.uri)).andExpect(method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withSuccess().body(this.json));
        assertEquals(this.json, this.service.getObject(this.uri, this.type));
        this.server.verify();
    }

    @Test
    void testGetObjectURITypeReferenceThrows() {
        this.server.expect(MockRestRequestMatchers.requestTo(this.uri)).andExpect(method(HttpMethod.GET))
                .andRespond(MockRestResponseCreators.withServerError());
        try {
            this.service.getObject(this.uri, this.type);
        } catch (RESTException e) {
            assertSame(RESTException.class, e.getClass());
        }
        this.server.verify();
    }

    @Test
    void testPostObject() {
        this.server.expect(MockRestRequestMatchers.requestTo(this.uri)).andExpect(method(HttpMethod.POST))
                .andExpect(content().string(this.json)).andRespond(withSuccess());
        this.service.postObject(this.uri, this.json, Boolean.class);
        this.server.verify();
    }

    @Test
    void testPostObjectThrows() {
        this.server.expect(MockRestRequestMatchers.requestTo(this.uri)).andExpect(method(HttpMethod.POST))
                .andExpect(content().string(this.json)).andRespond(MockRestResponseCreators.withServerError());
        try {
            this.service.postObject(this.uri, this.json, Boolean.class);
        } catch (RESTException e) {
            assertSame(RESTException.class, e.getClass());
        }
        this.server.verify();
    }
}
