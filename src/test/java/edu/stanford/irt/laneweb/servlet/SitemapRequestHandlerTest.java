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

    private RedirectProcessor redirectProcessor;

    @Before
    public void setUp() throws Exception {
        this.handler = new SitemapRequestHandler();
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.processor = createMock(Processor.class);
        this.servletContext = createMock(ServletContext.class);
        this.redirectProcessor = createMock(RedirectProcessor.class);
        this.handler.setProcessor(this.processor);
        this.handler.setServletContext(this.servletContext);
        this.handler.setRedirectProcessor(this.redirectProcessor);
    }

    @Test
    public void testHandleRequest() throws Exception {
        expect(request.getMethod()).andReturn("GET");
        expect(this.redirectProcessor.getRedirectURL("/")).andReturn(RedirectProcessor.NO_REDIRECT);
        expect(this.request.getRequestURI()).andReturn("/").times(3);
        expect(this.request.getQueryString()).andReturn(null);
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
    public void testSetMethodsNotAllowed() {
        try {
            this.handler.setMethodsNotAllowed(null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        this.handler.setMethodsNotAllowed(Collections.<String> emptySet());
    }

    @Test
    public void testSetRedirectProcessor() {
        try {
            this.handler.setRedirectProcessor(null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        this.handler.setRedirectProcessor(this.redirectProcessor);
    }
    
    private void replayMocks() {
        replay(this.servletContext);
        replay(this.response);
        replay(this.request);
        replay(this.processor);
        replay(this.redirectProcessor);
    }
    
    private void verifyMocks() {
        verify(this.servletContext);
        verify(this.processor);
        verify(this.response);
        verify(this.request);
        verify(this.redirectProcessor);
    }
}
