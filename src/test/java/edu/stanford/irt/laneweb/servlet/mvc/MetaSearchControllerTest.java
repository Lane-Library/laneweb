package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.CompositeDataBinder;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.SearchStatus;

public class MetaSearchControllerTest {

    private MetaSearchController controller;

    private CompositeDataBinder dataBinder;

    private MetaSearchManager<Result> manager;

    private Map<String, Object> map;

    private org.springframework.ui.Model model;

    private HttpServletRequest request;

    private Result result;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.manager = createMock(MetaSearchManager.class);
        this.dataBinder = createMock(CompositeDataBinder.class);
        this.controller = new MetaSearchController(this.manager, this.dataBinder);
        this.model = createMock(org.springframework.ui.Model.class);
        this.request = createMock(HttpServletRequest.class);
        this.map = new HashMap<String, Object>();
        this.result = createMock(Result.class);
    }

    @Test
    public void testBind() {
        expect(this.model.asMap()).andReturn(this.map);
        expect(this.model.containsAttribute(Model.PROXY_LINKS)).andReturn(false);
        expect(this.model.addAttribute(Model.PROXY_LINKS, false)).andReturn(this.model);
        expect(this.model.containsAttribute(Model.BASE_PROXY_URL)).andReturn(false);
        expect(this.model.addAttribute(Model.BASE_PROXY_URL, null)).andReturn(this.model);
        this.dataBinder.bind(this.map, this.request);
        replay(this.manager, this.dataBinder, this.model);
        this.controller.bind(this.request, this.model);
        verify(this.manager, this.dataBinder, this.model);
    }

    @Test
    public void testSearch() {
        expect(this.manager.describe(isA(Query.class), eq(Collections.singletonList("resource")))).andReturn(
                this.result);
        expect(this.result.getChildren()).andReturn(Collections.singletonList(this.result)).times(4);
        expect(this.result.getId()).andReturn("resource");
        expect(this.result.getId()).andReturn("engine");
        expect(this.manager.search(isA(Query.class), eq(60000L), eq(Collections.singletonList("engine")), eq(false)))
                .andReturn(this.result);
        expect(this.result.getStatus()).andReturn(SearchStatus.SUCCESSFUL).times(2);
        expect(this.result.getId()).andReturn("resource");
        expect(this.result.getURL()).andReturn("url");
        expect(this.result.getHits()).andReturn("2");
        replay(this.manager, this.dataBinder, this.model, this.result);
        assertEquals("{resources={resource={hits=2, status=SUCCESSFUL, url=url}}, status=SUCCESSFUL}", this.controller
                .search("query", Collections.singletonList("resource"), false, "baseProxyURL").toString());
        verify(this.manager, this.dataBinder, this.model, this.result);
    }
}
