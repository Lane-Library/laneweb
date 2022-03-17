package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

public class ShibTesterTest {

    private HttpServletRequest request;

    private HttpServletResponse response;

    private ShibTester tester;

    @Before
    public void setUp() {
        this.tester = new ShibTester();
        this.request = mock(HttpServletRequest.class);
        this.response = mock(HttpServletResponse.class);
    }

    @Test
    public void testTestUrl() throws IOException {
        try (BufferedReader r = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("shib-tester.txt")))) {
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = r.readLine()) != null) {
                sb.append(line).append('\n');
            }
            StringWriter sw = new StringWriter();
            expect(this.response.getWriter()).andReturn(new PrintWriter(sw));
            expect(this.request.getHeaderNames()).andReturn(Collections.enumeration(Collections.singleton("name")));
            expect(this.request.getHeader("name")).andReturn("value");
            expect(this.request.getRemoteUser()).andReturn("user");
            expect(this.request.getAttribute(isA(String.class))).andReturn("value").times(24);
            expect(this.request.getAttributeNames()).andReturn(
                    Collections.enumeration(Arrays.asList(new String[] { "name", "org.springframework.foo" })));
            this.response.setContentType("text/plain");
            replay(this.request, this.response);
            this.tester.testUrl(this.request, this.response);
            assertEquals(sb.toString(), sw.toString());
            verify(this.request, this.response);
        }
    }
}
