package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.pipeline.Pipeline;
import edu.stanford.irt.cocoon.sitemap.ComponentFactory;
import edu.stanford.irt.cocoon.sitemap.Sitemap;
import edu.stanford.irt.cocoon.sitemap.SitemapContext;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.laneweb.ResourceNotFoundException;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

public class SitemapHandlerExceptionResolverTest {

    private static final class TestSitemapHandlerExceptionResolver extends SitemapHandlerExceptionResolver {

        public TestSitemapHandlerExceptionResolver(final Map<String, Object> model,
                final ComponentFactory componentFactory, final DataBinder dataBinder,
                final Set<String> methodsNotAllowed, final String prefix, final ServletContext servletContext,
                final Sitemap sitemap, final SourceResolver sourceResolver) {
            super(componentFactory, dataBinder, methodsNotAllowed, prefix, servletContext, sitemap, sourceResolver);
        }
    }

    private ComponentFactory componentFactory;

    private DataBinder dataBinder;

    private Object handler;

    private Map<String, Object> model;

    private HttpServletRequest request;

    private SitemapHandlerExceptionResolver resolver;

    private HttpServletResponse response;

    private ServletContext servletContext;

    private Sitemap sitemap;

    private SourceResolver sourceResolver;

    @Before
    public void setUp() {
        this.componentFactory = createMock(ComponentFactory.class);
        this.sourceResolver = createMock(SourceResolver.class);
        this.dataBinder = createMock(DataBinder.class);
        this.servletContext = createMock(ServletContext.class);
        this.sitemap = createMock(Sitemap.class);
        this.model = new HashMap<String, Object>();
        this.resolver = new TestSitemapHandlerExceptionResolver(this.model, this.componentFactory, this.dataBinder,
                Collections.emptySet(), null, this.servletContext, this.sitemap, this.sourceResolver);
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
    }

    @Test
    public void testResolveClientAbortException() {
        Exception nested = new Exception();
        Exception ex = new ClientAbortException(nested);
        expect(this.request.getRemoteAddr()).andReturn("remoteAddr");
        expect(this.request.getRequestURL()).andReturn(new StringBuffer("requestURL"));
        expect(this.response.isCommitted()).andReturn(true);
        replay(this.sourceResolver, this.componentFactory, this.request, this.response);
        this.resolver.resolveException(this.request, this.response, this.handler, ex);
        verify(this.sourceResolver, this.componentFactory, this.request, this.response);
    }

    @Test
    public void testResolveException() {
        Exception ex = new Exception();
        expect(this.response.isCommitted()).andReturn(true);
        replay(this.sourceResolver, this.componentFactory, this.request, this.response);
        this.resolver.resolveException(this.request, this.response, this.handler, ex);
        verify(this.sourceResolver, this.componentFactory, this.request, this.response);
    }

    @Test
    public void testResolveExceptionNotCommitted() throws IOException {
        Exception ex = new Exception();
        expect(this.response.isCommitted()).andReturn(false);
        expect(this.request.getMethod()).andReturn("GET");
        expect(this.componentFactory.getComponent("edu.stanford.irt.cocoon.Model", Map.class)).andReturn(this.model);
        this.response.setStatus(404);
        this.dataBinder.bind(this.model, this.request);
        expect(this.servletContext.getMimeType("/error.html")).andReturn("text/html");
        this.response.setContentType("text/html");
        Pipeline pipeline = createMock(Pipeline.class);
        expect(this.sitemap.buildPipeline(isA(SitemapContext.class))).andReturn(pipeline);
        expect(this.response.getOutputStream()).andReturn(null);
        pipeline.process((OutputStream) null);
        replay(this.sourceResolver, this.componentFactory, this.request, this.response, this.dataBinder,
                this.servletContext, this.sitemap, pipeline);
        this.resolver.resolveException(this.request, this.response, this.handler, ex);
        verify(this.sourceResolver, this.componentFactory, this.request, this.response, this.dataBinder,
                this.servletContext, this.sitemap, pipeline);
    }

    @Test
    public void testResolveExceptionThrowIOException() throws IOException {
        Exception ex = new Exception();
        expect(this.response.isCommitted()).andReturn(false);
        expect(this.request.getMethod()).andReturn("GET");
        expect(this.componentFactory.getComponent("edu.stanford.irt.cocoon.Model", Map.class)).andReturn(this.model);
        this.response.setStatus(404);
        this.dataBinder.bind(this.model, this.request);
        expect(this.servletContext.getMimeType("/error.html")).andReturn("text/html");
        this.response.setContentType("text/html");
        Pipeline pipeline = createMock(Pipeline.class);
        expect(this.sitemap.buildPipeline(isA(SitemapContext.class))).andReturn(pipeline);
        IOException ioe = new IOException();
        expect(this.response.getOutputStream()).andThrow(ioe);
        replay(this.sourceResolver, this.componentFactory, this.request, this.response, this.dataBinder,
                this.servletContext, this.sitemap, pipeline);
        this.resolver.resolveException(this.request, this.response, this.handler, ex);
        verify(this.sourceResolver, this.componentFactory, this.request, this.response, this.dataBinder,
                this.servletContext, this.sitemap, pipeline);
    }

    @Test
    public void testResolveFileNotFoundException() {
        Exception nested = new FileNotFoundException();
        Exception ex = new Exception(nested);
        expect(this.response.isCommitted()).andReturn(true);
        replay(this.sourceResolver, this.componentFactory, this.request, this.response);
        this.resolver.resolveException(this.request, this.response, this.handler, ex);
        verify(this.sourceResolver, this.componentFactory, this.request, this.response);
    }

    @Test
    public void testResolveNestedException() {
        Exception nested = new Exception();
        Exception ex = new Exception(nested);
        expect(this.response.isCommitted()).andReturn(true);
        replay(this.sourceResolver, this.componentFactory, this.request, this.response);
        this.resolver.resolveException(this.request, this.response, this.handler, ex);
        verify(this.sourceResolver, this.componentFactory, this.request, this.response);
    }

    @Test
    public void testResolveResourceNotFoundException() {
        Exception ex = new ResourceNotFoundException("notfound");
        expect(this.response.isCommitted()).andReturn(true);
        replay(this.sourceResolver, this.componentFactory, this.request, this.response);
        this.resolver.resolveException(this.request, this.response, this.handler, ex);
        verify(this.sourceResolver, this.componentFactory, this.request, this.response);
    }
}
