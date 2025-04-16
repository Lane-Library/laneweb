package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ErrordocsServletTest {

    private HttpServletRequest request;

    private HttpServletResponse response;

    private ErrordocsServlet servlet;

    @BeforeEach
    public void setUp() throws Exception {
        this.servlet = new ErrordocsServlet();
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
    }

    @Test
    public void testService503() throws IOException {
        expect(this.request.getRequestURI()).andReturn("/errordocs/503err.html");
        this.response.sendError(503);
        replay(this.request, this.response);
        this.servlet.service(this.request, this.response);
        verify(this.request, this.response);
    }

    @Test
    public void testServiceBadURL() throws IOException {
        expect(this.request.getRequestURI()).andReturn("/errordocs/foo.html");
        this.response.sendError(404);
        replay(this.request, this.response);
        this.servlet.service(this.request, this.response);
        verify(this.request, this.response);
    }
}
