package edu.stanford.irt.laneweb;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.servlet.RequestProcessor;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.SitemapRequestHandler;

public class SitemapRequestHandlerTest {

    private SitemapRequestHandler handler;

    private RequestProcessor processor;

    @Before
    public void setUp() throws Exception {
        this.processor = createMock(RequestProcessor.class);
        this.handler = new SitemapRequestHandler() {

            @Override
            protected RequestProcessor getRequestProcessor() {
                return SitemapRequestHandlerTest.this.processor;
            }
        };
    }

    @Test
    public void testHandleRequest() throws ServletException, IOException {
        HttpServletRequest request = createMock(HttpServletRequest.class);
        expect(request.getMethod()).andReturn("GET");
        expect(request.getRequestURI()).andReturn("/");
        replay(request);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        this.processor.service(request, response);
        replay(this.processor);
        this.handler.handleRequest(request, response);
        verify(this.processor);
    }
    
    @Test
    public void testHandleRequestRedirect() throws ServletException, IOException {
        this.handler.setRedirectMap(Collections.singletonMap("(.*)/", "$1/index.html"));
        HttpServletRequest request = createMock(HttpServletRequest.class);
        expect(request.getMethod()).andReturn("GET");
        expect(request.getRequestURI()).andReturn("/foo/");
        replay(request);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        response.sendRedirect("/foo/index.html");
        replay(response);
        this.handler.handleRequest(request, response);
        verify(request);
        verify(response);
    }
    
    @Test
    public void testHandleRequestRedirectSlash() throws ServletException, IOException {
        this.handler.setRedirectMap(Collections.singletonMap("(.*)/", "$1/index.html"));
        HttpServletRequest request = createMock(HttpServletRequest.class);
        expect(request.getMethod()).andReturn("GET");
        expect(request.getRequestURI()).andReturn("/");
        replay(request);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        response.sendRedirect("/index.html");
        replay(response);
        this.handler.handleRequest(request, response);
        verify(request);
        verify(response);
    }
    
    @Test
    public void testHandleRequestRedirectLiaisons() throws ServletException, IOException {
        this.handler.setRedirectMap(Collections.singletonMap("(.*)/liaisons/index.html", "$1/contacts/index.html?loadTab=liaisons"));
        HttpServletRequest request = createMock(HttpServletRequest.class);
        expect(request.getMethod()).andReturn("GET");
        expect(request.getRequestURI()).andReturn("/stage/liaisons/index.html");
        replay(request);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        response.sendRedirect("/stage/contacts/index.html?loadTab=liaisons");
        replay(response);
        this.handler.handleRequest(request, response);
        verify(request);
        verify(response);
    }
    
    @Test
    public void testHandleRequestRedirectClasses() throws ServletException, IOException {
        this.handler.setRedirectMap(Collections.singletonMap("(.*)/classes/index.html", "$1/services/workshops/laneclasses.html"));
        HttpServletRequest request = createMock(HttpServletRequest.class);
        expect(request.getMethod()).andReturn("GET");
        expect(request.getRequestURI()).andReturn("/classes/index.html");
        replay(request);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        response.sendRedirect("/services/workshops/laneclasses.html");
        replay(response);
        this.handler.handleRequest(request, response);
        verify(request);
        verify(response);
    }
    
    @Test
    public void testHandleRequestRedirectClinician() throws ServletException, IOException {
        this.handler.setRedirectMap(Collections.singletonMap("(.*)/clinician/index.html", "$1/portals/clinical.html"));
        HttpServletRequest request = createMock(HttpServletRequest.class);
        expect(request.getMethod()).andReturn("GET");
        expect(request.getRequestURI()).andReturn("/foo/bar/clinician/index.html");
        replay(request);
        HttpServletResponse response = createMock(HttpServletResponse.class);
        response.sendRedirect("/foo/bar/portals/clinical.html");
        replay(response);
        this.handler.handleRequest(request, response);
        verify(request);
        verify(response);
    }
    

    @Test
    public void testSetMethodsNotAllowed() {
        try {
            this.handler.setMethodsNotAllowed(null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        this.handler.setMethodsNotAllowed(Collections.<String> emptySet());
    }

    @Test
    public void testSetRedirectMap() {
        try {
            this.handler.setRedirectMap(null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        this.handler.setRedirectMap(Collections.<String, String> emptyMap());
    }
}
