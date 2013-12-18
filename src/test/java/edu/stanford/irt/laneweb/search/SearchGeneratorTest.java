package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.LegacyMetaSearch;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class SearchGeneratorTest {

    private SearchGenerator generator;

    private LegacyMetaSearch manager;

    private Map<String, Object> model;

    private Result result;

    private SAXStrategy<Result> saxStrategy;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.manager = createMock(LegacyMetaSearch.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new SearchGenerator(this.manager, this.saxStrategy);
        this.result = createMock(Result.class);
        this.model = new HashMap<String, Object>();
        this.model.put(Model.QUERY, "query");
    }

    @Test
    public void testDoSearch() {
        expect(this.manager.search(isA(SimpleQuery.class), eq(60000L), eq(false))).andReturn(
                this.result);
        replay(this.saxStrategy, this.manager);
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        verify(this.saxStrategy, this.manager);
    }

    @Test
    public void testDoSearchEmptyQuery() {
        replay(this.saxStrategy, this.manager);
        this.generator.doSearch("");
        verify(this.saxStrategy, this.manager);
    }

    @Test
    public void testDoSearchNullQuery() {
        replay(this.saxStrategy, this.manager);
        this.generator.doSearch(null);
        verify(this.saxStrategy, this.manager);
    }

    @Test
    public void testDoSearchNumberFormatException() {
        expect(this.manager.search(isA(SimpleQuery.class), eq(60000L), eq(false))).andReturn(
                this.result);
        replay(this.saxStrategy, this.manager);
        this.generator.setModel(this.model);
        this.generator.setParameters(Collections.singletonMap("timeout", "foo"));
        this.generator.doSearch("query");
        verify(this.saxStrategy, this.manager);
    }

    @Test
    public void testDoSearchSynchronous() {
        expect(this.manager.search(isA(SimpleQuery.class), eq(60000L), eq(true))).andReturn(
                this.result);
        replay(this.saxStrategy, this.manager);
        this.model.put("synchronous", "true");
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        verify(this.saxStrategy, this.manager);
    }

    @Test
    public void testDoSearchSynchronousEmpty() {
        expect(this.manager.search(isA(SimpleQuery.class), eq(60000L), eq(false))).andReturn(
                this.result);
        replay(this.saxStrategy, this.manager);
        this.generator.setParameters(Collections.singletonMap("synchronous", ""));
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        verify(this.saxStrategy, this.manager);
    }

    @Test
    public void testDoSearchTimeout() {
        expect(this.manager.search(isA(SimpleQuery.class), eq(10L), eq(false))).andReturn(
                this.result);
        replay(this.saxStrategy, this.manager);
        this.model.put("timeout", "10");
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        verify(this.saxStrategy, this.manager);
    }

    @Test
    public void testDoSearchWait() {
        expect(this.manager.search(isA(SimpleQuery.class), eq(60000L), eq(false))).andReturn(
                this.result);
        expect(this.result.getStatus()).andReturn(SearchStatus.RUNNING);
        replay(this.saxStrategy, this.manager, this.result);
        this.model.put("wait", "10");
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        verify(this.saxStrategy, this.manager, this.result);
    }

    @Test
    public void testDoSearchWaitNumberFormatException() {
        expect(this.manager.search(isA(SimpleQuery.class), eq(60000L), eq(false))).andReturn(
                this.result);
        replay(this.saxStrategy, this.manager, this.result);
        this.model.put("wait", "foo");
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        verify(this.saxStrategy, this.manager, this.result);
    }
}
