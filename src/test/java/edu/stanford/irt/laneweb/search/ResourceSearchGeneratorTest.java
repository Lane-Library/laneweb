package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.Result;

public class ResourceSearchGeneratorTest {

    private ResourceSearchGenerator generator;

    private MetaSearchManager manager;

    private MetaSearchManagerSource msms;

    private Result result;

    @Before
    public void setUp() throws Exception {
        this.generator = new ResourceSearchGenerator();
        this.msms = createMock(MetaSearchManagerSource.class);
        this.manager = createMock(MetaSearchManager.class);
        this.result = createMock(Result.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoSearch() {
        expect(this.msms.getMetaSearchManager()).andReturn(this.manager);
        expect(this.manager.describe(isA(Query.class), (Collection<String>) isNull())).andReturn(this.result);
        expect(this.result.getChildren()).andReturn(Arrays.asList(new Result[] { this.result, this.result })).times(3);
        expect(this.result.getId()).andReturn("engine-1");
        expect(this.result.getId()).andReturn("resource-1-1");
        expect(this.result.getId()).andReturn("resource-1-2");
        expect(this.result.getId()).andReturn("engine-2");
        expect(this.result.getId()).andReturn("resource-2-1");
        expect(this.result.getId()).andReturn("resource-2-2");
        expect(this.manager.search(isA(Query.class), eq(60000L), eq(Collections.singleton("engine-2")), eq(false))).andReturn(
                this.result);
        replay(this.manager, this.msms, this.result);
        this.generator.setMetaSearchManagerSource(this.msms);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(Model.QUERY, "query");
        model.put("resources", Collections.singleton("resource-2-1"));
        this.generator.setParameters(Collections.<String, String> emptyMap());
        this.generator.setModel(model);
        this.generator.doSearch();
        verify(this.manager, this.msms, this.result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoSearchNullQUery() {
        expect(this.msms.getMetaSearchManager()).andReturn(this.manager);
        expect(this.manager.describe(isA(Query.class), (Collection<String>) isNull())).andReturn(this.result);
        expect(this.result.getChildren()).andReturn(Arrays.asList(new Result[] { this.result, this.result })).times(3);
        expect(this.result.getId()).andReturn("engine-1");
        expect(this.result.getId()).andReturn("resource-1-1");
        expect(this.result.getId()).andReturn("resource-1-2");
        expect(this.result.getId()).andReturn("engine-2");
        expect(this.result.getId()).andReturn("resource-2-1");
        expect(this.result.getId()).andReturn("resource-2-2");
        replay(this.manager, this.msms, this.result);
        this.generator.setMetaSearchManagerSource(this.msms);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("resources", Collections.singleton("resource-2-1"));
        this.generator.setModel(model);
        this.generator.doSearch();
        verify(this.manager, this.msms, this.result);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoSearchParameters() {
        expect(this.msms.getMetaSearchManager()).andReturn(this.manager);
        expect(this.manager.describe(isA(Query.class), (Collection<String>) isNull())).andReturn(this.result);
        expect(this.result.getChildren()).andReturn(Arrays.asList(new Result[] { this.result, this.result })).times(3);
        expect(this.result.getId()).andReturn("engine-1");
        expect(this.result.getId()).andReturn("resource-1-1");
        expect(this.result.getId()).andReturn("resource-1-2");
        expect(this.result.getId()).andReturn("engine-2");
        expect(this.result.getId()).andReturn("resource-2-1");
        expect(this.result.getId()).andReturn("resource-2-2");
        Capture<Collection<String>> list = new Capture<Collection<String>>();
        expect(this.manager.search(isA(Query.class), eq(60000L), capture(list), eq(false))).andReturn(this.result);
        replay(this.manager, this.msms, this.result);
        this.generator.setMetaSearchManagerSource(this.msms);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, "query"));
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("resource-list", "resource-1-2,resource-2-1,foo");
        this.generator.setParameters(parameters);
        this.generator.doSearch();
        assertTrue(list.getValue().contains("engine-1"));
        assertTrue(list.getValue().contains("engine-2"));
        verify(this.manager, this.msms, this.result);
    }

    @Test
    public void testSetParametersNullResourceList() {
        expect(this.msms.getMetaSearchManager()).andReturn(this.manager);
        replay(this.manager, this.msms, this.result);
        this.generator.setMetaSearchManagerSource(this.msms);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, "query"));
        try {
            this.generator.setParameters(Collections.<String, String> emptyMap());
        } catch (LanewebException e) {
        }
        verify(this.manager, this.msms, this.result);
    }
}
