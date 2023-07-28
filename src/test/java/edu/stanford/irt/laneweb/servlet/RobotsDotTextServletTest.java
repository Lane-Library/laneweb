package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
    public void testDoGet() throws IOException {
        expect(this.request.getHeader("X-Forwarded-Host")).andReturn(null);
        expect(this.request.getHeader("Host")).andReturn("lane.stanford.edu");
        expect(this.response.getOutputStream()).andReturn(this.outputStream);
        replay(this.response, this.request);
        this.servlet.doGet(this.request, this.response);
        assertNotEquals("User-agent: *\nDisallow: /", this.outputStream.toString());
        verify(this.response, this.request);
    }

    @Test
    public void testDoGetForwardedForLane() throws IOException {
        expect(this.request.getHeader("X-Forwarded-Host")).andReturn("lane.stanford.edu");
        expect(this.response.getOutputStream()).andReturn(this.outputStream);
        replay(this.response, this.request);
        this.servlet.doGet(this.request, this.response);
        assertNotEquals("User-agent: *\nDisallow: /", this.outputStream.toString());
        verify(this.response, this.request);
    }

    @Test
    public void testDoGetForwardedForLaneCommas() throws IOException {
        expect(this.request.getHeader("X-Forwarded-Host")).andReturn("lane.stanford.edu, notlane.stanford.edu");
        expect(this.response.getOutputStream()).andReturn(this.outputStream);
        replay(this.response, this.request);
        this.servlet.doGet(this.request, this.response);
        assertNotEquals("User-agent: *\nDisallow: /", this.outputStream.toString());
        verify(this.response, this.request);
    }

    @Test
    public void testDoGetGCPStage() throws IOException {
        expect(this.request.getHeader("X-Forwarded-Host")).andReturn(null);
        expect(this.request.getHeader("Host")).andReturn("lane-prototype.stanford.edu");
        expect(this.response.getOutputStream()).andReturn(this.outputStream);
        replay(this.response, this.request);
        this.servlet.doGet(this.request, this.response);
        assertEquals("User-agent: *\nAllow: /lmldbx/\nDisallow: /", this.outputStream.toString());
        verify(this.response, this.request);
    }

    @Test
    public void testDoGetNotProduction() throws IOException {
        expect(this.request.getHeader("X-Forwarded-Host")).andReturn(null);
        expect(this.request.getHeader("Host")).andReturn("notlane.stanford.edu");
        expect(this.response.getOutputStream()).andReturn(this.outputStream);
        replay(this.response, this.request);
        this.servlet.doGet(this.request, this.response);
        assertEquals("User-agent: *\nDisallow: /", this.outputStream.toString());
        verify(this.response, this.request);
    }
}
