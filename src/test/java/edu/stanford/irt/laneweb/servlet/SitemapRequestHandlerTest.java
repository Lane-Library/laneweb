package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.Processor;
import org.apache.cocoon.environment.Environment;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class SitemapRequestHandlerTest {

    private SitemapRequestHandler handler;
    
    private HttpServletRequest request;

    private HttpServletResponse response;
    
    private Processor processor;
    
    private ServletContext servletContext;

    @Before
    public void setUp() throws Exception {
        this.handler = new SitemapRequestHandler();
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.processor = createMock(Processor.class);
        this.servletContext = createMock(ServletContext.class);
        this.handler.setProcessor(this.processor);
        this.handler.setServletContext(this.servletContext);
    }

    @Test
    public void testHandleRequest() throws Exception {
        expect(request.getMethod()).andReturn("GET");
        expect(this.request.getRequestURI()).andReturn("/").times(3);
        expect(this.request.getContextPath()).andReturn("").times(2);
        expect(this.request.getParameter("cocoon-view")).andReturn(null);
        expect(this.request.getParameter("cocoon-action")).andReturn(null);
        expect(this.request.getParameterNames()).andReturn(Collections.enumeration(Collections.emptySet()));
        this.request.setAttribute(eq(Model.MODEL), isA(Map.class));
        expect(this.servletContext.getAttribute(isA(String.class))).andReturn("foo").times(3);
        expect(this.servletContext.getRealPath("/")).andReturn("/tmp");
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
        replay(this.response);
        replay(this.request);
        replay(this.processor);
    }
    
    private void verifyMocks() {
        verify(this.servletContext);
        verify(this.processor);
        verify(this.response);
        verify(this.request);
    }
}
