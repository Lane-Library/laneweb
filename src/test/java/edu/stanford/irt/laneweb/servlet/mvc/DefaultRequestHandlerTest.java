package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.LanewebException;

public class DefaultRequestHandlerTest {

    private DefaultRequestHandler handler;

    private HttpServletRequest request;

    private HttpServletResponse response;

    @BeforeEach
    public void setUp() throws Exception {
        this.handler = new DefaultRequestHandler();
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
    }

    @Test
    public void testHandleRequest() throws IOException {
        expect(this.request.getRequestURI()).andReturn("/foo");
        expect(this.request.getQueryString()).andReturn(null);
        this.response.sendRedirect("/foo/index.html");
        replay(this.request, this.response);
        this.handler.handleRequest(this.request, this.response);
        verify(this.request, this.response);
    }

    @Test
    public void testHandleRequestQueryString() throws IOException {
        expect(this.request.getRequestURI()).andReturn("/foo");
        expect(this.request.getQueryString()).andReturn("bar=baz");
        this.response.sendRedirect("/foo/index.html?bar=baz");
        replay(this.request, this.response);
        this.handler.handleRequest(this.request, this.response);
        verify(this.request, this.response);
    }

    @Test
    public void testHandleRequestThrowIOException() throws IOException {
        expect(this.request.getRequestURI()).andReturn("/foo");
        expect(this.request.getQueryString()).andReturn("bar=baz");
        this.response.sendRedirect("/foo/index.html?bar=baz");
        expectLastCall().andThrow(new IOException());
        replay(this.request, this.response);
        assertThrows(IOException.class, () -> {
            this.handler.handleRequest(this.request, this.response);
        });
        verify(this.request, this.response);
    }
}
