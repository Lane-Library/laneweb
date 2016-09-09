package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.search.MetaSearchService;
import edu.stanford.irt.laneweb.servlet.binding.CompositeDataBinder;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.Result;

public class MetaSearchControllerTest {

    private MetaSearchController controller;

    private CompositeDataBinder dataBinder;

    private MetaSearchService metaSearchService;

    private Map<String, Object> map;

    private org.springframework.ui.Model model;

    private HttpServletRequest request;

    private Result result;

    @Before
    public void setUp() throws Exception {
        this.metaSearchService = createMock(MetaSearchService.class);
        this.dataBinder = createMock(CompositeDataBinder.class);
        this.controller = new MetaSearchController(this.metaSearchService, this.dataBinder);
        this.model = createMock(org.springframework.ui.Model.class);
        this.request = createMock(HttpServletRequest.class);
        this.map = new HashMap<String, Object>();
        this.result = createMock(Result.class);
    }

    @Test
    public void testBindValuesNotPresent() {
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.model.containsAttribute(Model.PROXY_LINKS)).andReturn(false);
        expect(this.model.addAttribute(Model.PROXY_LINKS, false)).andReturn(this.model);
        expect(this.model.containsAttribute(Model.BASE_PROXY_URL)).andReturn(false);
        expect(this.model.addAttribute(Model.BASE_PROXY_URL, null)).andReturn(this.model);
        this.dataBinder.bind(this.map, this.request);
        replay(this.metaSearchService, this.dataBinder, this.model);
        this.controller.bind(this.request, this.model);
        verify(this.metaSearchService, this.dataBinder, this.model);
    }

    @Test
    public void testBindValuesPresent() {
        this.map.put(Model.PROXY_LINKS, Boolean.TRUE);
        this.map.put(Model.BASE_PROXY_URL, "baseProxyURL");
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.model.containsAttribute(Model.PROXY_LINKS)).andReturn(true);
        expect(this.model.containsAttribute(Model.BASE_PROXY_URL)).andReturn(true);
        this.dataBinder.bind(this.map, this.request);
        replay(this.metaSearchService, this.dataBinder, this.model);
        this.controller.bind(this.request, this.model);
        verify(this.metaSearchService, this.dataBinder, this.model);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSearch() {
        expect(this.metaSearchService.describe(isA(Query.class), isNull(Collection.class))).andReturn(this.result);
        expect(this.result.getChildren()).andReturn(Arrays.asList(new Result[] { this.result, this.result })).times(2);
        expect(this.result.getId()).andReturn("id");
        expect(this.result.getId()).andReturn("resource");
        expect(this.result.getId()).andReturn("engine");
        expect(this.result.getChildren()).andReturn(Collections.emptySet());
        expect(this.metaSearchService.search(isA(Query.class), isA(Collection.class), eq(60000L))).andReturn(this.result);
        expect(this.result.getStatus()).andReturn(SearchStatus.SUCCESSFUL);
        expect(this.result.getChildren()).andReturn(Collections.singleton(this.result));
        expect(this.result.getChildren()).andReturn(Arrays.asList(new Result[] { this.result, this.result }));
        expect(this.result.getId()).andReturn("resource");
        expect(this.result.getStatus()).andReturn(SearchStatus.SUCCESSFUL);
        expect(this.result.getURL()).andReturn("url");
        expect(this.result.getHits()).andReturn("2");
        expect(this.result.getId()).andReturn("id");
        replay(this.metaSearchService, this.dataBinder, this.model, this.result);
        Map<String, Object> map = this.controller.search("query", Collections.singletonList("resource"), false,
                "baseProxyURL");
        assertEquals(SearchStatus.SUCCESSFUL, map.get("status"));
        map = (Map<String, Object>) map.get("resources");
        map = (Map<String, Object>) map.get("resource");
        assertEquals(2, map.get("hits"));
        assertEquals(SearchStatus.SUCCESSFUL, map.get("status"));
        assertEquals("url", map.get("url"));
        verify(this.metaSearchService, this.dataBinder, this.model, this.result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSearchNullHits() {
        expect(this.metaSearchService.describe(isA(Query.class), isNull(Collection.class))).andReturn(this.result);
        expect(this.result.getChildren()).andReturn(Arrays.asList(new Result[] { this.result, this.result })).times(2);
        expect(this.result.getId()).andReturn("id");
        expect(this.result.getId()).andReturn("resource");
        expect(this.result.getId()).andReturn("engine");
        expect(this.result.getChildren()).andReturn(Collections.emptySet());
        expect(this.metaSearchService.search(isA(Query.class), isA(Collection.class), eq(60000L))).andReturn(this.result);
        expect(this.result.getStatus()).andReturn(SearchStatus.SUCCESSFUL);
        expect(this.result.getChildren()).andReturn(Collections.singleton(this.result));
        expect(this.result.getChildren()).andReturn(Arrays.asList(new Result[] { this.result, this.result }));
        expect(this.result.getId()).andReturn("resource");
        expect(this.result.getStatus()).andReturn(SearchStatus.SUCCESSFUL);
        expect(this.result.getURL()).andReturn("url");
        expect(this.result.getHits()).andReturn(null);
        expect(this.result.getId()).andReturn("id");
        replay(this.metaSearchService, this.dataBinder, this.model, this.result);
        Map<String, Object> map = this.controller.search("query", Collections.singletonList("resource"), false,
                "baseProxyURL");
        assertEquals(SearchStatus.SUCCESSFUL, map.get("status"));
        map = (Map<String, Object>) map.get("resources");
        map = (Map<String, Object>) map.get("resource");
        assertNull(map.get("hits"));
        assertEquals(SearchStatus.SUCCESSFUL, map.get("status"));
        assertEquals("url", map.get("url"));
        verify(this.metaSearchService, this.dataBinder, this.model, this.result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSearchProxyLinks() {
        expect(this.metaSearchService.describe(isA(Query.class), isNull(Collection.class))).andReturn(this.result);
        expect(this.result.getChildren()).andReturn(Collections.singletonList(this.result)).times(4);
        expect(this.result.getId()).andReturn("resource");
        expect(this.result.getId()).andReturn("engine");
        expect(this.metaSearchService.search(isA(Query.class), isA(Collection.class), eq(60000L))).andReturn(this.result);
        expect(this.result.getStatus()).andReturn(SearchStatus.SUCCESSFUL).times(2);
        expect(this.result.getId()).andReturn("resource");
        expect(this.result.getURL()).andReturn("url");
        expect(this.result.getHits()).andReturn("2");
        replay(this.metaSearchService, this.dataBinder, this.model, this.result);
        Map<String, Object> map = this.controller.search("query", Collections.singletonList("resource"), true,
                "baseProxyURL");
        assertEquals(SearchStatus.SUCCESSFUL, map.get("status"));
        map = (Map<String, Object>) map.get("resources");
        map = (Map<String, Object>) map.get("resource");
        assertEquals(Integer.valueOf("2"), map.get("hits"));
        assertEquals(SearchStatus.SUCCESSFUL, map.get("status"));
        assertEquals("baseProxyURLurl", map.get("url"));
        verify(this.metaSearchService, this.dataBinder, this.model, this.result);
    }
}
