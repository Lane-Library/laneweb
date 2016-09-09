package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.search.MetaSearchService;

public class ClearSearchCacheControllerTest {

    private ClearSearchCacheController controller;

    private MetaSearchService metaSearchService;

    @Before
    public void setUp() throws Exception {
        this.metaSearchService = createMock(MetaSearchService.class);
        this.controller = new ClearSearchCacheController(this.metaSearchService);
    }

    @Test
    public void testClearCache() {
        this.metaSearchService.clearAllCaches();;
        replay(this.metaSearchService);
        assertEquals("OK", this.controller.clearCache(null));
        verify(this.metaSearchService);
    }

    @Test
    public void testClearCacheQuery() {
        this.metaSearchService.clearCache("query");
        replay(this.metaSearchService);
        assertEquals("OK", this.controller.clearCache("query"));
        verify(this.metaSearchService);
    }
}
