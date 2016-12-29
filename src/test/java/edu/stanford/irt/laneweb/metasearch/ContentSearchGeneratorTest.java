package edu.stanford.irt.laneweb.metasearch;

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
import edu.stanford.irt.laneweb.mapping.PagingSearchResultList;
import edu.stanford.irt.laneweb.metasearch.ContentResultConversionStrategy;
import edu.stanford.irt.laneweb.metasearch.ContentSearchGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.MetaSearchManager;

public class ContentSearchGeneratorTest {

    private ContentResultConversionStrategy conversionStrategy;

    private ContentSearchGenerator generator;

    private MetaSearchManager metasearchManager;

    private Map<String, Object> model;

    private SAXStrategy<PagingSearchResultList> saxStrategy;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.metasearchManager = createMock(MetaSearchManager.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.conversionStrategy = createMock(ContentResultConversionStrategy.class);
        this.generator = new ContentSearchGenerator(this.metasearchManager, this.saxStrategy, this.conversionStrategy);
        this.model = new HashMap<>();
        this.model.put(Model.QUERY, "query");
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testdoSearch() {
        expect(this.metasearchManager.search(isA(Query.class), isNull(Collection.class), eq(20000L))).andReturn(null);
        expect(this.conversionStrategy.convertResult(null)).andReturn(Collections.emptyList());
        replay(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
        this.generator.doSearch("query");
        verify(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testdoSearchEngines() {
        this.model.put(Model.TIMEOUT, "1000");
        this.generator.setModel(this.model);
        expect(this.metasearchManager.search(isA(Query.class), isA(Collection.class), eq(1000L))).andReturn(null);
        expect(this.conversionStrategy.convertResult(null)).andReturn(Collections.emptyList());
        replay(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
        this.generator.setParameters(Collections.singletonMap(Model.ENGINES, "a,b,c"));
        this.generator.doSearch("query");
        verify(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testdoSearchTimeout() {
        expect(this.metasearchManager.search(isA(Query.class), isA(Collection.class), eq(1000L))).andReturn(null);
        expect(this.conversionStrategy.convertResult(null)).andReturn(Collections.emptyList());
        replay(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
        this.model.put(Model.TIMEOUT, "1000");
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        verify(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testdoSearchTimeoutNFE() {
        expect(this.metasearchManager.search(isA(Query.class), isA(Collection.class), eq(20000L))).andReturn(null);
        expect(this.conversionStrategy.convertResult(null)).andReturn(Collections.emptyList());
        replay(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
        this.model.put(Model.TIMEOUT, "foo");
        this.generator.setModel(this.model);
        this.generator.doSearch("query");
        verify(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testdoSearchTimeoutParameter() {
        expect(this.metasearchManager.search(isA(Query.class), isA(Collection.class), eq(1000L))).andReturn(null);
        expect(this.conversionStrategy.convertResult(null)).andReturn(Collections.emptyList());
        replay(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
        this.generator.setModel(this.model);
        this.generator.setParameters(Collections.singletonMap(Model.TIMEOUT, "1000"));
        this.generator.doSearch("query");
        verify(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
    }
}
