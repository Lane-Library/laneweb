package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.impl.Result;

public class EngineSearchGeneratorTest {

    private EngineSearchGenerator generator;

    private MetaSearchService metaSearchService;

    private SAXStrategy<Result> saxStrategy;

    @BeforeEach
    public void setUp() throws Exception {
        this.metaSearchService = mock(MetaSearchService.class);
        this.saxStrategy = mock(SAXStrategy.class);
        this.generator = new EngineSearchGenerator(this.metaSearchService, this.saxStrategy);
    }

    @Test
    public void testDoSearchString() {
        expect(this.metaSearchService.search("query", null, 60000L)).andReturn(null);
        replay(this.metaSearchService, this.saxStrategy);
        this.generator.doSearch("query");
        verify(this.metaSearchService, this.saxStrategy);
    }

    @Test
    public void testDoSearchStringEngines() {
        expect(this.metaSearchService.search("query", Arrays.asList(new String[] { "a", "b", "c" }), 60000L))
                .andReturn(null);
        replay(this.metaSearchService, this.saxStrategy);
        Map<String, Object> model = new HashMap<>();
        model.put(Model.QUERY, "query");
        model.put(Model.ENGINES, Arrays.asList(new String[] { "a", "b", "c" }));
        this.generator.setModel(model);
        this.generator.setParameters(Collections.singletonMap(Model.ENGINES, "a,b,c,d"));
        this.generator.doSearch("query");
        verify(this.metaSearchService, this.saxStrategy);
    }

    @Test
    public void testDoSearchStringParameterEngines() {
        expect(this.metaSearchService.search("query", Arrays.asList(new String[] { "a", "b", "c" }), 60000L))
                .andReturn(null);
        replay(this.metaSearchService, this.saxStrategy);
        this.generator.setParameters(Collections.singletonMap(Model.ENGINES, "a,b,c"));
        this.generator.doSearch("query");
        verify(this.metaSearchService, this.saxStrategy);
    }

    @Test
    public void testSetParametersEnginesNull() {
        replay(this.metaSearchService, this.saxStrategy);
        this.generator.setModel(Collections.singletonMap(Model.QUERY, "query"));
        try {
            this.generator.setParameters(Collections.emptyMap());
        } catch (NullPointerException e) {
        }
        verify(this.metaSearchService, this.saxStrategy);
    }
}
