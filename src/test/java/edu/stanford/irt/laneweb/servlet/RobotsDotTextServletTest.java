package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

public class RobotsDotTextServletTest {

    private static class FakeServletOutputStream extends ServletOutputStream {

        private final ByteArrayOutputStream bout = new ByteArrayOutputStream();

        @Override
        public boolean isReady() {
            throw new UnsupportedOperationException();
        }

        @Override
        public void setWriteListener(final WriteListener writeListener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public String toString() {
            return this.bout.toString();
        }

        @Override
        public void write(final int b) throws IOException {
            this.bout.write(b);
        }
    }

    private FakeServletOutputStream outputStream;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private RobotsDotTextServlet servlet;

    @Before
    public void setUp() {
        this.servlet = new RobotsDotTextServlet();
        this.response = mock(HttpServletResponse.class);
        this.request = mock(HttpServletRequest.class);
        this.outputStream = new FakeServletOutputStream();
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        expect(this.request.getHeader("X-Forwarded-Host")).andReturn(null);
        expect(this.request.getHeader("Host")).andReturn("lane.stanford.edu");
        expect(this.response.getOutputStream()).andReturn(this.outputStream);
        replay(this.response, this.request);
        this.servlet.doGet(this.request, this.response);
        assertFalse(this.outputStream.toString().equals("User-agent: *\nDisallow: /"));
        verify(this.response, this.request);
    }

    @Test
    public void testDoGetForwardedForLane() throws ServletException, IOException {
        expect(this.request.getHeader("X-Forwarded-Host")).andReturn("lane.stanford.edu");
        expect(this.response.getOutputStream()).andReturn(this.outputStream);
        replay(this.response, this.request);
        this.servlet.doGet(this.request, this.response);
        assertFalse(this.outputStream.toString().equals("User-agent: *\nDisallow: /"));
        verify(this.response, this.request);
    }

    @Test
    public void testDoGetForwardedForLaneCommas() throws ServletException, IOException {
        expect(this.request.getHeader("X-Forwarded-Host")).andReturn("lane.stanford.edu, notlane.stanford.edu");
        expect(this.response.getOutputStream()).andReturn(this.outputStream);
        replay(this.response, this.request);
        this.servlet.doGet(this.request, this.response);
        assertFalse(this.outputStream.toString().equals("User-agent: *\nDisallow: /"));
        verify(this.response, this.request);
    }

    @Test
    public void testDoGetNotProduction() throws ServletException, IOException {
        expect(this.request.getHeader("X-Forwarded-Host")).andReturn(null);
        expect(this.request.getHeader("Host")).andReturn("notlane.stanford.edu");
        expect(this.response.getOutputStream()).andReturn(this.outputStream);
        replay(this.response, this.request);
        this.servlet.doGet(this.request, this.response);
        assertTrue(this.outputStream.toString().equals("User-agent: *\nDisallow: /"));
        verify(this.response, this.request);
    }
}
