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

public class ShowParameterFilterTest {

    private FilterChain chain;

    private ShowParameterFilter filter;

    private HttpServletRequest request;

    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        this.filter = new ShowParameterFilter();
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.chain = createMock(FilterChain.class);
    }

    @Test
    public void testInternalDoFilterNoShow() throws IOException, ServletException {
        expect(this.request.getParameter("show")).andReturn(null);
        this.chain.doFilter(this.request, this.response);
        replay(this.request, this.response, this.chain);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain);
    }

    @Test
    public void testInternalDoFilterShow() throws IOException, ServletException {
        expect(this.request.getParameter("show")).andReturn("show");
        this.response.sendError(403, "show parameter not accepted");
        replay(this.request, this.response, this.chain);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.request, this.response, this.chain);
    }
}
