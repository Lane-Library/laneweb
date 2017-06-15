package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
    }

    @Test
    public void testTestUrl() throws IOException {
        List<String> lines = Files.readAllLines(
                FileSystems.getDefault().getPath(getClass().getResource("shib-tester.txt").getPath()));
        StringBuilder sb = new StringBuilder();
        lines.stream().forEach(s -> sb.append(s).append('\n'));
        StringWriter sw = new StringWriter();
        expect(this.response.getWriter()).andReturn(new PrintWriter(sw));
        expect(this.request.getHeaderNames()).andReturn(Collections.enumeration(Collections.singleton("name")));
        expect(this.request.getHeader("name")).andReturn("value");
        expect(this.request.getRemoteUser()).andReturn("user");
        expect(this.request.getAttribute(isA(String.class))).andReturn("value").times(25);
        expect(this.request.getAttributeNames())
                .andReturn(Collections.enumeration(Arrays.asList(new String[] { "name", "org.springframework.foo" })));
        this.response.setContentType("text/plain");
        replay(this.request, this.response);
        this.tester.testUrl(this.request, this.response);
        assertEquals(sb.toString(), sw.toString());
        verify(this.request, this.response);
    }
}
