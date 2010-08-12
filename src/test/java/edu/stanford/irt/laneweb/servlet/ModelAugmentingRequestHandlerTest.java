package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.Collections;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.cocoon.Processor;
import org.apache.cocoon.environment.Environment;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.IPGroup;
import edu.stanford.irt.laneweb.model.Model;

public class ModelAugmentingRequestHandlerTest {

    private ModelAugmentingRequestHandler handler;

    private Processor processor;

    private ProxyLinks proxyLinks;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private ServletContext servletContext;

    private HttpSession session;

    private TemplateChooser templateChooser;

    @Before
    public void setUp() throws Exception {
        this.session = createMock(HttpSession.class);
        this.handler = new ModelAugmentingRequestHandler();
        this.request = createMock(HttpServletRequest.class);
        this.proxyLinks = createMock(ProxyLinks.class);
        this.response = createMock(HttpServletResponse.class);
        this.templateChooser = createMock(TemplateChooser.class);
        this.processor = createMock(Processor.class);
        this.servletContext = createMock(ServletContext.class);
        this.handler.setProxyLinks(this.proxyLinks);
        this.handler.setTemplateChooser(this.templateChooser);
        this.handler.setProcessor(this.processor);
        this.handler.setServletContext(this.servletContext);
    }

    @Test
    public void testProcess() throws Exception {
        expect(this.request.getAttribute(Model.MODEL)).andReturn(new HashMap<String, Object>());
        expect(this.request.getParameter(isA(String.class))).andReturn(null).atLeastOnce();
        expect(this.request.getRemoteUser()).andReturn(null);
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.request.getHeader(isA(String.class))).andReturn(null).atLeastOnce();
        expect(this.request.getCookies()).andReturn(null).atLeastOnce();
        expect(this.request.getRemoteAddr()).andReturn("127.0.0.1");
        expect(this.request.getParameterNames()).andReturn(Collections.enumeration(Collections.emptyList()))
                .atLeastOnce();
        expect(this.request.getQueryString()).andReturn(null);
        expect(this.request.getContextPath()).andReturn("/").atLeastOnce();
        expect(this.request.getRequestURI()).andReturn("/index.html").atLeastOnce();
        expect(this.session.getAttribute(isA(String.class))).andReturn(null).atLeastOnce();
        this.session.setAttribute(isA(String.class), isA(Object.class));
        expectLastCall().atLeastOnce();
        expect(this.proxyLinks.getProxyLinks(this.request, this.session, IPGroup.OTHER, "127.0.0.1")).andReturn(
                Boolean.FALSE);
        expect(this.templateChooser.getTemplate(this.request)).andReturn("foo");
        expect(this.processor.process(isA(Environment.class))).andReturn(Boolean.TRUE);
        replayMocks();
        this.handler.process(this.request, this.response);
        verifyMocks();
    }

    private void replayMocks() {
        replay(this.session);
        replay(this.servletContext);
        replay(this.templateChooser);
        replay(this.response);
        replay(this.request);
        replay(this.processor);
        replay(this.proxyLinks);
    }

    private void verifyMocks() {
        verify(this.session);
        verify(this.servletContext);
        verify(this.processor);
        verify(this.templateChooser);
        verify(this.response);
        verify(this.request);
        verify(this.proxyLinks);
    }
}
