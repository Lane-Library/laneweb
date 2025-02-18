package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.metasearch.MetaSearchService;

public class ClearSearchCacheControllerTest {

    private ClearSearchCacheController controller;

    private MetaSearchService metaSearchService;

    @BeforeEach
    public void setUp() throws Exception {
        this.metaSearchService = mock(MetaSearchService.class);
        this.controller = new ClearSearchCacheController(this.metaSearchService);
    }

    @Test
    public void testClearCache() {
        this.metaSearchService.clearAllCaches();
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
