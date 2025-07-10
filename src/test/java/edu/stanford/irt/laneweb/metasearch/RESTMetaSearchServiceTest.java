package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.rest.BasicAuthRESTService;
import edu.stanford.irt.laneweb.rest.RESTException;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.status.ApplicationStatus;

public class RESTMetaSearchServiceTest {

    private BasicAuthRESTService restService;

    private Result result;

    private MetaSearchService service;

    private URI uri;

    @BeforeEach
    public void setUp() throws URISyntaxException {
        this.restService = mock(BasicAuthRESTService.class);
        this.uri = new URI("/");
        this.service = new RESTMetaSearchService(this.uri, this.restService);
        this.result = mock(Result.class);
    }

    @Test
    public void testClearAllCaches() throws RESTException, URISyntaxException {
        expect(this.restService.getObject(new URI("/clearCache"), String.class)).andReturn("OK");
        replay(this.restService);
        this.service.clearAllCaches();
        verify(this.restService);
    }

    @Test
    public void testClearCache() throws RESTException, URISyntaxException {
        expect(this.restService.getObject(new URI("/clearCache?query=the+query"), String.class)).andReturn("OK");
        replay(this.restService);
        this.service.clearCache("the query");
        verify(this.restService);
    }

    @Test
    public void testDescribe() throws URISyntaxException {
        expect(this.restService.getObject(new URI("/describe?query=the+query&engines=pubmed"), Result.class))
                .andReturn(this.result);
        replay(this.restService);
        assertSame(this.result, this.service.describe("the query", Collections.singleton("pubmed")));
        verify(this.restService);
    }

    @Test
    public void testGetStatus() throws RESTException, URISyntaxException {
        ApplicationStatus status = mock(ApplicationStatus.class);
        expect(this.restService.getObject(new URI("/status.json"), ApplicationStatus.class)).andReturn(status);
        replay(this.restService);
        assertSame(status, this.service.getStatus());
        verify(this.restService);
    }

    @Test
    public void testSearch() throws URISyntaxException {
        expect(this.restService.getObject(new URI("/search?query=the+query&engines=pubmed&timeout=0"), Result.class))
                .andReturn(this.result);
        replay(this.restService);
        assertSame(this.result, this.service.search("the query", Collections.singleton("pubmed"), 0));
        verify(this.restService);
    }

    @Test
    public void testTestURL() throws IOException, URISyntaxException {
        expect(this.restService.getInputStream(new URI("/test-url?url=uri")))
                .andReturn(getClass().getResourceAsStream("metasearchservicetest/test-url"));
        replay(this.restService);
        assertArrayEquals("test".getBytes(), this.service.testURL("uri"));
        verify(this.restService);
    }
}
