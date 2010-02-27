package edu.stanford.irt.laneweb.servlet;

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

public class SitemapRequestHandlerTest {

    private SitemapRequestHandler handler;

    private RequestProcessor processor;
    
    private ProxyLinks proxyLinks;
    
    private HttpServletRequest request;

    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        this.processor = createMock(RequestProcessor.class);
        this.handler = new SitemapRequestHandler() {

            @Override
            protected RequestProcessor getRequestProcessor() {
                return SitemapRequestHandlerTest.this.processor;
            }
        };
        this.request = createMock(HttpServletRequest.class);
        this.proxyLinks = createMock(ProxyLinks.class);
        this.response = createMock(HttpServletResponse.class);
        this.handler.setProxyLinks(this.proxyLinks);
    }

    @Test
    public void testHandleRequest() throws ServletException, IOException {
        expect(request.getMethod()).andReturn("GET");
        expect(request.getRequestURI()).andReturn("/");
        this.processor.service(request, response);
        this.proxyLinks.setupProxyLinks(this.request);
        replayMocks();
        this.handler.handleRequest(request, response);
        verifyMocks();
    }
    
    @Test
    public void testHandleRequestRedirect() throws ServletException, IOException {
        this.handler.setRedirectMap(Collections.singletonMap("(.*)/", "$1/index.html"));
        expect(request.getMethod()).andReturn("GET");
        expect(request.getRequestURI()).andReturn("/foo/");
        response.sendRedirect("/foo/index.html");
        replayMocks();
        this.handler.handleRequest(request, response);
        verifyMocks();
    }
    
    @Test
    public void testHandleRequestRedirectSlash() throws ServletException, IOException {
        this.handler.setRedirectMap(Collections.singletonMap("(.*)/", "$1/index.html"));
        expect(request.getMethod()).andReturn("GET");
        expect(request.getRequestURI()).andReturn("/");
        response.sendRedirect("/index.html");
        replayMocks();
        this.handler.handleRequest(request, response);
        verifyMocks();
    }
    
    @Test
    public void testHandleRequestRedirectLiaisons() throws ServletException, IOException {
        this.handler.setRedirectMap(Collections.singletonMap("(.*)/liaisons/index.html", "$1/contacts/index.html?loadTab=liaisons"));
        expect(request.getMethod()).andReturn("GET");
        expect(request.getRequestURI()).andReturn("/stage/liaisons/index.html");
        response.sendRedirect("/stage/contacts/index.html?loadTab=liaisons");
        replayMocks();
        this.handler.handleRequest(request, response);
        verifyMocks();
    }
    
    @Test
    public void testHandleRequestRedirectClasses() throws ServletException, IOException {
        this.handler.setRedirectMap(Collections.singletonMap("(.*)/classes/index.html", "$1/services/workshops/laneclasses.html"));
        expect(request.getMethod()).andReturn("GET");
        expect(request.getRequestURI()).andReturn("/classes/index.html");
        response.sendRedirect("/services/workshops/laneclasses.html");
        replayMocks();
        this.handler.handleRequest(request, response);
        verifyMocks();
    }
    
    @Test
    public void testHandleRequestRedirectClinician() throws ServletException, IOException {
        this.handler.setRedirectMap(Collections.singletonMap("(.*)/clinician/index.html", "$1/portals/clinical.html"));
        expect(request.getMethod()).andReturn("GET");
        expect(request.getRequestURI()).andReturn("/foo/bar/clinician/index.html");
        response.sendRedirect("/foo/bar/portals/clinical.html");
        replayMocks();
        this.handler.handleRequest(request, response);
        verifyMocks();
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
    
    private void replayMocks() {
        replay(this.response);
        replay(this.request);
        replay(this.processor);
        replay(this.proxyLinks);
    }
    
    private void verifyMocks() {
        verify(this.response);
        verify(this.request);
        verify(this.processor);
        verify(this.proxyLinks);
    }
}
