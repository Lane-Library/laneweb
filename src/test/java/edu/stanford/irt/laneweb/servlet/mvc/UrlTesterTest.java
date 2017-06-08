package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.metasearch.MetaSearchService;

public class UrlTesterTest {

    private HttpEntity entity;

    private byte[] expectedResult = "response text\n\n\n<!--\n\nRequest Headers:\n\n\n\n\nResponse Headers:\n\nname ==> value\n\n-->"
            .getBytes();

    private Header header;

    private HttpResponse httpResponse;

    private MetaSearchService metaSearchService;

    private ServletOutputStream outputStream;

    private HttpServletResponse response;

    private String responseText = "response text";

    private UrlTester tester;

    @Before
    public void setUp() throws Exception {
        this.tester = new UrlTester();
        this.metaSearchService = createMock(MetaSearchService.class);
        this.response = createMock(HttpServletResponse.class);
        this.httpResponse = createMock(HttpResponse.class);
        this.entity = createMock(HttpEntity.class);
        this.outputStream = createMock(ServletOutputStream.class);
        this.header = createMock(Header.class);
    }

    @Test
    public void testTestUrl() throws IOException {
        expect(this.metaSearchService.execute(isA(HttpGet.class))).andReturn(this.httpResponse);
        expect(this.httpResponse.getEntity()).andReturn(this.entity);
        expect(this.entity.getContent())
                .andReturn(new ByteArrayInputStream(this.responseText.getBytes(StandardCharsets.UTF_8)));
        expect(this.entity.getContentLength()).andReturn((long) this.responseText.length()).times(2);
        this.response.setHeader("Content-Type", "text/plain");
        expect(this.response.getOutputStream()).andReturn(this.outputStream);
        expect(this.httpResponse.getAllHeaders()).andReturn(new Header[] { this.header });
        expect(this.header.getName()).andReturn("name");
        expect(this.header.getValue()).andReturn("value");
        this.outputStream.write(aryEq(this.expectedResult));
        replay(this.metaSearchService, this.response, this.httpResponse, this.entity, this.outputStream, this.header);
        this.tester.setMetaSearchManagerSource(this.metaSearchService);
        this.tester.testUrl("url", this.response);
        verify(this.metaSearchService, this.response, this.httpResponse, this.entity, this.outputStream, this.header);
    }

    @Test
    public void testTestUrlException() throws IOException {
        expect(this.metaSearchService.execute(isA(HttpGet.class))).andThrow(new IOException());
        replay(this.metaSearchService, this.response, this.httpResponse, this.entity, this.outputStream, this.header);
        this.tester.setMetaSearchManagerSource(this.metaSearchService);
        try {
            this.tester.testUrl("url", this.response);
            fail();
        } catch (LanewebException e) {
        }
        verify(this.metaSearchService, this.response, this.httpResponse, this.entity, this.outputStream, this.header);
    }
}
