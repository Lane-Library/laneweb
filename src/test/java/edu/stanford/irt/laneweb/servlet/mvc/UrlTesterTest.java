package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertArrayEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.metasearch.MetaSearchService;

public class UrlTesterTest {

    private MetaSearchService metasearchService;

    private UrlTester urlTester;

    @Before
    public void setUp() {
        this.metasearchService = mock(MetaSearchService.class);
        this.urlTester = new UrlTester(this.metasearchService);
    }

    @Test
    public void testTestUrl() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        expect(this.metasearchService.testURL("url")).andReturn("result".getBytes());
        replay(this.metasearchService);
        this.urlTester.testUrl("url", baos);
        assertArrayEquals("result".getBytes(), baos.toByteArray());
        verify(this.metasearchService);
    }
}
