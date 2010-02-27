package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.*;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.Processor;
import org.apache.cocoon.environment.Environment;
import org.junit.Before;
import org.junit.Test;

public class SitemapRequestHandlerTest {

    private SitemapRequestHandler handler;
    
    private ProxyLinks proxyLinks;
    
    private TemplateChooser templateChooser;
    
    private HttpServletRequest request;

    private HttpServletResponse response;
    
    private Processor processor;
    
    private ServletContext servletContext;

    @Before
    public void setUp() throws Exception {
        this.handler = new SitemapRequestHandler();
        this.request = createMock(HttpServletRequest.class);
        this.proxyLinks = createMock(ProxyLinks.class);
        this.response = createMock(HttpServletResponse.class);
        this.templateChooser = createMock(TemplateChooser.class);
        this.processor = createMock(Processor.class);
        this.servletContext = createMock(ServletContext.class);
        this.handler.setProxyLinks(this.proxyLinks);
        this.handler.setTemplateChooser(this.templateChooser);
        this.handler.setProcessor(this.processor);
        this.handler.setServletContext(this.servletContext);
    }

    @Test
    public void testHandleRequest() throws Exception {
        expect(request.getMethod()).andReturn("GET");
        expect(request.getRequestURI()).andReturn("/");
        expect(request.getParameter(isA(String.class))).andReturn(null).times(2);
        expect(this.request.getParameterNames()).andReturn(Collections.enumeration(Collections.emptyList()));
        this.proxyLinks.setupProxyLinks(this.request);
        this.templateChooser.setupTemplate(this.request);
        expect(this.processor.process(isA(Environment.class))).andReturn(Boolean.TRUE);
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
        replay(this.servletContext);
        replay(this.templateChooser);
        replay(this.response);
        replay(this.request);
        replay(this.processor);
        replay(this.proxyLinks);
    }
    
    private void verifyMocks() {
        verify(this.servletContext);
        verify(this.processor);
        verify(this.templateChooser);
        verify(this.response);
        verify(this.request);
        verify(this.proxyLinks);
    }
}
