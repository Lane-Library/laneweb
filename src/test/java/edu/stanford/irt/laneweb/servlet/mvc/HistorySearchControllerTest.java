package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collections;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;

import edu.stanford.irt.laneweb.servlet.binding.CompositeDataBinder;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.ResultBuilder;

public class HistorySearchControllerTest {

    private HistorySearchController controller;

    private CompositeDataBinder dataBinder;

    private MetaSearchManager metasearchManager;

    private Model model;

    private Result result;

    @Before
    public void setUp() {
        this.dataBinder = createMock(CompositeDataBinder.class);
        this.metasearchManager = createMock(MetaSearchManager.class);
        this.controller = new HistorySearchController(this.metasearchManager, this.dataBinder);
        this.model = createMock(Model.class);
        this.result = new ResultBuilder().setId("id").setDescription("description").setURL("url").build();
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
        this.result.setHits("10");
        this.result.setStatus(SearchStatus.SUCCESSFUL);
        this.result.setChildren(Collections.singleton(this.result));
        expect(this.metasearchManager.search(isA(Query.class), eq(60000L), eq(true))).andReturn(this.result);
        replay(this.metasearchManager);
        Map<String, Object> resultMap = this.controller.search("query");
        assertEquals(SearchStatus.SUCCESSFUL, resultMap.get("status"));
        Map<String, Object> resourceMap = (Map<String, Object>) resultMap.get("resources");
        Map<String, Object> resource = (Map<String, Object>) resourceMap.get("id");
        assertEquals(10, resource.get("hits"));
        assertEquals(SearchStatus.SUCCESSFUL, resource.get("status"));
        assertEquals("description", resource.get("description"));
        assertEquals("url", resource.get("url"));
        verify(this.metasearchManager);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testSearchFailedNullHits() {
        this.result.setStatus(SearchStatus.FAILED);
        this.result.setChildren(Collections.singleton(this.result));
        expect(this.metasearchManager.search(isA(Query.class), eq(60000L), eq(true))).andReturn(this.result);
        replay(this.metasearchManager);
        Map<String, Object> resultMap = this.controller.search("query");
        assertEquals(SearchStatus.FAILED, resultMap.get("status"));
        Map<String, Object> resourceMap = (Map<String, Object>) resultMap.get("resources");
        Map<String, Object> resource = (Map<String, Object>) resourceMap.get("id");
        assertNull(resource.get("hits"));
        assertEquals(SearchStatus.FAILED, resource.get("status"));
        assertEquals("description", resource.get("description"));
        assertEquals("url", resource.get("url"));
        verify(this.metasearchManager);
    }
}
