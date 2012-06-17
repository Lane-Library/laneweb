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

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.SimpleQuery;

public class SearchGeneratorTest {

    private SearchGenerator generator;

    private MetaSearchManager manager;

    private Map<String, Object> model;

    private Result result;

    private MetaSearchManagerSource source;

    @Before
    public void setUp() {
        this.generator = new SearchGenerator();
        this.source = createMock(MetaSearchManagerSource.class);
        this.manager = createMock(MetaSearchManager.class);
        this.result = createMock(Result.class);
        this.model = new HashMap<String, Object>();
        this.model.put(Model.QUERY, "query");
    }

    @Test
    public void testDoSearch() {
        expect(this.source.getMetaSearchManager()).andReturn(this.manager);
        expect(this.manager.search(isA(SimpleQuery.class), eq(60000L), (Collection<String>) isNull(), eq(false)))
                .andReturn(this.result);
        replay(this.source, this.manager);
        this.generator.setMetaSearchManagerSource(this.source);
        this.generator.setModel(this.model);
        this.generator.doSearch();
        verify(this.source, this.manager);
    }

    @Test
    public void testDoSearchEmptyQuery() {
        expect(this.source.getMetaSearchManager()).andReturn(this.manager);
        // expect(this.manager.search(isA(SimpleQuery.class), eq(60000L),
        // (Collection<String>) isNull(), eq(false))).andReturn(this.result);
        replay(this.source, this.manager);
        this.generator.setMetaSearchManagerSource(this.source);
        this.generator.query = "";
        this.generator.doSearch();
        verify(this.source, this.manager);
    }

    @Test
    public void testDoSearchNullQuery() {
        expect(this.source.getMetaSearchManager()).andReturn(this.manager);
        // expect(this.manager.search(isA(SimpleQuery.class), eq(60000L),
        // (Collection<String>) isNull(), eq(false))).andReturn(this.result);
        replay(this.source, this.manager);
        this.generator.setMetaSearchManagerSource(this.source);
        this.generator.query = null;
        this.generator.doSearch();
        verify(this.source, this.manager);
    }

    @Test
    public void testDoSearchNumberFormatException() {
        expect(this.source.getMetaSearchManager()).andReturn(this.manager);
        expect(this.manager.search(isA(SimpleQuery.class), eq(60000L), (Collection<String>) isNull(), eq(false)))
                .andReturn(this.result);
        replay(this.source, this.manager);
        this.generator.setMetaSearchManagerSource(this.source);
        this.generator.setModel(this.model);
        this.generator.setParameters(Collections.singletonMap("timeout", "foo"));
        this.generator.doSearch();
        verify(this.source, this.manager);
    }

    @Test
    public void testDoSearchSynchronous() {
        expect(this.source.getMetaSearchManager()).andReturn(this.manager);
        expect(this.manager.search(isA(SimpleQuery.class), eq(60000L), (Collection<String>) isNull(), eq(true))).andReturn(this.result);
        replay(this.source, this.manager);
        this.generator.setMetaSearchManagerSource(this.source);
        this.model.put("synchronous", "true");
        this.generator.setModel(this.model);
        this.generator.doSearch();
        verify(this.source, this.manager);
    }

    @Test
    public void testDoSearchSynchronousEmpty() {
        expect(this.source.getMetaSearchManager()).andReturn(this.manager);
        expect(this.manager.search(isA(SimpleQuery.class), eq(60000L), (Collection<String>) isNull(), eq(false)))
                .andReturn(this.result);
        replay(this.source, this.manager);
        this.generator.setMetaSearchManagerSource(this.source);
        this.generator.setParameters(Collections.singletonMap("synchronous", ""));
        this.generator.setModel(this.model);
        this.generator.doSearch();
        verify(this.source, this.manager);
    }

    @Test
    public void testDoSearchTimeout() {
        expect(this.source.getMetaSearchManager()).andReturn(this.manager);
        expect(this.manager.search(isA(SimpleQuery.class), eq(10L), (Collection<String>) isNull(), eq(false))).andReturn(
                this.result);
        replay(this.source, this.manager);
        this.generator.setMetaSearchManagerSource(this.source);
        this.model.put("timeout", "10");
        this.generator.setModel(this.model);
        this.generator.doSearch();
        verify(this.source, this.manager);
    }

    @Test
    public void testDoSearchWait() {
        expect(this.source.getMetaSearchManager()).andReturn(this.manager);
        expect(this.manager.search(isA(SimpleQuery.class), eq(60000L), (Collection<String>) isNull(), eq(false)))
                .andReturn(this.result);
        expect(this.result.getStatus()).andReturn(SearchStatus.RUNNING);
        replay(this.source, this.manager, this.result);
        this.generator.setMetaSearchManagerSource(this.source);
        this.model.put("wait", "10");
        this.generator.setModel(this.model);
        this.generator.doSearch();
        verify(this.source, this.manager, this.result);
    }

    @Test
    public void testDoSearchWaitNumberFormatException() {
        expect(this.source.getMetaSearchManager()).andReturn(this.manager);
        expect(this.manager.search(isA(SimpleQuery.class), eq(60000L), (Collection<String>) isNull(), eq(false)))
                .andReturn(this.result);
        // expect(this.result.getStatus()).andReturn(SearchStatus.RUNNING);
        replay(this.source, this.manager, this.result);
        this.generator.setMetaSearchManagerSource(this.source);
        this.model.put("wait", "foo");
        this.generator.setModel(this.model);
        this.generator.doSearch();
        verify(this.source, this.manager, this.result);
    }
}
