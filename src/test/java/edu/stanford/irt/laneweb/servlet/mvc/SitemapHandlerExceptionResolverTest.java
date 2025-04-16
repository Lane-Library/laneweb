package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.FileNotFoundException;
import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.ClientAbortException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.ResourceNotFoundException;

public class SitemapHandlerExceptionResolverTest {

    private SitemapController controller;

    private HttpServletRequest request;

    private SitemapHandlerExceptionResolver resolver;

    private HttpServletResponse response;

    @BeforeEach
    public void setUp() {
        this.controller = mock(SitemapController.class);
        this.resolver = new SitemapHandlerExceptionResolver(this.controller);
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
    }

    @Test
    public void testResolveClientAbortException() {
        Exception nested = new Exception();
        Exception ex = new ClientAbortException(nested);
        expect(this.response.isCommitted()).andReturn(true);
        replay(this.request, this.response);
        this.resolver.resolveException(this.request, this.response, null, ex);
        verify(this.request, this.response);
    }

    @Test
    public void testResolveException() {
        Exception ex = new Exception();
        expect(this.response.isCommitted()).andReturn(true);
        replay(this.request, this.response);
        this.resolver.resolveException(this.request, this.response, null, ex);
        verify(this.request, this.response);
    }

    @Test
    public void testResolveExceptionNotCommitted() throws IOException {
        Exception ex = new Exception();
        expect(this.response.isCommitted()).andReturn(false);
        this.response.setStatus(404);
        this.controller.handleRequest(isA(HttpServletRequest.class), isA(HttpServletResponse.class));
        replay(this.controller, this.request, this.response);
        this.resolver.resolveException(this.request, this.response, null, ex);
        verify(this.controller, this.request, this.response);
    }

    @Test
    public void testResolveExceptionThrowIOException() throws IOException {
        Exception ex = new Exception();
        expect(this.response.isCommitted()).andReturn(false);
        this.response.setStatus(404);
        IOException ioe = new IOException();
        this.controller.handleRequest(isA(HttpServletRequest.class), isA(HttpServletResponse.class));
        expectLastCall().andThrow(ioe);
        replay(this.controller, this.request, this.response);
        this.resolver.resolveException(this.request, this.response, null, ex);
        verify(this.controller, this.request, this.response);
    }

    @Test
    public void testResolveFileNotFoundException() {
        Exception nested = new FileNotFoundException();
        Exception ex = new Exception(nested);
        expect(this.response.isCommitted()).andReturn(true);
        replay(this.request, this.response);
        this.resolver.resolveException(this.request, this.response, null, ex);
        verify(this.request, this.response);
    }

    @Test
    public void testResolveNestedException() {
        Exception nested = new Exception();
        Exception ex = new Exception(nested);
        expect(this.response.isCommitted()).andReturn(true);
        replay(this.request, this.response);
        this.resolver.resolveException(this.request, this.response, null, ex);
        verify(this.request, this.response);
    }

    @Test
    public void testResolveResourceNotFoundException() {
        Exception ex = new ResourceNotFoundException("notfound");
        expect(this.response.isCommitted()).andReturn(true);
        replay(this.request, this.response);
        this.resolver.resolveException(this.request, this.response, null, ex);
        verify(this.request, this.response);
    }
}
