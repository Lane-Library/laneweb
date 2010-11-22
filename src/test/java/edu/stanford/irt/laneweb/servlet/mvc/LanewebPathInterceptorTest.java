package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.Model;

public class LanewebPathInterceptorTest {

    private URL defaultContentBase;

    private URL resourcesBase;

    private LanewebPathInterceptor interceptor;

    private HttpServletRequest request;

    private URL stageBase;
    
    private URL ceyatesContent;

    @Before
    public void setUp() throws Exception {
        this.interceptor = new LanewebPathInterceptor();
        this.defaultContentBase = new URL("file:/afs/ir.stanford.edu/groups/lane/beta2/live");
        this.resourcesBase = new URL("file:/resources");
        this.stageBase = new URL("file:/afs/ir.stanford.edu/groups/lane/beta2/stage");
        this.ceyatesContent = new URL("file:/afs/ir.stanford.edu/users/c/e/ceyates/laneweb/content");
        this.interceptor.setDefaultContentBase(this.defaultContentBase);
        this.interceptor.setResourcesBase(this.resourcesBase);
        this.interceptor.setStageBase(this.stageBase);
        this.request = createMock(HttpServletRequest.class);
    }

    @Test
    public void testDefaults() throws Exception {
        expect(this.request.getRequestURI()).andReturn("/index.html");
        expect(this.request.getContextPath()).andReturn("");
        this.request.setAttribute(Model.BASE_PATH, "");
        this.request.setAttribute(Model.CONTENT_BASE, this.defaultContentBase);
        this.request.setAttribute(Model.RESOURCES_BASE, this.resourcesBase);
        replay(this.request);
        this.interceptor.preHandle(this.request, null, null);
        verify(this.request);
    }

    @Test
    public void testStage() throws Exception {
        expect(this.request.getRequestURI()).andReturn("/stage/index.html");
        expect(this.request.getContextPath()).andReturn("");
        this.request.setAttribute(Model.BASE_PATH, "/stage");
        this.request.setAttribute(Model.CONTENT_BASE, this.stageBase);
        this.request.setAttribute(Model.RESOURCES_BASE, this.resourcesBase);
        replay(this.request);
        this.interceptor.preHandle(this.request, null, null);
        verify(this.request);
    }

    @Test
    public void testContextPath() throws Exception {
        expect(this.request.getRequestURI()).andReturn("/laneweb/index.html");
        expect(this.request.getContextPath()).andReturn("/laneweb");
        this.request.setAttribute(Model.BASE_PATH, "/laneweb");
        this.request.setAttribute(Model.CONTENT_BASE, this.defaultContentBase);
        this.request.setAttribute(Model.RESOURCES_BASE, this.resourcesBase);
        replay(this.request);
        this.interceptor.preHandle(this.request, null, null);
        verify(this.request);
    }

    @Test
    public void testContextPathAndStage() throws Exception {
        expect(this.request.getRequestURI()).andReturn("/laneweb/stage/index.html");
        expect(this.request.getContextPath()).andReturn("/laneweb");
        this.request.setAttribute(Model.BASE_PATH, "/laneweb/stage");
        this.request.setAttribute(Model.CONTENT_BASE, this.stageBase);
        this.request.setAttribute(Model.RESOURCES_BASE, this.resourcesBase);
        replay(this.request);
        this.interceptor.preHandle(this.request, null, null);
        verify(this.request);
    }

    @Test
    public void testAFS() throws Exception {
        expect(this.request.getRequestURI()).andReturn("/ceyates/index.html");
        expect(this.request.getContextPath()).andReturn("");
        this.request.setAttribute(Model.BASE_PATH, "/ceyates");
        this.request.setAttribute(Model.CONTENT_BASE, this.ceyatesContent);
        this.request.setAttribute(Model.RESOURCES_BASE, this.resourcesBase);
        replay(this.request);
        this.interceptor.preHandle(this.request, null, null);
        verify(this.request);
    }

    @Test
    public void testContextPathAndAFS() throws Exception {
        expect(this.request.getRequestURI()).andReturn("/laneweb/ceyates/index.html");
        expect(this.request.getContextPath()).andReturn("/laneweb");
        this.request.setAttribute(Model.BASE_PATH, "/laneweb/ceyates");
        this.request.setAttribute(Model.CONTENT_BASE, this.ceyatesContent);
        this.request.setAttribute(Model.RESOURCES_BASE, this.resourcesBase);
        replay(this.request);
        this.interceptor.preHandle(this.request, null, null);
        verify(this.request);
    }
}
