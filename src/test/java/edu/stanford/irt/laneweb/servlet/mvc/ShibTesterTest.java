package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;

public class ShibTesterTest {

    private ServletOutputStream output;

    private HttpServletRequest request;

    private HttpServletResponse response;

    private ShibTester tester;

    @Before
    public void setUp() {
        this.tester = new ShibTester();
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.output = createMock(ServletOutputStream.class);
    }

    @Test
    public void testTestUrl() throws IOException {
        expect(this.request.getHeaderNames()).andReturn(Collections.enumeration(Collections.singleton("name")));
        expect(this.request.getHeader("name")).andReturn("value");
        expect(this.request.getRemoteUser()).andReturn("user");
        expect(this.request.getAttribute(isA(String.class))).andReturn("value").times(25);
        expect(this.request.getAttributeNames()).andReturn(
                Collections.enumeration(Arrays.asList(new String[] { "name", "org.spring.foo" })));
        this.response.setContentType("text/plain");
        expect(this.response.getOutputStream()).andReturn(this.output);
        this.output.write(isA(byte[].class));
        replay(this.request, this.response, this.output);
        this.tester.testUrl(this.request, this.response);
        verify(this.request, this.response, this.output);
    }

    @Test(expected = LanewebException.class)
    public void testTestUrlThrowsException() throws IOException {
        expect(this.request.getHeaderNames()).andReturn(Collections.enumeration(Collections.singleton("name")));
        expect(this.request.getHeader("name")).andReturn("value");
        expect(this.request.getRemoteUser()).andReturn("user");
        expect(this.request.getAttribute(isA(String.class))).andReturn("value").times(25);
        expect(this.request.getAttributeNames()).andReturn(
                Collections.enumeration(Arrays.asList(new String[] { "name", "org.spring.foo" })));
        this.response.setContentType("text/plain");
        expect(this.response.getOutputStream()).andReturn(this.output);
        this.output.write(isA(byte[].class));
        expectLastCall().andThrow(new IOException());
        replay(this.request, this.response, this.output);
        this.tester.testUrl(this.request, this.response);
        verify(this.request, this.response, this.output);
    }
}
