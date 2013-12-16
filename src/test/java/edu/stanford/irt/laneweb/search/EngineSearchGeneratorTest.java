package edu.stanford.irt.laneweb.search;

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

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.DefaultResult;

public class EngineSearchGeneratorTest {

    private EngineSearchGenerator generator;

    private MetaSearchManager<DefaultResult> metaSearchManager;

    private SAXStrategy<DefaultResult> saxStrategy;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.metaSearchManager = createMock(MetaSearchManager.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new EngineSearchGenerator(this.metaSearchManager, this.saxStrategy);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoSearchString() {
        expect(this.metaSearchManager.search(isA(Query.class), eq(60000L), (Collection<String>) isNull(), eq(false))).andReturn(
                null);
        replay(this.metaSearchManager, this.saxStrategy);
        this.generator.doSearch("query");
        verify(this.metaSearchManager, this.saxStrategy);
    }

    @Test
    public void testDoSearchStringEngines() {
        expect(
                this.metaSearchManager.search(isA(Query.class), eq(60000L), eq(Arrays.asList(new String[] { "a", "b", "c" })),
                        eq(false))).andReturn(null);
        replay(this.metaSearchManager, this.saxStrategy);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.ENGINES,
                Arrays.asList(new String[] { "a", "b", "c" })));
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.ENGINES, "a,b,c,d"));
        this.generator.doSearch("query");
        verify(this.metaSearchManager, this.saxStrategy);
    }

    @Test
    public void testDoSearchStringParameterEngines() {
        expect(
                this.metaSearchManager.search(isA(Query.class), eq(60000L), eq(Arrays.asList(new String[] { "a", "b", "c" })),
                        eq(false))).andReturn(null);
        replay(this.metaSearchManager, this.saxStrategy);
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.ENGINES, "a,b,c"));
        this.generator.doSearch("query");
        verify(this.metaSearchManager, this.saxStrategy);
    }

    @Test
    public void testSetParametersEnginesNull() {
        replay(this.metaSearchManager, this.saxStrategy);
        this.generator.setModel(Collections.<String, Object> emptyMap());
        try {
            this.generator.setParameters(Collections.<String, String> emptyMap());
        } catch (LanewebException e) {
        }
        verify(this.metaSearchManager, this.saxStrategy);
    }
}
