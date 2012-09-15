package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.Result;

public class ResourceSearchGeneratorTest {

    private ResourceSearchGenerator generator;

    private MetaSearchManager manager;

    private Result result;

    private SAXStrategy<Result> saxStrategy;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.manager = createMock(MetaSearchManager.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new ResourceSearchGenerator(this.manager, this.saxStrategy);
        this.result = createMock(Result.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoSearch() {
        expect(this.manager.describe(isA(Query.class), (Collection<String>) isNull())).andReturn(this.result);
        expect(this.result.getChildren()).andReturn(Arrays.asList(new Result[] { this.result, this.result })).times(6);
        expect(this.result.getId()).andReturn("engine-1");
        expect(this.result.getId()).andReturn("resource-1-1");
        expect(this.result.getId()).andReturn("resource-1-2");
        expect(this.result.getId()).andReturn("engine-2");
        expect(this.result.getId()).andReturn("resource-2-1");
        expect(this.result.getId()).andReturn("resource-2-2");
        expect(this.manager.search(isA(Query.class), eq(60000L), eq(Collections.singleton("engine-2")), eq(false))).andReturn(
                this.result);
        expect(this.result.getId()).andReturn("resource-1-1");
        expect(this.result.getId()).andReturn("resource-1-2");
        expect(this.result.getId()).andReturn("resource-2-1");
        expect(this.result.getId()).andReturn("resource-2-2");
        this.result.removeChild(this.result);
        expectLastCall().times(3);
        replay(this.manager, this.saxStrategy, this.result);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(Model.QUERY, "query");
        model.put("resources", Collections.singleton("resource-2-1"));
        this.generator.setParameters(Collections.<String, String> emptyMap());
        this.generator.setModel(model);
        this.generator.doSearch("query");
        verify(this.manager, this.saxStrategy, this.result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoSearchNullQuery() {
        expect(this.manager.describe(isA(Query.class), (Collection<String>) isNull())).andReturn(this.result);
        expect(this.result.getChildren()).andReturn(Arrays.asList(new Result[] { this.result, this.result })).times(6);
        expect(this.result.getId()).andReturn("engine-1");
        expect(this.result.getId()).andReturn("resource-1-1");
        expect(this.result.getId()).andReturn("resource-1-2");
        expect(this.result.getId()).andReturn("engine-2");
        expect(this.result.getId()).andReturn("resource-2-1");
        expect(this.result.getId()).andReturn("resource-2-2");
        expect(this.manager.search(isA(Query.class), eq(60000L), isA(Collection.class), eq(false))).andReturn(this.result);
        expect(this.result.getId()).andReturn("resource-1-1");
        expect(this.result.getId()).andReturn("resource-1-2");
        expect(this.result.getId()).andReturn("resource-2-1");
        expect(this.result.getId()).andReturn("resource-2-2");
        this.result.removeChild(this.result);
        expectLastCall().times(3);
        replay(this.manager, this.saxStrategy, this.result);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("resources", Collections.singleton("resource-2-1"));
        this.generator.setModel(model);
        this.generator.doSearch("query");
        verify(this.manager, this.saxStrategy, this.result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoSearchParameters() {
        expect(this.manager.describe(isA(Query.class), (Collection<String>) isNull())).andReturn(this.result);
        expect(this.result.getChildren()).andReturn(Arrays.asList(new Result[] { this.result, this.result })).times(6);
        expect(this.result.getId()).andReturn("engine-1");
        expect(this.result.getId()).andReturn("resource-1-1");
        expect(this.result.getId()).andReturn("resource-1-2");
        expect(this.result.getId()).andReturn("engine-2");
        expect(this.result.getId()).andReturn("resource-2-1");
        expect(this.result.getId()).andReturn("resource-2-2");
        Capture<Collection<String>> list = new Capture<Collection<String>>();
        expect(this.manager.search(isA(Query.class), eq(60000L), capture(list), eq(false))).andReturn(this.result);
        expect(this.result.getId()).andReturn("resource-1-1");
        expect(this.result.getId()).andReturn("resource-1-2");
        expect(this.result.getId()).andReturn("resource-2-1");
        expect(this.result.getId()).andReturn("resource-2-2");
        this.result.removeChild(this.result);
        expectLastCall().times(2);
        replay(this.manager, this.saxStrategy, this.result);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, "query"));
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("resource-list", "resource-1-2,resource-2-1,foo");
        this.generator.setParameters(parameters);
        this.generator.doSearch("query");
        assertTrue(list.getValue().contains("engine-1"));
        assertTrue(list.getValue().contains("engine-2"));
        verify(this.manager, this.saxStrategy, this.result);
    }

    @Test
    public void testSetParametersNullResourceList() {
        replay(this.manager, this.saxStrategy, this.result);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, "query"));
        try {
            this.generator.setParameters(Collections.<String, String> emptyMap());
        } catch (LanewebException e) {
        }
        verify(this.manager, this.saxStrategy, this.result);
    }
}
