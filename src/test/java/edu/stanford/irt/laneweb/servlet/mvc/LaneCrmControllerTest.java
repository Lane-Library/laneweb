package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.stanford.irt.laneweb.crm.CRMService;

public class LaneCrmControllerTest {

    private LaneCrmController controller;

    private CRMService service;
    
    private HttpServletRequest request;
    
    Map<String, Object > requestParameters;
    @Before
    public void setUp() {
        this.service = mock(CRMService.class);
        this.request = mock(HttpServletRequest.class);
        this.controller = new LaneCrmController(this.service);
        requestParameters = new HashMap<String, Object>();
        requestParameters.put("requestedBy.email", "test@stanford.edu"); 

    }

    @Test
    public void testFormSubmitLanelibacqs() throws IOException {
        assertEquals("redirect:/error.html", this.controller.formSubmitLanelibacqs( null, this.request));
    }

    @Test
    public void testJsonSubmitLanelibacqs() throws IOException {
        expect(this.request.getRemoteAddr()).andReturn("ip");
        expect(this.service.submitRequest(Collections.singletonMap("requestedBy.email", "test@stanford.edu"),"ip")).andReturn(200);
        replay(this.request, this.service);
        ResponseEntity<String> response = this.controller.jsonSubmitLanelibacqs(Collections.singletonMap("requestedBy.email", "test@stanford.edu"), this.request);
        assertSame(HttpStatus.OK, response.getStatusCode());
        verify(this.service, this.request);
    }
    

    @Test
    public void testShcValidEmail() throws IOException {
        expect(this.request.getRemoteAddr()).andReturn("ip");
        expect(this.service.submitRequest(Collections.singletonMap("requestedBy.email", "test@stanfordhealthcare.org"),"ip")).andReturn(200);
        replay(this.request, this.service);
        ResponseEntity<String> response = this.controller.jsonSubmitLanelibacqs(Collections.singletonMap("requestedBy.email", "test@stanfordhealthcare.org"), this.request);
        assertSame(HttpStatus.OK, response.getStatusCode());
        verify(this.service, this.request);
    }
    
    @Test
    public void testLpchValidEmail() throws IOException {
        expect(this.request.getRemoteAddr()).andReturn("ip");
        expect(this.service.submitRequest(Collections.singletonMap("requestedBy.email", "test@stanfordchildrens.org"),"ip")).andReturn(200);
        replay(this.request, this.service);
        ResponseEntity<String> response = this.controller.jsonSubmitLanelibacqs(Collections.singletonMap("requestedBy.email", "test@stanfordchildrens.org"), this.request);
        assertSame(HttpStatus.OK, response.getStatusCode());
        verify(this.service, this.request);
    }
    
    @Test
    public void testNotValidEmail() throws IOException {
        ResponseEntity<String> response = this.controller.jsonSubmitLanelibacqs(Collections.singletonMap("requestedBy.email", "test@gmail.com"), this.request);
        assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    
    @Test
    public void testNotEndValidEmail() throws IOException {
        ResponseEntity<String> response = this.controller.jsonSubmitLanelibacqs(Collections.singletonMap("requestedBy.email", "test@stanfordchildrens.orgg"), this.request);
        assertSame(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
    
    
}
