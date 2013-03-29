package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;
import org.springframework.web.servlet.HandlerMapping;

import edu.stanford.irt.laneweb.model.Model;

public class LanewebPathInterceptorTest {

    private URL defaultContentBase;

    private LanewebPathInterceptor interceptor;

    private HttpServletRequest request;

    private URL stageBase;

    @Before
    public void setUp() throws Exception {
        this.interceptor = new LanewebPathInterceptor();
        this.defaultContentBase = new URL("file:/afs/ir.stanford.edu/groups/lane/beta2/live");
        this.stageBase = new URL("file:/afs/ir.stanford.edu/groups/lane/beta2/stage");
        this.interceptor.setDefaultContentBase(this.defaultContentBase);
        this.interceptor.setStageBase(this.stageBase);
        this.request = createMock(HttpServletRequest.class);
    }

    @Test
    public void testContextPath() throws Exception {
        expect(this.request.getRequestURI()).andReturn("/laneweb/index.html");
        expect(this.request.getContextPath()).andReturn("/laneweb");
        this.request.setAttribute(Model.BASE_PATH, "/laneweb");
        this.request.setAttribute(Model.CONTENT_BASE, this.defaultContentBase);
        replay(this.request);
        this.interceptor.preHandle(this.request, null, null);
        verify(this.request);
    }

    @Test
    public void testContextPathAndStage() throws Exception {
        expect(this.request.getRequestURI()).andReturn("/laneweb/stage/index.html");
        expect(this.request.getContextPath()).andReturn("/laneweb");
        this.request.setAttribute(Model.BASE_PATH, "/laneweb/stage");
        expect(this.request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).andReturn("/stage/index.html");
        this.request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, "/index.html");
        this.request.setAttribute(Model.CONTENT_BASE, this.stageBase);
        replay(this.request);
        this.interceptor.preHandle(this.request, null, null);
        verify(this.request);
    }

    @Test
    public void testDefaults() throws Exception {
        expect(this.request.getRequestURI()).andReturn("/index.html");
        expect(this.request.getContextPath()).andReturn("");
        this.request.setAttribute(Model.BASE_PATH, "");
        this.request.setAttribute(Model.CONTENT_BASE, this.defaultContentBase);
        replay(this.request);
        this.interceptor.preHandle(this.request, null, null);
        verify(this.request);
    }

    @Test
    public void testStage() throws Exception {
        expect(this.request.getRequestURI()).andReturn("/stage/index.html");
        expect(this.request.getContextPath()).andReturn("");
        this.request.setAttribute(Model.BASE_PATH, "/stage");
        expect(this.request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).andReturn("/stage/index.html");
        this.request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, "/index.html");
        this.request.setAttribute(Model.CONTENT_BASE, this.stageBase);
        replay(this.request);
        this.interceptor.preHandle(this.request, null, null);
        verify(this.request);
    }
}
