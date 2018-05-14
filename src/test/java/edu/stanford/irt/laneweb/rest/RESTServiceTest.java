package edu.stanford.irt.laneweb.rest;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestOperations;

public class RESTServiceTest {

    private ResponseEntity<String> entity;

    private RestOperations restOperations;

    private String result;

    private RESTService service;

    private TypeReference<String> type;

    private URI uri;

    @Before
    public void setUp() throws URISyntaxException {
        this.restOperations = mock(RestOperations.class);
        this.service = new RESTService(this.restOperations);
        this.uri = new URI("/");
        this.type = new TypeReference<String>() {
        };
        this.entity = mock(ResponseEntity.class);
        this.result = "";
    }

    @Test
    public void testGetObjectURIClass() {
        expect(this.restOperations.getForObject(this.uri, String.class)).andReturn(this.result);
        replay(this.restOperations);
        assertSame(this.result, this.service.getObject(this.uri, String.class));
        verify(this.restOperations);
    }

    @Test
    public void testGetObjectURITypeReference() {
        expect(this.restOperations.exchange(this.uri, HttpMethod.GET, null, this.type)).andReturn(this.entity);
        expect(this.entity.getBody()).andReturn(this.result);
        replay(this.restOperations, this.entity);
        assertSame(this.result, this.service.getObject(this.uri, this.type));
        verify(this.restOperations, this.entity);
    }
}
