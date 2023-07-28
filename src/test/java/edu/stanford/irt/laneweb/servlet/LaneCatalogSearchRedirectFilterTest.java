package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.util.Enumeration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

public class LaneCatalogSearchRedirectFilterTest {

    private FilterChain chain;

    private LaneCatalogSearchRedirectFilter filter;

    private Enumeration<String> names;

    private HttpServletRequest request;

    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        this.filter = new LaneCatalogSearchRedirectFilter();
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.chain = mock(FilterChain.class);
        this.names = mock(Enumeration.class);
    }

    @Test
    public void testInternalDoFilterNoSource() throws IOException, ServletException {
        expect(this.request.getParameterNames()).andReturn(this.names);
        expect(this.names.hasMoreElements()).andReturn(true);
        expect(this.names.nextElement()).andReturn("nope");
        expect(this.names.hasMoreElements()).andReturn(false);
        this.chain.doFilter(this.request, this.response);
        replay(this.chain, this.request, this.response, this.names);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.chain, this.request, this.response, this.names);
    }

    @Test
    public void testInternalDoFilterSource() throws IOException, ServletException {
        expect(this.request.getParameterNames()).andReturn(this.names);
        expect(this.names.hasMoreElements()).andReturn(true);
        expect(this.names.nextElement()).andReturn("source");
        expect(this.request.getParameter("source")).andReturn("catalog-all");
        expect(this.names.hasMoreElements()).andReturn(false);
        StringBuffer sb = new StringBuffer("url");
        expect(this.request.getRequestURL()).andReturn(sb);
        expect(this.request.getQueryString()).andReturn("queryString");
        this.response.setStatus(301);
        this.response.addHeader("Location", "url?queryString&facets=recordType%3A%22bib%22");
        replay(this.chain, this.request, this.response, this.names);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.chain, this.request, this.response, this.names);
    }

    @Test
    public void testInternalDoFilterSourceAndFacets() throws IOException, ServletException {
        expect(this.request.getParameterNames()).andReturn(this.names);
        expect(this.names.hasMoreElements()).andReturn(true);
        expect(this.names.nextElement()).andReturn("source");
        expect(this.request.getParameter("source")).andReturn("catalog-all");
        expect(this.names.hasMoreElements()).andReturn(true);
        expect(this.names.nextElement()).andReturn("facets");
        expect(this.request.getParameter("facets")).andReturn("type:Book");
        expect(this.names.hasMoreElements()).andReturn(false);
        StringBuffer sb = new StringBuffer("url");
        expect(this.request.getRequestURL()).andReturn(sb);
        expect(this.request.getQueryString()).andReturn("q=foo&source=catalog-all&facets=type:Book");
        this.response.setStatus(301);
        this.response.addHeader("Location",
                "url?q=foo&source=catalog-all&facets=type:Book%3A%3ArecordType%3A%22bib%22");
        replay(this.chain, this.request, this.response, this.names);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.chain, this.request, this.response, this.names);
    }

    @Test
    public void testInternalDoFilterSourceAndLaneCatalogFacet() throws IOException, ServletException {
        expect(this.request.getParameterNames()).andReturn(this.names);
        expect(this.names.hasMoreElements()).andReturn(true);
        expect(this.names.nextElement()).andReturn("source");
        expect(this.request.getParameter("source")).andReturn("catalog-all");
        expect(this.names.hasMoreElements()).andReturn(true);
        expect(this.names.nextElement()).andReturn("facets");
        expect(this.request.getParameter("facets")).andReturn("recordType:\"bib\"");
        expect(this.names.hasMoreElements()).andReturn(false);
        this.chain.doFilter(this.request, this.response);
        replay(this.chain, this.request, this.response, this.names);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.chain, this.request, this.response, this.names);
    }
}
