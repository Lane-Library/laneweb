package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.Result;

public class EngineSearchGeneratorTest {

    private EngineSearchGenerator generator;

    private MetaSearchManager manager;

    private Result result;

    @Before
    public void setUp() throws Exception {
        this.generator = new EngineSearchGenerator();
        this.manager = createMock(MetaSearchManager.class);
        MetaSearchManagerSource msms = createMock(MetaSearchManagerSource.class);
        expect(msms.getMetaSearchManager()).andReturn(this.manager);
        replay(msms);
        this.generator.setMetaSearchManagerSource(msms);
        verify(msms);
        this.result = createMock(Result.class);
    }

    @Test
    public void testDoSearch() {
        expect(this.manager.search(isA(Query.class), eq(60000L), eq(Collections.singletonList("engine")), eq(false))).andReturn(
                this.result);
        replay(this.manager, this.result);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, "query"));
        this.generator.setParameters(Collections.singletonMap("engines", "engine"));
        assertSame(this.result, this.generator.doSearch());
        verify(this.manager, this.result);
    }

    @Test
    public void testDoSearchEnginesInModel() {
        expect(this.manager.search(isA(Query.class), eq(60000L), eq(Collections.singletonList("model-engine")), eq(false)))
                .andReturn(this.result);
        replay(this.manager, this.result);
        Map<String, Object> model = new HashMap<String, Object>();
        model.put(Model.QUERY, "query");
        model.put(Model.ENGINES, Collections.singletonList("model-engine"));
        this.generator.setModel(model);
        this.generator.setParameters(Collections.singletonMap(Model.ENGINES, "parameter-engine"));
        assertSame(this.result, this.generator.doSearch());
        verify(this.manager, this.result);
    }

    @Test
    public void testDoSearchNullEngines() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.QUERY, "query"));
        try {
            this.generator.setParameters(Collections.<String, String> emptyMap());
            fail();
        } catch (LanewebException e) {
        }
    }
}
