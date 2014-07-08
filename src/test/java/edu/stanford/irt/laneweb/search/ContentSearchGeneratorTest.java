package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.impl.Result;

public class ContentSearchGeneratorTest {

    private ContentResultConversionStrategy conversionStrategy;

    private ContentSearchGenerator generator;

    private MetaSearchManager metasearchManager;

    private SAXStrategy<PagingSearchResultList> saxStrategy;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.metasearchManager = createMock(MetaSearchManager.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.conversionStrategy = createMock(ContentResultConversionStrategy.class);
        this.generator = new ContentSearchGenerator(this.metasearchManager, this.saxStrategy, this.conversionStrategy);
    }

    @Test
    public void testGetSearchResults() {
        expect(this.metasearchManager.search(isA(Query.class), eq(20000L), eq(true)))
                .andReturn(null);
        expect(this.conversionStrategy.convertResult(null)).andReturn(null);
        replay(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
        this.generator.getSearchResults("query");
        verify(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
    }

    @Test(expected = LanewebException.class)
    public void testGetSearchResultsEmptyQuery() {
        expect(this.conversionStrategy.convertResult(isA(Result.class))).andReturn(null);
        replay(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
        this.generator.getSearchResults("");
        verify(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
    }

    @Test
    public void testGetSearchResultsEngines() {
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.TIMEOUT, "1000"));
        expect(
                this.metasearchManager.search(isA(Query.class), eq(1000L), eq(true))).andReturn(null);
        expect(this.conversionStrategy.convertResult(null)).andReturn(null);
        replay(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.ENGINES, "a,b,c"));
        this.generator.getSearchResults("query");
        verify(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
    }

    @Test(expected = LanewebException.class)
    public void testGetSearchResultsNullQuery() {
        expect(this.conversionStrategy.convertResult(isA(Result.class))).andReturn(null);
        replay(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
        this.generator.getSearchResults(null);
        verify(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
    }

    @Test
    public void testGetSearchResultsTimeout() {
        expect(this.metasearchManager.search(isA(Query.class), eq(1000L), eq(true)))
                .andReturn(null);
        expect(this.conversionStrategy.convertResult(null)).andReturn(null);
        replay(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.TIMEOUT, "1000"));
        this.generator.getSearchResults("query");
        verify(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
    }

    @Test
    public void testGetSearchResultsTimeoutNFE() {
        expect(this.metasearchManager.search(isA(Query.class), eq(20000L), eq(true)))
                .andReturn(null);
        expect(this.conversionStrategy.convertResult(null)).andReturn(null);
        replay(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
        this.generator.setModel(Collections.<String, Object> singletonMap(Model.TIMEOUT, "foo"));
        this.generator.getSearchResults("query");
        verify(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
    }

    @Test
    public void testGetSearchResultsTimeoutParameter() {
        expect(this.metasearchManager.search(isA(Query.class), eq(1000L), eq(true)))
                .andReturn(null);
        expect(this.conversionStrategy.convertResult(null)).andReturn(null);
        replay(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
        this.generator.setModel(Collections.<String, Object> emptyMap());
        this.generator.setParameters(Collections.<String, String> singletonMap(Model.TIMEOUT, "1000"));
        this.generator.getSearchResults("query");
        verify(this.metasearchManager, this.conversionStrategy, this.saxStrategy);
    }
}
