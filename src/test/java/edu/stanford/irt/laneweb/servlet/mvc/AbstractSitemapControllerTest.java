package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.pipeline.Pipeline;
import edu.stanford.irt.cocoon.sitemap.ComponentFactory;
import edu.stanford.irt.cocoon.sitemap.Sitemap;
import edu.stanford.irt.cocoon.sitemap.SitemapContext;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

public class AbstractSitemapControllerTest {

    private static class TestAbstractSitemapController extends AbstractSitemapController {

        public TestAbstractSitemapController(final ComponentFactory componentFactory, final DataBinder dataBinder,
                final Sitemap sitemap, final SourceResolver sourceResolver) {
            super(componentFactory, dataBinder, sitemap, sourceResolver);
        }

        @Override
        public void handleRequest(final HttpServletRequest request, final HttpServletResponse response)
                throws IOException {
            super.doHandleRequest(request, response, "");
        }
    }

    private ComponentFactory componentFactory;

    private DataBinder dataBinder;

    private AbstractSitemapController handler;

    private Map<String, Object> model;

    private Pipeline pipeline;

    private Sitemap processor;

    private HttpServletRequest request;

    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        this.componentFactory = mock(ComponentFactory.class);
        this.processor = mock(Sitemap.class);
        this.dataBinder = mock(DataBinder.class);
        this.handler = new TestAbstractSitemapController(this.componentFactory, this.dataBinder, this.processor, null);
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.pipeline = mock(Pipeline.class);
        this.model = new HashMap<>();
    }

    @Test
    public void testGetSitemapURI() {
        expect(this.request.getServletPath()).andReturn("/sitemapURI");
        replay(this.componentFactory, this.request);
        assertEquals("/sitemapURI", this.handler.getSitemapURI(this.request, ""));
        verify(this.componentFactory, this.request);
    }

    @Test
    public void testGetSitemapURIPrefix() {
        this.handler = new TestAbstractSitemapController(null, this.dataBinder, this.processor, null);
        expect(this.request.getServletPath()).andReturn("/prefix/sitemapURI");
        replay(this.componentFactory, this.request);
        assertEquals("/sitemapURI", this.handler.getSitemapURI(this.request, "/prefix"));
        verify(this.componentFactory, this.request);
    }

    @Test
    public void testHandleHEADRequest() throws Exception {
        expect(this.request.getMethod()).andReturn("HEAD");
        expect(this.request.getServletPath()).andReturn("/index.html");
        expect(this.componentFactory.getComponent("edu.stanford.irt.cocoon.Model", Map.class)).andReturn(this.model);
        this.dataBinder.bind(this.model, this.request);
        expect(this.processor.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        expect(this.pipeline.getMimeType()).andReturn("mime/type");
        this.response.setContentType("mime/type");
        replay(this.componentFactory, this.request, this.processor, this.pipeline, this.response, this.dataBinder);
        this.handler.handleRequest(this.request, this.response);
        verify(this.componentFactory, this.request, this.processor, this.pipeline, this.response, this.dataBinder);
    }

    @Test
    public void testHandleRequest() throws Exception {
        expect(this.request.getMethod()).andReturn("GET");
        expect(this.request.getServletPath()).andReturn("/index.html");
        expect(this.componentFactory.getComponent("edu.stanford.irt.cocoon.Model", Map.class)).andReturn(this.model);
        this.dataBinder.bind(this.model, this.request);
        expect(this.processor.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        expect(this.pipeline.getMimeType()).andReturn("mime/type");
        this.response.setContentType("mime/type");
        expect(this.response.getOutputStream()).andReturn(null);
        this.pipeline.process((OutputStream) null);
        replay(this.componentFactory, this.request, this.processor, this.pipeline, this.response, this.dataBinder);
        this.handler.handleRequest(this.request, this.response);
        verify(this.componentFactory, this.request, this.processor, this.pipeline, this.response, this.dataBinder);
    }
}
