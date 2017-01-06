package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.metasearch.MetaSearchManagerSource;
import edu.stanford.irt.search.spring.SearchCacheManager;

public class ClearSearchCacheControllerTest {

    private ClearSearchCacheController controller;

    private MetaSearchManagerSource msms;

    private SearchCacheManager searchCacheManger;

    @Before
    public void setUp() throws Exception {
        this.msms = createMock(MetaSearchManagerSource.class);
        this.controller = new ClearSearchCacheController(this.msms);
        this.searchCacheManger = createMock(SearchCacheManager.class);
    }

    @Test
    public void testClearCache() {
        expect(this.msms.getSearchCacheManager()).andReturn(this.searchCacheManger);
        this.searchCacheManger.clearAllCaches();
        replay(this.msms, this.searchCacheManger);
        assertEquals("OK", this.controller.clearCache(null));
        verify(this.msms, this.searchCacheManger);
    }

    @Test
    public void testClearCacheQuery() {
        expect(this.msms.getSearchCacheManager()).andReturn(this.searchCacheManger);
        this.searchCacheManger.clearCache("query");
        replay(this.msms, this.searchCacheManger);
        assertEquals("OK", this.controller.clearCache("query"));
        verify(this.msms, this.searchCacheManger);
    }
}
