package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.Collections;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.cocoon.Processor;
import org.apache.cocoon.environment.Environment;
import org.junit.Before;
import org.junit.Test;

public class ModelAugmentingRequestHandlerTest {

    private ModelAugmentingRequestHandler handler;

    private Processor processor;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private ServletContext servletContext;

    @Before
    public void setUp() throws Exception {
        this.handler = new ModelAugmentingRequestHandler();
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.processor = createMock(Processor.class);
        this.servletContext = createMock(ServletContext.class);
        this.handler.setProcessor(this.processor);
        this.handler.setServletContext(this.servletContext);
    }

    @Test
    public void testProcess() throws Exception {
        expect(this.request.getParameter(isA(String.class))).andReturn(null).atLeastOnce();
        expect(this.request.getParameterNames()).andReturn(Collections.enumeration(Collections.emptyList()))
                .atLeastOnce();
        expect(this.request.getContextPath()).andReturn("/").atLeastOnce();
        expect(this.request.getRequestURI()).andReturn("/index.html").atLeastOnce();
        expect(this.processor.process(isA(Environment.class))).andReturn(Boolean.TRUE);
        this.request.setAttribute("model", null);
        replay(this.servletContext, this.response, this.request, this.processor);
        this.handler.process(null, this.request, this.response);
        verify(this.servletContext, this.processor, this.response, this.request);
    }
}
