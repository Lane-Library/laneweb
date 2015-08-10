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
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

public class SitemapRequestHandlerTest {

    private ComponentFactory componentFactory;

    private DataBinder dataBinder;

    private SitemapRequestHandler handler;

    private Map<String, Object> model;

    private Pipeline pipeline;

    private Sitemap processor;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private ServletContext servletContext;

    @Before
    public void setUp() throws Exception {
        this.componentFactory = createMock(ComponentFactory.class);
        this.processor = createMock(Sitemap.class);
        this.servletContext = createMock(ServletContext.class);
        this.dataBinder = createMock(DataBinder.class);
        this.handler = new SitemapRequestHandler(this.componentFactory, this.dataBinder, Collections.emptySet(), "",
                this.servletContext, this.processor, null);
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.pipeline = createMock(Pipeline.class);
        this.model = new HashMap<String, Object>();
    }

    @Test
    public void testGetSitemapURI() {
        expect(this.request.getServletPath()).andReturn("/sitemapURI");
        replay(this.componentFactory, this.request);
        assertEquals("/sitemapURI", this.handler.getSitemapURI(this.request));
        verify(this.componentFactory, this.request);
    }

    @Test
    public void testGetSitemapURIPrefix() {
        this.handler = new SitemapRequestHandler(null, this.dataBinder, Collections.emptySet(), "/prefix",
                this.servletContext, this.processor, null);
        expect(this.request.getServletPath()).andReturn("/prefix/sitemapURI");
        replay(this.componentFactory, this.request);
        assertEquals("/sitemapURI", this.handler.getSitemapURI(this.request));
        verify(this.componentFactory, this.request);
    }

    @Test
    public void testHandleHEADRequest() throws Exception {
        expect(this.request.getMethod()).andReturn("HEAD");
        expect(this.request.getServletPath()).andReturn("/index.html");
        expect(this.componentFactory.getComponent("edu.stanford.irt.cocoon.Model", Map.class)).andReturn(this.model);
        this.dataBinder.bind(this.model, this.request);
        expect(this.servletContext.getMimeType("/index.html")).andReturn("text/html");
        this.response.setContentType("text/html");
        expect(this.processor.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        replay(this.componentFactory, this.servletContext, this.response, this.request, this.processor, this.pipeline,
                this.dataBinder);
        this.handler.handleRequest(this.request, this.response);
        verify(this.componentFactory, this.servletContext, this.response, this.request, this.processor, this.pipeline,
                this.dataBinder);
    }

    @Test
    public void testHandleRequest() throws Exception {
        expect(this.request.getMethod()).andReturn("GET");
        expect(this.request.getServletPath()).andReturn("/index.html");
        expect(this.componentFactory.getComponent("edu.stanford.irt.cocoon.Model", Map.class)).andReturn(this.model);
        this.dataBinder.bind(this.model, this.request);
        expect(this.servletContext.getMimeType("/index.html")).andReturn("text/html");
        expect(this.response.getOutputStream()).andReturn(null);
        this.response.setContentType("text/html");
        expect(this.processor.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        this.pipeline.process((OutputStream) null);
        replay(this.componentFactory, this.servletContext, this.response, this.request, this.processor, this.pipeline,
                this.dataBinder);
        this.handler.handleRequest(this.request, this.response);
        verify(this.componentFactory, this.servletContext, this.response, this.request, this.processor, this.pipeline,
                this.dataBinder);
    }

    @Test
    public void testHandleRequestClasses() throws Exception {
        expect(this.request.getMethod()).andReturn("HEAD");
        expect(this.request.getServletPath()).andReturn("/classes/");
        expect(this.componentFactory.getComponent("edu.stanford.irt.cocoon.Model", Map.class)).andReturn(this.model);
        this.dataBinder.bind(this.model, this.request);
        expect(this.servletContext.getMimeType("/classes/")).andReturn(null);
        this.response.setContentType("text/xml");
        expect(this.processor.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        replay(this.componentFactory, this.servletContext, this.response, this.request, this.processor, this.pipeline,
                this.dataBinder);
        this.handler.handleRequest(this.request, this.response);
        verify(this.componentFactory, this.servletContext, this.response, this.request, this.processor, this.pipeline,
                this.dataBinder);
    }

    @Test
    public void testHandleRequestHTML() throws Exception {
        expect(this.request.getMethod()).andReturn("HEAD");
        expect(this.request.getServletPath()).andReturn("/html");
        expect(this.componentFactory.getComponent("edu.stanford.irt.cocoon.Model", Map.class)).andReturn(this.model);
        this.dataBinder.bind(this.model, this.request);
        expect(this.servletContext.getMimeType("/html")).andReturn(null);
        this.response.setContentType("text/html");
        expect(this.processor.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        replay(this.componentFactory, this.servletContext, this.response, this.request, this.processor, this.pipeline,
                this.dataBinder);
        this.handler.handleRequest(this.request, this.response);
        verify(this.componentFactory, this.servletContext, this.response, this.request, this.processor, this.pipeline,
                this.dataBinder);
    }

    @Test
    public void testHandleRequestJSON() throws Exception {
        expect(this.request.getMethod()).andReturn("HEAD");
        expect(this.request.getServletPath()).andReturn("/json");
        expect(this.componentFactory.getComponent("edu.stanford.irt.cocoon.Model", Map.class)).andReturn(this.model);
        this.dataBinder.bind(this.model, this.request);
        expect(this.servletContext.getMimeType("/json")).andReturn(null);
        this.response.setContentType("application/json");
        expect(this.processor.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        replay(this.componentFactory, this.servletContext, this.response, this.request, this.processor, this.pipeline,
                this.dataBinder);
        this.handler.handleRequest(this.request, this.response);
        verify(this.componentFactory, this.servletContext, this.response, this.request, this.processor, this.pipeline,
                this.dataBinder);
    }

    @Test
    public void testHandleRequestMethodNotAllowed() throws Exception {
        this.handler.setMethodsNotAllowed(Collections.singleton("POST"));
        expect(this.request.getMethod()).andReturn("POST");
        this.response.sendError(405);
        replay(this.componentFactory, this.servletContext, this.response, this.request, this.processor, this.pipeline,
                this.dataBinder);
        this.handler.handleRequest(this.request, this.response);
        verify(this.componentFactory, this.servletContext, this.response, this.request, this.processor, this.pipeline,
                this.dataBinder);
    }

    @Test
    public void testHandleRequestRSSPrefix() throws Exception {
        this.handler = new SitemapRequestHandler(this.componentFactory, this.dataBinder, Collections.emptySet(), "/rss",
                this.servletContext, this.processor, null);
        expect(this.request.getMethod()).andReturn("HEAD");
        expect(this.request.getServletPath()).andReturn("/rss/foo");
        expect(this.componentFactory.getComponent("edu.stanford.irt.cocoon.Model", Map.class)).andReturn(this.model);
        this.dataBinder.bind(this.model, this.request);
        expect(this.servletContext.getMimeType("/foo")).andReturn(null);
        this.response.setContentType("text/xml");
        expect(this.processor.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        replay(this.componentFactory, this.servletContext, this.response, this.request, this.processor, this.pipeline,
                this.dataBinder);
        this.handler.handleRequest(this.request, this.response);
        verify(this.componentFactory, this.servletContext, this.response, this.request, this.processor, this.pipeline,
                this.dataBinder);
    }

    @Test
    public void testHandleRequestText() throws Exception {
        expect(this.request.getMethod()).andReturn("HEAD");
        expect(this.request.getServletPath()).andReturn("/foo");
        expect(this.componentFactory.getComponent("edu.stanford.irt.cocoon.Model", Map.class)).andReturn(this.model);
        this.dataBinder.bind(this.model, this.request);
        expect(this.servletContext.getMimeType("/foo")).andReturn(null);
        this.response.setContentType("text/plain");
        expect(this.processor.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        replay(this.componentFactory, this.servletContext, this.response, this.request, this.processor, this.pipeline,
                this.dataBinder);
        this.handler.handleRequest(this.request, this.response);
        verify(this.componentFactory, this.servletContext, this.response, this.request, this.processor, this.pipeline,
                this.dataBinder);
    }

    @Test
    public void testHandleRequestXML() throws Exception {
        expect(this.request.getMethod()).andReturn("HEAD");
        expect(this.request.getServletPath()).andReturn("/xml");
        expect(this.componentFactory.getComponent("edu.stanford.irt.cocoon.Model", Map.class)).andReturn(this.model);
        this.dataBinder.bind(this.model, this.request);
        expect(this.servletContext.getMimeType("/xml")).andReturn(null);
        this.response.setContentType("text/xml");
        expect(this.processor.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        replay(this.componentFactory, this.servletContext, this.response, this.request, this.processor, this.pipeline,
                this.dataBinder);
        this.handler.handleRequest(this.request, this.response);
        verify(this.componentFactory, this.servletContext, this.response, this.request, this.processor, this.pipeline,
                this.dataBinder);
    }

    @Test
    public void testSetMethodsNotAllowed() {
        try {
            this.handler.setMethodsNotAllowed(null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        this.handler.setMethodsNotAllowed(Collections.emptySet());
    }
}
