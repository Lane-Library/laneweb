package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LanewebHTTPSFilterTest {

    private FilterChain chain;

    private LanewebHTTPSFilter filter;

    private HttpServletRequest request;

    private HttpServletResponse response;

    @BeforeEach
    public void setUp() {
        this.filter = new LanewebHTTPSFilter();
        this.chain = mock(FilterChain.class);
        this.response = mock(HttpServletResponse.class);
        this.request = mock(HttpServletRequest.class);
    }

    @Test
    public void testInternalDoFilter() throws IOException, ServletException {
        expect(this.request.getQueryString()).andReturn(null);
        expect(this.request.getRequestURL()).andReturn(new StringBuffer("https://lane.stanford.edu/secure/index.html"));
        expect(this.request.getScheme()).andReturn("https");
        this.chain.doFilter(this.request, this.response);
        replay(this.chain, this.response, this.request);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.chain, this.response, this.request);
    }

    @Test
    public void testInternalDoFilterGoHttps() throws IOException, ServletException {
        expect(this.request.getQueryString()).andReturn(null);
        expect(this.request.getRequestURL()).andReturn(new StringBuffer("https://lane.stanford.edu/secure/index.html"));
        expect(this.request.getScheme()).andReturn("http");
        expect(this.request.getHeader("gohttps")).andReturn("1");
        this.chain.doFilter(this.request, this.response);
        replay(this.chain, this.response, this.request);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.chain, this.response, this.request);
    }

    @Test
    public void testInternalDoFilterHttp() throws IOException, ServletException {
        expect(this.request.getQueryString()).andReturn(null);
        expect(this.request.getRequestURL()).andReturn(new StringBuffer("http://lane.stanford.edu/secure/index.html"));
        expect(this.request.getScheme()).andReturn("http");
        expect(this.request.getHeader("gohttps")).andReturn(null);
        expect(this.request.getHeader("x-forwarded-proto")).andReturn(null);
        this.response.sendRedirect("https://lane.stanford.edu/secure/index.html");
        replay(this.chain, this.response, this.request);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.chain, this.response, this.request);
    }

    @Test
    public void testInternalDoFilterHttpQuery() throws IOException, ServletException {
        expect(this.request.getQueryString()).andReturn("query");
        expect(this.request.getRequestURL()).andReturn(new StringBuffer("http://lane.stanford.edu/secure/index.html"));
        expect(this.request.getScheme()).andReturn("http");
        expect(this.request.getHeader("gohttps")).andReturn(null);
        expect(this.request.getHeader("x-forwarded-proto")).andReturn(null);
        this.response.sendRedirect("https://lane.stanford.edu/secure/index.html?query");
        replay(this.chain, this.response, this.request);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.chain, this.response, this.request);
    }

    @Test
    public void testInternalDoFilterXForwardedProto() throws IOException, ServletException {
        expect(this.request.getQueryString()).andReturn(null);
        expect(this.request.getRequestURL()).andReturn(new StringBuffer("https://lane.stanford.edu/secure/index.html"));
        expect(this.request.getScheme()).andReturn("http");
        expect(this.request.getHeader("gohttps")).andReturn(null);
        expect(this.request.getHeader("x-forwarded-proto")).andReturn("https");
        this.chain.doFilter(this.request, this.response);
        replay(this.chain, this.response, this.request);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.chain, this.response, this.request);
    }
}
