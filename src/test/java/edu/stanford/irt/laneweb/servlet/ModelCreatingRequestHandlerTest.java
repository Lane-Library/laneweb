package edu.stanford.irt.laneweb.servlet;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.cocoon.Processor;
import org.junit.Before;
import org.junit.Test;

public class ModelCreatingRequestHandlerTest {

    private ModelCreatingRequestHandler handler;
    
    private ProxyLinks proxyLinks;
    
    private TemplateChooser templateChooser;
    
    private HttpServletRequest request;

    private HttpServletResponse response;
    
    private Processor processor;
    
    private ServletContext servletContext;
    
    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        this.session = createMock(HttpSession.class);
        this.handler = new ModelCreatingRequestHandler();
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
    public void testProcess() throws IOException {
//        expect(this.request.getSession()).andReturn(this.session);
//        replayMocks();
//        this.handler.process(this.request, this.response);
//        verifyMocks();
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
