package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.same;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.Collections;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.spring.SearchCacheManager;

public class SpringContextMetaSearchServiceTest {

    private ConfigurableApplicationContext context;

    private SpringContextMetaSearchService service;

    @Before
    public void setUp() throws Exception {
        this.context = createMock(ConfigurableApplicationContext.class);
        this.service = new SpringContextMetaSearchService(this.context);
    }

    @Test
    public void testClearAllCaches() {
        SearchCacheManager searchCacheManager = createMock(SearchCacheManager.class);
        expect(this.context.isActive()).andReturn(true);
        expect(this.context.getBean(SearchCacheManager.class)).andReturn(searchCacheManager);
        searchCacheManager.clearAllCaches();
        replay(this.context, searchCacheManager);
        this.service.clearAllCaches();
        verify(this.context, searchCacheManager);
    }

    @Test
    public void testClearCache() {
        SearchCacheManager searchCacheManager = createMock(SearchCacheManager.class);
        expect(this.context.isActive()).andReturn(true);
        expect(this.context.getBean(SearchCacheManager.class)).andReturn(searchCacheManager);
        searchCacheManager.clearCache("query");
        replay(this.context, searchCacheManager);
        this.service.clearCache("query");
        verify(this.context, searchCacheManager);
    }

    @Test
    public void testDescribe() {
        MetaSearchManager metaSearchManager = createMock(MetaSearchManager.class);
        Result result = createMock(Result.class);
        expect(this.context.isActive()).andReturn(true);
        expect(this.context.getBean(MetaSearchManager.class)).andReturn(metaSearchManager);
        expect(metaSearchManager.describe(isA(Query.class), same(Collections.emptySet()))).andReturn(result);
        replay(this.context, metaSearchManager);
        assertSame(result, this.service.describe("query", Collections.emptySet()));
        verify(this.context, metaSearchManager);
    }

    @Test
    public void testDispose() {
        expect(this.context.isActive()).andReturn(true);
        this.context.close();
        replay(this.context);
        this.service.dispose();
        verify(this.context);
    }

    @Test
    public void testDisposeNotActive() {
        expect(this.context.isActive()).andReturn(false);
        replay(this.context);
        this.service.dispose();
        verify(this.context);
    }

    @Test
    public void testExecute() throws IOException {
        HttpClient httpClient = createMock(HttpClient.class);
        HttpGet httpGet = createMock(HttpGet.class);
        HttpResponse httpResponse = createMock(HttpResponse.class);
        expect(this.context.isActive()).andReturn(true);
        expect(this.context.getBean(HttpClient.class)).andReturn(httpClient);
        expect(httpClient.execute(httpGet)).andReturn(httpResponse);
        replay(this.context, httpClient);
        assertSame(httpResponse, this.service.execute(httpGet));
        verify(this.context, httpClient);
    }

    @Test
    public void testSearch() {
        MetaSearchManager metaSearchManager = createMock(MetaSearchManager.class);
        Result result = createMock(Result.class);
        expect(this.context.isActive()).andReturn(true);
        expect(this.context.getBean(MetaSearchManager.class)).andReturn(metaSearchManager);
        expect(metaSearchManager.search(isA(Query.class), same(Collections.emptySet()), eq(0L))).andReturn(result);
        replay(this.context, metaSearchManager);
        assertSame(result, this.service.search("query", Collections.emptySet(), 0L));
        verify(this.context, metaSearchManager);
    }

    @Test
    public void testSearchNotActive() {
        MetaSearchManager metaSearchManager = createMock(MetaSearchManager.class);
        Result result = createMock(Result.class);
        expect(this.context.isActive()).andReturn(false);
        this.context.refresh();
        expect(this.context.getBean(MetaSearchManager.class)).andReturn(metaSearchManager);
        expect(metaSearchManager.search(isA(Query.class), same(Collections.emptySet()), eq(0L))).andReturn(result);
        replay(this.context, metaSearchManager);
        assertSame(result, this.service.search("query", Collections.emptySet(), 0L));
        verify(this.context, metaSearchManager);
    }
}
