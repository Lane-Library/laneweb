package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.impl.Result;

public class ResourceSearchGeneratorTest {

    private ResourceSearchGenerator generator;

    private MetaSearchService metaSearchService;

    private Result result;

    private SAXStrategy<Result> saxStrategy;

    @Before
    public void setUp() throws Exception {
        this.metaSearchService = mock(MetaSearchService.class);
        this.saxStrategy = mock(SAXStrategy.class);
        this.generator = new ResourceSearchGenerator(this.metaSearchService, this.saxStrategy);
        this.result = mock(Result.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoSearch() {
        expect(this.metaSearchService.describe("query", null)).andReturn(this.result);
        expect(this.result.getChildren()).andReturn(Arrays.asList(new Result[] { this.result, this.result })).times(3);
        expect(this.result.getId()).andReturn("engine-1");
        expect(this.result.getId()).andReturn("resource-1-1");
        expect(this.result.getId()).andReturn("resource-1-2");
        expect(this.result.getId()).andReturn("engine-2");
        expect(this.result.getId()).andReturn("resource-2-1");
        expect(this.result.getId()).andReturn("resource-2-2");
        expect(this.metaSearchService.search(eq("query"), isA(Collection.class), eq(60000L))).andReturn(this.result);
        replay(this.metaSearchService, this.saxStrategy, this.result);
        Map<String, Object> model = new HashMap<>();
        model.put(Model.QUERY, "query");
        model.put("resources", Collections.singleton("resource-2-1"));
        this.generator.setParameters(Collections.emptyMap());
        this.generator.setModel(model);
        this.generator.doSearch("query");
        verify(this.metaSearchService, this.saxStrategy, this.result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoSearchNullQuery() {
        expect(this.metaSearchService.describe("query", null)).andReturn(this.result);
        expect(this.result.getChildren()).andReturn(Arrays.asList(new Result[] { this.result, this.result })).times(3);
        expect(this.result.getId()).andReturn("engine-1");
        expect(this.result.getId()).andReturn("resource-1-1");
        expect(this.result.getId()).andReturn("resource-1-2");
        expect(this.result.getId()).andReturn("engine-2");
        expect(this.result.getId()).andReturn("resource-2-1");
        expect(this.result.getId()).andReturn("resource-2-2");
        expect(this.metaSearchService.search(eq("query"), isA(Collection.class), eq(60000L))).andReturn(this.result);
        replay(this.metaSearchService, this.saxStrategy, this.result);
        Map<String, Object> model = new HashMap<>();
        model.put("resources", Collections.singleton("resource-2-1"));
        model.put(Model.QUERY, "query");
        this.generator.setModel(model);
        this.generator.doSearch("query");
        verify(this.metaSearchService, this.saxStrategy, this.result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoSearchParameters() {
        expect(this.metaSearchService.describe("query", null)).andReturn(this.result);
        expect(this.result.getChildren()).andReturn(Arrays.asList(new Result[] { this.result, this.result })).times(3);
        expect(this.result.getId()).andReturn("engine-1");
        expect(this.result.getId()).andReturn("resource-1-1");
        expect(this.result.getId()).andReturn("resource-1-2");
        expect(this.result.getId()).andReturn("engine-2");
        expect(this.result.getId()).andReturn("resource-2-1");
        expect(this.result.getId()).andReturn("resource-2-2");
        expect(this.metaSearchService.search(eq("query"), isA(Collection.class), eq(60000L))).andReturn(this.result);
        replay(this.metaSearchService, this.saxStrategy, this.result);
        this.generator.setModel(Collections.singletonMap(Model.QUERY, "query"));
        Map<String, String> parameters = new HashMap<>();
        parameters.put("resource-list", "resource-1-2,resource-2-1,foo");
        this.generator.setParameters(parameters);
        this.generator.doSearch("query");
        verify(this.metaSearchService, this.saxStrategy, this.result);
    }

    @Test
    public void testSetParametersNullResourceList() {
        replay(this.metaSearchService, this.saxStrategy, this.result);
        this.generator.setModel(Collections.singletonMap(Model.QUERY, "query"));
        try {
            this.generator.setParameters(Collections.emptyMap());
        } catch (NullPointerException e) {
        }
        verify(this.metaSearchService, this.saxStrategy, this.result);
    }
}
