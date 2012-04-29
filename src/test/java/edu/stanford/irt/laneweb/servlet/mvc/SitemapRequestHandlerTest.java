package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.Processor;
import org.apache.cocoon.environment.Environment;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.cocoon.LanewebEnvironment;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

public class SitemapRequestHandlerTest {

    private static final class TestHandler extends SitemapRequestHandler {

        @Override
        protected LanewebEnvironment getEnvironment() {
            return environment;
        }
    }

    private static LanewebEnvironment environment;

    private DataBinder dataBinder;

    private SitemapRequestHandler handler;

    private Processor processor;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private ServletContext servletContext;

    @Before
    public void setUp() throws Exception {
        this.handler = new TestHandler();
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.processor = createMock(Processor.class);
        this.servletContext = createMock(ServletContext.class);
        this.dataBinder = createMock(DataBinder.class);
        environment = createMock(LanewebEnvironment.class);
        this.handler.setProcessor(this.processor);
        this.handler.setServletContext(this.servletContext);
        this.handler.setDataBinder(this.dataBinder);
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
        expect(this.processor.process(isA(Environment.class))).andReturn(Boolean.TRUE);
        environment.setModel(isA(Map.class));
        environment.setOutputStream(null);
        environment.setIsExternal(true);
        this.dataBinder.bind(isA(Map.class), isA(HttpServletRequest.class));
        replay(this.servletContext, this.response, this.request, this.processor, environment);
        this.handler.handleRequest(this.request, this.response);
        verify(this.servletContext, this.processor, this.response, this.request, environment);
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
