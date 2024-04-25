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

import org.junit.Before;
import org.junit.Test;

public class FrameOptionsFilterTest {

    private FilterChain chain;

    private FrameOptionsFilter filter;

    private HttpServletRequest request;

    private HttpServletResponse response;

    @Before
    public void setUp() {
        this.filter = new FrameOptionsFilter();
        this.chain = mock(FilterChain.class);
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
    }

    @Test
    public void testInternalDoFilterNullReferrer() throws IOException, ServletException {
        expect(this.request.getHeader("referer")).andReturn(null);
        this.response.setHeader("X-Frame-Options", "SAMEORIGIN");
        this.chain.doFilter(this.request, this.response);
        replay(this.chain, this.request, this.response);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.chain, this.request, this.response);
    }

    @Test
    public void testInternalDoFilterReferrerNotStanford1() throws IOException, ServletException {
        expect(this.request.getHeader("referer")).andReturn("http://foo.com/foo");
        this.response.setHeader("X-Frame-Options", "SAMEORIGIN");
        this.chain.doFilter(this.request, this.response);
        replay(this.chain, this.request, this.response);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.chain, this.request, this.response);
    }

    @Test
    public void testInternalDoFilterReferrerNotStanford2() throws IOException, ServletException {
        expect(this.request.getHeader("referer"))
                .andReturn("http://weblisting.freetemplatespot.com/lane.stanford.edu/index.html");
        this.response.setHeader("X-Frame-Options", "SAMEORIGIN");
        this.chain.doFilter(this.request, this.response);
        replay(this.chain, this.request, this.response);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.chain, this.request, this.response);
    }

    @Test
    public void testInternalDoFilterReferrerStanford1() throws IOException, ServletException {
        expect(this.request.getHeader("referer")).andReturn("http://sfx.stanford.edu/foo");
        this.chain.doFilter(this.request, this.response);
        replay(this.chain, this.request, this.response);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.chain, this.request, this.response);
    }

    @Test
    public void testInternalDoFilterReferrerStanford2() throws IOException, ServletException {
        expect(this.request.getHeader("referer")).andReturn("https://foo-1234-bar.stanford.edu/foo");
        this.chain.doFilter(this.request, this.response);
        replay(this.chain, this.request, this.response);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.chain, this.request, this.response);
    }

    @Test
    public void testInternalDoFilterReferrerTelemtryTV() throws IOException, ServletException {
        expect(this.request.getHeader("referer")).andReturn("https://render.telemetrytv.com/foo");
        this.chain.doFilter(this.request, this.response);
        replay(this.chain, this.request, this.response);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.chain, this.request, this.response);
    }
}
