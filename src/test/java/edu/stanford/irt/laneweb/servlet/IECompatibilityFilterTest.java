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

public class IECompatibilityFilterTest {

    private FilterChain chain;

    private IECompatibilityFilter filter;

    private HttpServletRequest request;

    private HttpServletResponse response;

    @Before
    public void setUp() {
        this.filter = new IECompatibilityFilter();
        this.chain = createMock(FilterChain.class);
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
    }

    @Test
    public void testInternalDoFilterAgentMSIE() throws IOException, ServletException {
        expect(this.request.getHeader("user-agent")).andReturn("this one is MSIE ");
        this.response.setHeader("X-UA-Compatible", "IE=edge");
        this.chain.doFilter(this.request, this.response);
        replay(this.chain, this.request, this.response);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.chain, this.request, this.response);
    }

    @Test
    public void testInternalDoFilterAgentNotMSIE() throws IOException, ServletException {
        expect(this.request.getHeader("user-agent")).andReturn("something else");
        this.chain.doFilter(this.request, this.response);
        replay(this.chain, this.request, this.response);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.chain, this.request, this.response);
    }

    @Test
    public void testInternalDoFilterNullAgent() throws IOException, ServletException {
        expect(this.request.getHeader("user-agent")).andReturn(null);
        this.chain.doFilter(this.request, this.response);
        replay(this.chain, this.request, this.response);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.chain, this.request, this.response);
    }
}
