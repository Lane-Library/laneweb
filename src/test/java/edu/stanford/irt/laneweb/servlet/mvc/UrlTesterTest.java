package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.metasearch.MetaSearchService;

public class UrlTesterTest {

    private MetaSearchService metasearchService;

    private HttpServletResponse servletResponse;

    private UrlTester urlTester;

    @Before
    public void setUp() {
        this.metasearchService = mock(MetaSearchService.class);
        this.urlTester = new UrlTester(this.metasearchService);
        this.servletResponse = mock(HttpServletResponse.class);
    }

    @Test
    public void testTestUrl() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ServletOutputStream output = new ServletOutputStream() {

            @Override
            public boolean isReady() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void setWriteListener(final WriteListener writeListener) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void write(final int b) throws IOException {
                baos.write(b);
            }
        };
        expect(this.metasearchService.testURL("url")).andReturn("result".getBytes());
        this.servletResponse.setHeader("Content-Type", "text/plain");
        expect(this.servletResponse.getOutputStream()).andReturn(output);
        replay(this.metasearchService, this.servletResponse);
        this.urlTester.testUrl("url", this.servletResponse);
        assertArrayEquals("result".getBytes(), baos.toByteArray());
        verify(this.metasearchService, this.servletResponse);
    }
}
