package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;

import java.util.Collection;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.Result;

public class DescribeGeneratorTest {

    private DescribeGenerator generator;

    private MetaSearchManager metaSearchManager;

    private MetaSearchManagerSource metaSearchManagerSource;

    private Map<String, Object> model;

    private Result result;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.generator = new DescribeGenerator();
        this.metaSearchManagerSource = createMock(MetaSearchManagerSource.class);
        this.metaSearchManager = createMock(MetaSearchManager.class);
        expect(this.metaSearchManagerSource.getMetaSearchManager()).andReturn(this.metaSearchManager);
        replay(this.metaSearchManagerSource);
        this.generator.setMetaSearchManagerSource(this.metaSearchManagerSource);
        verify(this.metaSearchManagerSource);
        this.model = createMock(Map.class);
        this.result = createMock(Result.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoSearch() {
        expect(this.metaSearchManager.describe(isA(Query.class), (Collection<String>) isNull())).andReturn(this.result);
        replay(this.metaSearchManager);
        assertSame(this.result, this.generator.doSearch());
        verify(this.metaSearchManager);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoSearchEnginesQuery() {
        expect(this.model.get(Model.QUERY)).andReturn("query");
        expect(this.model.containsKey(Model.ENGINES)).andReturn(true);
        expect(this.model.get(Model.ENGINES)).andReturn(new String[] { "engine" });
        replay(this.model);
        this.generator.setModel(this.model);
        verify(this.model);
        expect(this.metaSearchManager.describe(isA(Query.class), isA(Collection.class))).andReturn(this.result);
        replay(this.metaSearchManager);
        assertSame(this.result, this.generator.doSearch());
        verify(this.metaSearchManager);
    }

    @Test
    public void testSetModel() {
        expect(this.model.get(Model.QUERY)).andReturn(null);
        expect(this.model.containsKey(Model.ENGINES)).andReturn(false);
        replay(this.model);
        this.generator.setModel(this.model);
        verify(this.model);
    }
}
