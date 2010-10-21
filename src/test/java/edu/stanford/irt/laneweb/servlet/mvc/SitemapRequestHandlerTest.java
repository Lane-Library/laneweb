package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.Processor;
import org.apache.cocoon.environment.Environment;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.servlet.binding.DataBinder;
import edu.stanford.irt.laneweb.servlet.mvc.SitemapRequestHandler;
import edu.stanford.irt.laneweb.servlet.redirect.DefaultRedirectProcessor;
import edu.stanford.irt.laneweb.servlet.redirect.RedirectProcessor;

public class SitemapRequestHandlerTest {

    private SitemapRequestHandler handler;

    private Processor processor;

    private RedirectProcessor redirectProcessor;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private ServletContext servletContext;
    
    private DataBinder dataBinder;

    @Before
    public void setUp() throws Exception {
        this.handler = new SitemapRequestHandler(){

            @Override
            protected Map<String, Object> getModel() {
                return new HashMap<String, Object>();
            }};
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.processor = createMock(Processor.class);
        this.servletContext = createMock(ServletContext.class);
        this.redirectProcessor = createMock(DefaultRedirectProcessor.class);
        this.dataBinder = createMock(DataBinder.class);
        this.handler.setProcessor(this.processor);
        this.handler.setServletContext(this.servletContext);
        this.handler.setRedirectProcessor(this.redirectProcessor);
        this.handler.setDataBinder(this.dataBinder);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHandleRequest() throws Exception {
        expect(this.request.getMethod()).andReturn("GET");
        expect(this.redirectProcessor.getRedirectURL("/index.html", "", null)).andReturn(null);
        expect(this.request.getRequestURI()).andReturn("/index.html");
        expect(this.request.getQueryString()).andReturn(null);
        expect(this.request.getContextPath()).andReturn("");
        expect(this.request.getParameter("cocoon-view")).andReturn(null);
        expect(this.request.getParameter("cocoon-action")).andReturn(null);
        expect(this.request.getParameterNames()).andReturn(Collections.enumeration(Collections.emptySet()));
        expect(this.processor.process(isA(Environment.class))).andReturn(Boolean.TRUE);
        expect(this.servletContext.getMimeType("/index.html")).andReturn(null);
        this.dataBinder.bind(isA(Map.class), isA(HttpServletRequest.class));
        this.response.setContentType(null);
        replay(this.servletContext, this.response, this.request, this.processor, this.redirectProcessor);
        this.handler.handleRequest(this.request, this.response);
        verify(this.servletContext, this.processor, this.response, this.request, this.redirectProcessor);
    }
    
    @Test
    public void testAddBasePathToNewPageRedirect() throws ServletException, IOException {
        expect(this.request.getMethod()).andReturn("GET");
        expect(this.request.getContextPath()).andReturn("/baz");
        expect(this.request.getRequestURI()).andReturn("/baz/foo.html");
        expect(this.request.getQueryString()).andReturn(null);
        expect(this.redirectProcessor.getRedirectURL("/foo.html", "/baz", null)).andReturn("/baz/newpage.html?page=/baz/bar/foo.html");
        this.response.sendRedirect("/baz/newpage.html?page=/baz/bar/foo.html");
        replay(this.servletContext, this.response, this.request, this.processor, this.redirectProcessor);
        this.handler.handleRequest(this.request, this.response);
        verify(this.servletContext, this.processor, this.response, this.request, this.redirectProcessor);
        
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
}
