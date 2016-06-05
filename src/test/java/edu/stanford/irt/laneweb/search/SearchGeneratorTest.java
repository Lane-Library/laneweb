package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

public class SearchGeneratorTest {

    private SearchGenerator generator;

    private MetaSearchManager manager;

    private Map<String, Object> model;

    private Result result;

    private SAXStrategy<Result> saxStrategy;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.manager = createMock(MetaSearchManager.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new SearchGenerator(this.manager, this.saxStrategy);
        this.result = createMock(Result.class);
        this.model = new HashMap<String, Object>();
        this.model.put(Model.QUERY, "query");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoSearch() {
        expect(this.manager.search(isA(SimpleQuery.class), isNull(Collection.class), eq(60000L)))
                .andReturn(this.result);
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

    @SuppressWarnings("unchecked")
    @Test
    public void testDoSearchNumberFormatException() {
        expect(this.manager.search(isA(SimpleQuery.class), isNull(Collection.class), eq(60000L)))
                .andReturn(this.result);
        replay(this.saxStrategy, this.manager);
        this.generator.setModel(this.model);
        this.generator.setParameters(Collections.singletonMap("timeout", "foo"));
        this.generator.doSearch("query");
        verify(this.saxStrategy, this.manager);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoSearchTimeout() {
        expect(this.manager.search(isA(SimpleQuery.class), isNull(Collection.class), eq(10L))).andReturn(this.result);
        replay(this.saxStrategy, this.manager);
        this.model.put("timeout", "10");
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        verify(this.saxStrategy, this.manager);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoSearchWaitNumberFormatException() {
        expect(this.manager.search(isA(SimpleQuery.class), isNull(Collection.class), eq(60000L)))
                .andReturn(this.result);
        replay(this.saxStrategy, this.manager, this.result);
        this.model.put("wait", "foo");
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        verify(this.saxStrategy, this.manager, this.result);
    }
}
