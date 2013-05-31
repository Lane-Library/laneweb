package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
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
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

public class SitemapRequestHandlerTest {

    private static final class TestHandler extends SitemapRequestHandler {

        public TestHandler(ComponentFactory componentFactory, SourceResolver sourceResolver) {
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

    private HttpServletRequest request;

    private HttpServletResponse response;

    private ServletContext servletContext;

    private Sitemap processor;

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

    @SuppressWarnings("unchecked")
    @Test
    public void testHandleRequest() throws Exception {
        expect(this.request.getMethod()).andReturn("GET");
        expect(this.request.getAttribute(Model.BASE_PATH)).andReturn("");
        expect(this.request.getRequestURI()).andReturn("/index.html");
        expect(this.servletContext.getMimeType("/index.html")).andReturn("text/html");
        expect(this.response.getOutputStream()).andReturn(null);
        this.response.setContentType("text/html");
        expect(this.processor.buildPipeline(isA(SitemapContext.class))).andReturn(this.pipeline);
        this.pipeline.process((OutputStream) null);
        this.dataBinder.bind(isA(Map.class), isA(HttpServletRequest.class));
        replay(this.servletContext, this.response, this.request, this.processor, this.pipeline);
        this.handler.handleRequest(this.request, this.response);
        verify(this.servletContext, this.processor, this.response, this.request, this.pipeline);
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
