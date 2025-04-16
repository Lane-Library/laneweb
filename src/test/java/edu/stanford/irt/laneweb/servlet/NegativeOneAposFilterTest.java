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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class NegativeOneAposFilterTest {

    private FilterChain chain;

    private NegativeOneAposFilter filter;

    private Enumeration<String> names;

    private HttpServletRequest request;

    private HttpServletResponse response;

    @BeforeEach
    public void setUp() throws Exception {
        this.filter = new NegativeOneAposFilter();
        this.chain = mock(FilterChain.class);
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
        this.names = mock(Enumeration.class);
    }

    @Test
    public void testInternalDoFilterBad() throws IOException, ServletException {
        expect(this.request.getParameterNames()).andReturn(this.names);
        expect(this.names.hasMoreElements()).andReturn(true);
        expect(this.names.nextElement()).andReturn("name");
        expect(this.request.getParameter("name")).andReturn("-1'");
        this.response.sendError(403, "-1' parameter value not accepted");
        replay(this.chain, this.request, this.response, this.names);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.chain, this.request, this.response, this.names);
    }

    @Test
    public void testInternalDoFilterGood() throws IOException, ServletException {
        expect(this.request.getParameterNames()).andReturn(this.names);
        expect(this.names.hasMoreElements()).andReturn(true);
        expect(this.names.nextElement()).andReturn("name");
        expect(this.request.getParameter("name")).andReturn("value");
        expect(this.names.hasMoreElements()).andReturn(false);
        this.chain.doFilter(this.request, this.response);
        replay(this.chain, this.request, this.response, this.names);
        this.filter.internalDoFilter(this.request, this.response, this.chain);
        verify(this.chain, this.request, this.response, this.names);
    }
}
