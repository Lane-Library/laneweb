package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.isNull;
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
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.impl.Result;

public class EngineSearchGeneratorTest {

    private EngineSearchGenerator generator;

    private MetaSearchManager manager;

    private SAXStrategy<Result> saxStrategy;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.manager = createMock(MetaSearchManager.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new EngineSearchGenerator(this.manager, this.saxStrategy);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoSearchString() {
        expect(this.manager.search(isA(Query.class), isNull(Collection.class), eq(60000L))).andReturn(null);
        replay(this.manager, this.saxStrategy);
        this.generator.doSearch("query");
        verify(this.manager, this.saxStrategy);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoSearchStringEngines() {
        expect(this.manager.search(isA(Query.class), isA(Collection.class), eq(60000L))).andReturn(null);
        replay(this.manager, this.saxStrategy);
        Map<String, Object> model = new HashMap<>();
        model.put(Model.QUERY, "query");
        model.put(Model.ENGINES, Arrays.asList(new String[] { "a", "b", "c" }));
        this.generator.setModel(model);
        this.generator.setParameters(Collections.singletonMap(Model.ENGINES, "a,b,c,d"));
        this.generator.doSearch("query");
        verify(this.manager, this.saxStrategy);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoSearchStringParameterEngines() {
        expect(this.manager.search(isA(Query.class), isA(Collection.class), eq(60000L))).andReturn(null);
        replay(this.manager, this.saxStrategy);
        this.generator.setParameters(Collections.singletonMap(Model.ENGINES, "a,b,c"));
        this.generator.doSearch("query");
        verify(this.manager, this.saxStrategy);
    }

    @Test
    public void testSetParametersEnginesNull() {
        replay(this.manager, this.saxStrategy);
        this.generator.setModel(Collections.singletonMap(Model.QUERY, "query"));
        try {
            this.generator.setParameters(Collections.emptyMap());
        } catch (NullPointerException e) {
        }
        verify(this.manager, this.saxStrategy);
    }
}
