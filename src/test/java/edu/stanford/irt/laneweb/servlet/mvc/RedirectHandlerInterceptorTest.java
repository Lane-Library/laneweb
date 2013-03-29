package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.redirect.RedirectProcessor;

public class RedirectHandlerInterceptorTest {

    private RedirectHandlerInterceptor interceptor;

    private RedirectProcessor redirectProcessor;

    private HttpServletRequest request;

    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        this.interceptor = new RedirectHandlerInterceptor();
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.redirectProcessor = createMock(RedirectProcessor.class);
        this.interceptor.setRedirectProcessor(this.redirectProcessor);
    }

    @Test
    public void testSlashClasses() throws Exception {
        expect(this.request.getRequestURI()).andReturn("/classes");
        expect(this.request.getAttribute(Model.BASE_PATH)).andReturn("");
        expect(this.request.getQueryString()).andReturn(null);
        expect(this.redirectProcessor.getRedirectURL("/classes", "", null)).andReturn("/classes/");
        this.response.sendRedirect("/classes/");
        replay(this.request, this.response, this.redirectProcessor);
        this.interceptor.preHandle(this.request, this.response, null);
        verify(this.request, this.response, this.redirectProcessor);
    }

    @Test
    public void testSlashClinician() throws Exception {
        expect(this.request.getRequestURI()).andReturn("/clinician");
        expect(this.request.getAttribute(Model.BASE_PATH)).andReturn("");
        expect(this.request.getQueryString()).andReturn(null);
        expect(this.redirectProcessor.getRedirectURL("/clinician", "", null)).andReturn("/clinician/");
        this.response.sendRedirect("/clinician/");
        replay(this.request, this.response, this.redirectProcessor);
        this.interceptor.preHandle(this.request, this.response, null);
        verify(this.request, this.response, this.redirectProcessor);
    }

    @Test
    public void testSlashLKSCPrint() throws Exception {
        expect(this.request.getRequestURI()).andReturn("/lksc-print.html");
        expect(this.request.getAttribute(Model.BASE_PATH)).andReturn("");
        expect(this.request.getQueryString()).andReturn(null);
        expect(this.redirectProcessor.getRedirectURL("/lksc-print.html", "", null)).andReturn("/help/lksc-print.html");
        this.response.sendRedirect("/help/lksc-print.html");
        replay(this.request, this.response, this.redirectProcessor);
        this.interceptor.preHandle(this.request, this.response, null);
        verify(this.request, this.response, this.redirectProcessor);
    }

    @Test
    public void testSlashM() throws Exception {
        expect(this.request.getRequestURI()).andReturn("/laneweb/m");
        expect(this.request.getAttribute(Model.BASE_PATH)).andReturn("/laneweb");
        expect(this.request.getQueryString()).andReturn(null);
        expect(this.redirectProcessor.getRedirectURL("/m", "/laneweb", null)).andReturn("/laneweb/m/");
        this.response.sendRedirect("/laneweb/m/");
        replay(this.request, this.response, this.redirectProcessor);
        this.interceptor.preHandle(this.request, this.response, null);
        verify(this.request, this.response, this.redirectProcessor);
    }
}
