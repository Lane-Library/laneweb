package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.Processor;
import org.apache.cocoon.environment.Environment;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.servlet.binding.DataBinder;
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

    @Test
    public void testHandleRequest() throws Exception {
        expect(this.request.getMethod()).andReturn("GET");
        expect(this.redirectProcessor.getRedirectURL("/")).andReturn(null);
        expect(this.request.getRequestURI()).andReturn("/").times(2);
        expect(this.request.getQueryString()).andReturn(null).times(1);
        expect(this.request.getContextPath()).andReturn("").times(2);
        expect(this.request.getParameter("cocoon-view")).andReturn(null);
        expect(this.request.getParameter("cocoon-action")).andReturn(null);
        expect(this.request.getParameterNames()).andReturn(Collections.enumeration(Collections.emptySet()));
        expect(this.processor.process(isA(Environment.class))).andReturn(Boolean.TRUE);
        expect(this.servletContext.getMimeType("")).andReturn(null);
        this.dataBinder.bind(isA(Map.class), isA(HttpServletRequest.class));
        this.response.setContentType(null);
        replayMocks();
        this.handler.handleRequest(this.request, this.response);
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
