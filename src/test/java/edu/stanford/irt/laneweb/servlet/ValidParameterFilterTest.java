package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.util.Collections;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

public class ValidParameterFilterTest {

    private FilterChain chain;

    private ValidParameterFilter filter;

    private HttpServletRequest request;

    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        this.filter = new ValidParameterFilter();
        this.filter.init(null);
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.chain = createMock(FilterChain.class);
    }

    @Test
    public void testInternalDoFilterEmptyParameterNames() throws IOException, ServletException {
        expect(this.request.getParameterNames()).andReturn(Collections.emptyEnumeration());
        this.chain.doFilter(this.request, this.response);
        replay(this.request, this.response, this.chain);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain);
    }

    @Test
    public void testInternalDoFilterInvalidParameter() throws IOException, ServletException {
        expect(this.request.getParameterNames()).andReturn(Collections.enumeration(Collections.singleton("invalid")));
        this.response.setStatus(400);
        expect(this.request.getServletPath()).andReturn("servletPath");
        expect(this.request.getQueryString()).andReturn("queryString");
        this.chain.doFilter(isA(HttpServletRequestWrapper.class), eq(this.response));
        replay(this.request, this.response, this.chain);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain);
    }

    @Test
    public void testInternalDoFilterNullParameterNames() throws IOException, ServletException {
        expect(this.request.getParameterNames()).andReturn(null);
        this.chain.doFilter(this.request, this.response);
        replay(this.request, this.response, this.chain);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain);
    }

    @Test
    public void testInternalDoFilterValidParameter() throws IOException, ServletException {
        expect(this.request.getParameterNames()).andReturn(Collections.enumeration(Collections.singleton("page")));
        this.chain.doFilter(this.request, this.response);
        replay(this.request, this.response, this.chain);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain);
    }
}
