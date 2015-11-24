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

public class FrameOptionsFilterTest {

    private FilterChain chain;

    private FrameOptionsFilter filter;

    private HttpServletRequest request;

    private HttpServletResponse response;

    @Before
    public void setUp() {
        this.filter = new FrameOptionsFilter();
        this.chain = createMock(FilterChain.class);
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
    }

    @Test
    public void testInternalDoFilterNullReferrer() throws IOException, ServletException {
        expect(this.request.getHeader("referer")).andReturn(null);
        this.chain.doFilter(this.request, this.response);
        replay(this.chain, this.request, this.response);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.chain, this.request, this.response);
    }

    @Test
    public void testInternalDoFilterReferrerNotStanford() throws IOException, ServletException {
        expect(this.request.getHeader("referer")).andReturn("http://foo.com/foo");
        this.response.setHeader("X-Frame-Options", "SAMEORIGIN");
        this.chain.doFilter(this.request, this.response);
        replay(this.chain, this.request, this.response);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.chain, this.request, this.response);
    }

    @Test
    public void testInternalDoFilterReferrerStanford() throws IOException, ServletException {
        expect(this.request.getHeader("referer")).andReturn("http://sfx.stanford.edu/foo");
        this.chain.doFilter(this.request, this.response);
        replay(this.chain, this.request, this.response);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.chain, this.request, this.response);
    }
}
