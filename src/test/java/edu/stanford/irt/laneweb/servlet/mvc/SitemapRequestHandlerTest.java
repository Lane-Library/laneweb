package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.pipeline.Pipeline;
import edu.stanford.irt.cocoon.sitemap.ComponentFactory;
import edu.stanford.irt.cocoon.sitemap.Sitemap;
import edu.stanford.irt.cocoon.sitemap.SitemapContext;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

public class SitemapRequestHandlerTest {

    private static final class TestHandler extends SitemapRequestHandler {

        public TestHandler(final ComponentFactory componentFactory, final SourceResolver sourceResolver) {
            super(componentFactory, sourceResolver);
        }

        @Override
        protected Map<String, Object> getModel() {
            return new HashMap<String, Object>();
        }
    }

    private DataBinder dataBinder;

    private SitemapRequestHandler handler;

    private Pipeline pipeline;

    private Sitemap processor;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private ServletContext servletContext;

    @Before
    public void setUp() throws Exception {
        this.handler = new TestHandler(null, null);
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.processor = createMock(Sitemap.class);
        this.servletContext = createMock(ServletContext.class);
        this.dataBinder = createMock(DataBinder.class);
        this.handler.setSitemap(this.processor);
        this.handler.setServletContext(this.servletContext);
        this.handler.setDataBinder(this.dataBinder);
        this.pipeline = createMock(Pipeline.class);
    }

    @Test
    public void testGetSitemapURI() {
        expect(this.request.getServletPath()).andReturn("/sitemapURI");
        replay(this.request);
        assertEquals("/sitemapURI", this.handler.getSitemapURI(this.request));
        verify(this.request);
    }

    @Test
    public void testGetSitemapURIJsessionid() {
        expect(this.request.getServletPath()).andReturn("/sitemapURI;jsessionid=jsessionid");
        replay(this.request);
        assertEquals("/sitemapURI", this.handler.getSitemapURI(this.request));
        verify(this.request);
    }

    @Test
    public void testGetSitemapURIPrefix() {
        this.handler.setPrefix("/prefix");
        expect(this.request.getServletPath()).andReturn("/prefix/sitemapURI");
        replay(this.request);
        assertEquals("/sitemapURI", this.handler.getSitemapURI(this.request));
        verify(this.request);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHandleHEADRequest() throws Exception {
        expect(this.request.getMethod()).andReturn("HEAD");
        expect(this.request.getServletPath()).andReturn("/index.html");
        expect(this.servletContext.getMimeType("/index.html")).andReturn("text/html");
        this.response.setContentType("text/html");
        expect(this.processor.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        this.dataBinder.bind(isA(Map.class), isA(HttpServletRequest.class));
        replay(this.servletContext, this.response, this.request, this.processor, this.pipeline, this.dataBinder);
        this.handler.handleRequest(this.request, this.response);
        verify(this.servletContext, this.response, this.request, this.processor, this.pipeline, this.dataBinder);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHandleRequest() throws Exception {
        expect(this.request.getMethod()).andReturn("GET");
        expect(this.request.getServletPath()).andReturn("/index.html");
        expect(this.servletContext.getMimeType("/index.html")).andReturn("text/html");
        expect(this.response.getOutputStream()).andReturn(null);
        this.response.setContentType("text/html");
        expect(this.processor.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        this.pipeline.process((OutputStream) null);
        this.dataBinder.bind(isA(Map.class), isA(HttpServletRequest.class));
        replay(this.servletContext, this.response, this.request, this.processor, this.pipeline, this.dataBinder);
        this.handler.handleRequest(this.request, this.response);
        verify(this.servletContext, this.response, this.request, this.processor, this.pipeline, this.dataBinder);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHandleRequestClasses() throws Exception {
        expect(this.request.getMethod()).andReturn("HEAD");
        expect(this.request.getServletPath()).andReturn("/classes/");
        expect(this.servletContext.getMimeType("/classes/")).andReturn(null);
        this.response.setContentType("text/xml");
        expect(this.processor.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        this.dataBinder.bind(isA(Map.class), isA(HttpServletRequest.class));
        replay(this.servletContext, this.response, this.request, this.processor, this.pipeline, this.dataBinder);
        this.handler.handleRequest(this.request, this.response);
        verify(this.servletContext, this.response, this.request, this.processor, this.pipeline, this.dataBinder);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHandleRequestHTML() throws Exception {
        expect(this.request.getMethod()).andReturn("HEAD");
        expect(this.request.getServletPath()).andReturn("/html");
        expect(this.servletContext.getMimeType("/html")).andReturn(null);
        this.response.setContentType("text/html");
        expect(this.processor.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        this.dataBinder.bind(isA(Map.class), isA(HttpServletRequest.class));
        replay(this.servletContext, this.response, this.request, this.processor, this.pipeline, this.dataBinder);
        this.handler.handleRequest(this.request, this.response);
        verify(this.servletContext, this.response, this.request, this.processor, this.pipeline, this.dataBinder);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHandleRequestJSON() throws Exception {
        expect(this.request.getMethod()).andReturn("HEAD");
        expect(this.request.getServletPath()).andReturn("/json");
        expect(this.servletContext.getMimeType("/json")).andReturn(null);
        this.response.setContentType("application/json");
        expect(this.processor.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        this.dataBinder.bind(isA(Map.class), isA(HttpServletRequest.class));
        replay(this.servletContext, this.response, this.request, this.processor, this.pipeline, this.dataBinder);
        this.handler.handleRequest(this.request, this.response);
        verify(this.servletContext, this.response, this.request, this.processor, this.pipeline, this.dataBinder);
    }

    @Test
    public void testHandleRequestMethodNotAllowed() throws Exception {
        this.handler.setMethodsNotAllowed(Collections.singleton("POST"));
        expect(this.request.getMethod()).andReturn("POST");
        this.response.sendError(405);
        replay(this.servletContext, this.response, this.request, this.processor, this.pipeline, this.dataBinder);
        this.handler.handleRequest(this.request, this.response);
        verify(this.servletContext, this.response, this.request, this.processor, this.pipeline, this.dataBinder);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHandleRequestRSSPrefix() throws Exception {
        this.handler.setPrefix("/rss");
        expect(this.request.getMethod()).andReturn("HEAD");
        expect(this.request.getServletPath()).andReturn("/rss/foo");
        expect(this.servletContext.getMimeType("/foo")).andReturn(null);
        this.response.setContentType("text/xml");
        expect(this.processor.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        this.dataBinder.bind(isA(Map.class), isA(HttpServletRequest.class));
        replay(this.servletContext, this.response, this.request, this.processor, this.pipeline, this.dataBinder);
        this.handler.handleRequest(this.request, this.response);
        verify(this.servletContext, this.response, this.request, this.processor, this.pipeline, this.dataBinder);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHandleRequestText() throws Exception {
        expect(this.request.getMethod()).andReturn("HEAD");
        expect(this.request.getServletPath()).andReturn("/foo");
        expect(this.servletContext.getMimeType("/foo")).andReturn(null);
        this.response.setContentType("text/plain");
        expect(this.processor.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        this.dataBinder.bind(isA(Map.class), isA(HttpServletRequest.class));
        replay(this.servletContext, this.response, this.request, this.processor, this.pipeline, this.dataBinder);
        this.handler.handleRequest(this.request, this.response);
        verify(this.servletContext, this.response, this.request, this.processor, this.pipeline, this.dataBinder);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testHandleRequestXML() throws Exception {
        expect(this.request.getMethod()).andReturn("HEAD");
        expect(this.request.getServletPath()).andReturn("/xml");
        expect(this.servletContext.getMimeType("/xml")).andReturn(null);
        this.response.setContentType("text/xml");
        expect(this.processor.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        this.dataBinder.bind(isA(Map.class), isA(HttpServletRequest.class));
        replay(this.servletContext, this.response, this.request, this.processor, this.pipeline, this.dataBinder);
        this.handler.handleRequest(this.request, this.response);
        verify(this.servletContext, this.response, this.request, this.processor, this.pipeline, this.dataBinder);
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
}
