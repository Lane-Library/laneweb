package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.impl.Result;

public class SearchGeneratorTest {

    private SearchGenerator generator;

    private MetaSearchService metaSearchService;

    private Map<String, Object> model;

    private Result result;

    private SAXStrategy<Result> saxStrategy;

    @BeforeEach
    public void setUp() {
        this.metaSearchService = mock(MetaSearchService.class);
        this.saxStrategy = mock(SAXStrategy.class);
        this.generator = new SearchGenerator(this.metaSearchService, this.saxStrategy);
        this.result = mock(Result.class);
        this.model = new HashMap<>();
        this.model.put(Model.QUERY, "query");
    }

    @Test
    public void testDoSearch() {
        expect(this.metaSearchService.search("query", null, 60000L)).andReturn(this.result);
        replay(this.saxStrategy, this.metaSearchService);
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        verify(this.saxStrategy, this.metaSearchService);
    }

    @Test
    public void testDoSearchNumberFormatException() {
        expect(this.metaSearchService.search("query", null, 60000L)).andReturn(this.result);
        replay(this.saxStrategy, this.metaSearchService);
        this.generator.setModel(this.model);
        this.generator.setParameters(Collections.singletonMap("timeout", "foo"));
        this.generator.doSearch("query");
        verify(this.saxStrategy, this.metaSearchService);
    }

    @Test
    public void testDoSearchTimeout() {
        expect(this.metaSearchService.search("query", null, 10L)).andReturn(this.result);
        replay(this.saxStrategy, this.metaSearchService);
        this.model.put("timeout", "10");
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        verify(this.saxStrategy, this.metaSearchService);
    }

    @Test
    public void testDoSearchWaitNumberFormatException() {
        expect(this.metaSearchService.search("query", null, 60000L)).andReturn(this.result);
        replay(this.saxStrategy, this.metaSearchService, this.result);
        this.model.put("wait", "foo");
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        verify(this.saxStrategy, this.metaSearchService, this.result);
    }
}
