package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;

import edu.stanford.irt.laneweb.metasearch.MetaSearchService;
import edu.stanford.irt.laneweb.servlet.binding.CompositeDataBinder;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.Result.ResultBuilder;

public class HistorySearchControllerTest {

    private ResultBuilder builder;

    private HistorySearchController controller;

    private CompositeDataBinder dataBinder;

    private MetaSearchService metaSearchService;

    private Model model;

    @Before
    public void setUp() {
        this.dataBinder = createMock(CompositeDataBinder.class);
        this.metaSearchService = createMock(MetaSearchService.class);
        this.controller = new HistorySearchController(this.metaSearchService, this.dataBinder);
        this.model = createMock(Model.class);
        this.builder = Result.newResultBuilder().id("id").description("description").url("url");
    }

    @Test
    public void testBind() {
        expect(this.model.asMap()).andReturn(null);
        this.dataBinder.bind(null, null);
        replay(this.dataBinder, this.model);
        this.controller.bind(null, this.model);
        verify(this.dataBinder, this.model);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSearch() {
        Result child = Result
                .newResultBuilder()
                .id("id")
                .description("description")
                .url("url")
                .children(
                        Collections.singleton(Result.newResultBuilder().hits("10").status(SearchStatus.SUCCESSFUL)
                                .id("id").description("description").url("url").build())).build();
        this.builder.status(SearchStatus.SUCCESSFUL);
        this.builder.children(Collections.singleton(child));
        expect(this.metaSearchService.search(isA(Query.class), isA(Collection.class), eq(60000L))).andReturn(
                this.builder.build());
        replay(this.metaSearchService);
        Map<String, Object> resultMap = this.controller.search("query");
        assertEquals(SearchStatus.SUCCESSFUL, resultMap.get("status"));
        Map<String, Object> resourceMap = (Map<String, Object>) resultMap.get("resources");
        Map<String, Object> resource = (Map<String, Object>) resourceMap.get("id");
        assertEquals(10, resource.get("hits"));
        assertEquals(SearchStatus.SUCCESSFUL, resource.get("status"));
        assertEquals("description", resource.get("description"));
        assertEquals("url", resource.get("url"));
        verify(this.metaSearchService);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSearchFailedNullHits() {
        Result child = Result
                .newResultBuilder()
                .id("id")
                .description("description")
                .url("url")
                .children(
                        Collections.singleton(Result.newResultBuilder().id("id").description("description").url("url")
                                .build())).build();
        this.builder.status(SearchStatus.FAILED);
        this.builder.children(Collections.singleton(child));
        expect(this.metaSearchService.search(isA(Query.class), isA(Collection.class), eq(60000L))).andReturn(
                this.builder.build());
        replay(this.metaSearchService);
        Map<String, Object> resultMap = this.controller.search("query");
        assertEquals(SearchStatus.FAILED, resultMap.get("status"));
        Map<String, Object> resourceMap = (Map<String, Object>) resultMap.get("resources");
        Map<String, Object> resource = (Map<String, Object>) resourceMap.get("id");
        assertNull(resource.get("hits"));
        assertEquals("description", resource.get("description"));
        assertEquals("url", resource.get("url"));
        verify(this.metaSearchService);
    }
}
