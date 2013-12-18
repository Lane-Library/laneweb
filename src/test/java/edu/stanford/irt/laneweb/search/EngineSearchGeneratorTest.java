package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.legacy.LegacyMetaSearch;
import edu.stanford.irt.search.legacy.Result;

public class EngineSearchGeneratorTest {

    private EngineSearchGenerator generator;

    private LegacyMetaSearch LegacyMetaSearch;

    private SAXStrategy<Result> saxStrategy;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.LegacyMetaSearch = createMock(LegacyMetaSearch.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new EngineSearchGenerator(this.LegacyMetaSearch, this.saxStrategy);
    }

    @Test
    public void testDoSearchString() {
        expect(this.LegacyMetaSearch.search(isA(Query.class), eq(60000L), eq(false))).andReturn(
                null);
        replay(this.LegacyMetaSearch, this.saxStrategy);
        this.generator.doSearch("query");
        verify(this.LegacyMetaSearch, this.saxStrategy);
    }

    @Test
    public void testDoSearchStringEngines() {
        expect(
                this.LegacyMetaSearch.search(isA(Query.class), eq(60000L),
                        eq(false))).andReturn(null);
        replay(this.LegacyMetaSearch, this.saxStrategy);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.ENGINES,
                Arrays.asList(new String[] { "a", "b", "c" })));
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.ENGINES, "a,b,c,d"));
        this.generator.doSearch("query");
        verify(this.LegacyMetaSearch, this.saxStrategy);
    }

    @Test
    public void testDoSearchStringParameterEngines() {
        expect(
                this.LegacyMetaSearch.search(isA(Query.class), eq(60000L),
                        eq(false))).andReturn(null);
        replay(this.LegacyMetaSearch, this.saxStrategy);
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.ENGINES, "a,b,c"));
        this.generator.doSearch("query");
        verify(this.LegacyMetaSearch, this.saxStrategy);
    }

    @Test
    public void testSetParametersEnginesNull() {
        replay(this.LegacyMetaSearch, this.saxStrategy);
        this.generator.setModel(Collections.<String, Object> emptyMap());
        try {
            this.generator.setParameters(Collections.<String, String> emptyMap());
        } catch (LanewebException e) {
        }
        verify(this.LegacyMetaSearch, this.saxStrategy);
    }
}
