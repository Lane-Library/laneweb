package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

public class LanewebHTTPSFilterTest {

    private FilterChain chain;

    private LanewebHTTPSFilter filter;

    private HttpServletRequest request;

    private HttpServletResponse response;

    @Before
    public void setUp() {
        this.filter = new LanewebHTTPSFilter();
        this.chain = createMock(FilterChain.class);
        this.response = createMock(HttpServletResponse.class);
        this.request = createMock(HttpServletRequest.class);
    }

    @Test
    public void testInternalDoFilter() throws IOException, ServletException {
        expect(this.request.getQueryString()).andReturn(null);
        expect(this.request.getRequestURL()).andReturn(new StringBuffer("https://lane.stanford.edu/secure/index.html"));
        expect(this.request.getHeader("x-forwarded-proto")).andReturn(null);
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
        expect(this.request.getHeader("x-forwarded-proto")).andReturn("https");
        this.chain.doFilter(this.request, this.response);
        replay(this.chain, this.response, this.request);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.chain, this.response, this.request);
    }

    @Test
    public void testInternalDoFilterHttp() throws IOException, ServletException {
        expect(this.request.getQueryString()).andReturn(null);
        expect(this.request.getRequestURL()).andReturn(new StringBuffer("http://lane.stanford.edu/secure/index.html"));
        expect(this.request.getHeader("x-forwarded-proto")).andReturn(null);
        expect(this.request.getScheme()).andReturn("http");
        this.response.sendRedirect("https://lane.stanford.edu/secure/index.html");
        replay(this.chain, this.response, this.request);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.chain, this.response, this.request);
    }

    @Test
    public void testInternalDoFilterHttpQuery() throws IOException, ServletException {
        expect(this.request.getQueryString()).andReturn("query");
        expect(this.request.getRequestURL()).andReturn(new StringBuffer("http://lane.stanford.edu/secure/index.html"));
        expect(this.request.getHeader("x-forwarded-proto")).andReturn(null);
        expect(this.request.getScheme()).andReturn("http");
        this.response.sendRedirect("https://lane.stanford.edu/secure/index.html?query");
        replay(this.chain, this.response, this.request);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.chain, this.response, this.request);
    }
}
