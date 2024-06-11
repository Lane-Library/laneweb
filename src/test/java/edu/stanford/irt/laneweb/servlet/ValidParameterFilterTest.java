package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.util.Collections;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;

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
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.chain = mock(FilterChain.class);
    }

    @Test
    public void testInternalDoFilterEmptyParameterMap() throws IOException, ServletException {
        expect(this.request.getParameterMap()).andReturn(Collections.emptyMap());
        this.chain.doFilter(this.request, this.response);
        replay(this.request, this.response, this.chain);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain);
    }

    @Test
    public void testInternalDoFilterInvalidParameter() throws IOException, ServletException {
        expect(this.request.getParameterMap()).andReturn(Collections.singletonMap("invalid", new String[] { "value" }));
        this.response.setStatus(400);
        this.chain.doFilter(isA(HttpServletRequestWrapper.class), eq(this.response));
        replay(this.request, this.response, this.chain);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain);
    }

    @Test
    public void testInternalDoFilterInvalidParameterLength() throws IOException, ServletException {
        String longString = "long query".repeat(50);
        expect(this.request.getParameterMap()).andReturn(Collections.singletonMap("q", new String[] { longString }));
        this.response.setStatus(400);
        this.chain.doFilter(isA(HttpServletRequestWrapper.class), eq(this.response));
        replay(this.request, this.response, this.chain);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain);
    }

    @Test
    public void testInternalDoFilterNullParameterMap() throws IOException, ServletException {
        expect(this.request.getParameterMap()).andReturn(null);
        this.chain.doFilter(this.request, this.response);
        replay(this.request, this.response, this.chain);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain);
    }

    @Test
    public void testInternalDoFilterValidParameter() throws IOException, ServletException {
        expect(this.request.getParameterMap()).andReturn(Collections.singletonMap("page", new String[] { "1" }));
        this.chain.doFilter(this.request, this.response);
        replay(this.request, this.response, this.chain);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain);
    }

    @Test
    public void testInternalDoFilterValidParameterLength() throws IOException, ServletException {
        String query = "short query";
        expect(this.request.getParameterMap()).andReturn(Collections.singletonMap("q", new String[] { query }));
        this.chain.doFilter(this.request, this.response);
        replay(this.request, this.response, this.chain);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain);
    }
}
