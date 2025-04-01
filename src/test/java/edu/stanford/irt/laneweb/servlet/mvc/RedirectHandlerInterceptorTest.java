package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.servlet.redirect.RedirectProcessor;

public class RedirectHandlerInterceptorTest {

    private RedirectHandlerInterceptor interceptor;

    private RedirectProcessor redirectProcessor;

    private HttpServletRequest request;

    private HttpServletResponse response;

    @BeforeEach
    public void setUp() {
        this.redirectProcessor = mock(RedirectProcessor.class);
        this.interceptor = new RedirectHandlerInterceptor(this.redirectProcessor);
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
    }

    @Test
    public void testNoRedirect() throws IOException {
        expect(this.request.getRequestURI()).andReturn("index.html");
        expect(this.request.getContextPath()).andReturn("");
        expect(this.request.getQueryString()).andReturn(null);
        expect(this.redirectProcessor.getRedirectURL("index.html", "", null)).andReturn(null);
        replay(this.request, this.response, this.redirectProcessor);
        assertTrue(this.interceptor.preHandle(this.request, this.response, null));
        verify(this.request, this.response, this.redirectProcessor);
    }

    @Test
    public void testSlashClinician() throws IOException {
        expect(this.request.getRequestURI()).andReturn("/clinician");
        expect(this.request.getContextPath()).andReturn("");
        expect(this.request.getQueryString()).andReturn(null);
        expect(this.redirectProcessor.getRedirectURL("/clinician", "", null)).andReturn("/clinician/");
        this.response.sendRedirect("/clinician/");
        replay(this.request, this.response, this.redirectProcessor);
        assertFalse(this.interceptor.preHandle(this.request, this.response, null));
        verify(this.request, this.response, this.redirectProcessor);
    }

    @Test
    public void testSlashLKSCPrint() throws IOException {
        expect(this.request.getRequestURI()).andReturn("/lksc-print.html");
        expect(this.request.getContextPath()).andReturn("");
        expect(this.request.getQueryString()).andReturn(null);
        expect(this.redirectProcessor.getRedirectURL("/lksc-print.html", "", null)).andReturn("/help/lksc-print.html");
        this.response.sendRedirect("/help/lksc-print.html");
        replay(this.request, this.response, this.redirectProcessor);
        assertFalse(this.interceptor.preHandle(this.request, this.response, null));
        verify(this.request, this.response, this.redirectProcessor);
    }

}
