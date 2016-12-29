package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.metasearch.ClinicalSearchResults;
import edu.stanford.irt.laneweb.metasearch.ClinicalSearchResultsFactory;
import edu.stanford.irt.laneweb.metasearch.ContentResultConversionStrategy;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

public class ClinicalSearchResultsFactoryTest {

    private ContentResult content;

    private ContentResultConversionStrategy conversionStrategy;

    private List<String> facets;

    private ClinicalSearchResultsFactory factory;

    private Result result;

    @Before
    public void setUp() {
        this.facets = new ArrayList<>();
        this.result = createMock(Result.class);
        this.content = createMock(ContentResult.class);
        this.conversionStrategy = createMock(ContentResultConversionStrategy.class);
        this.factory = new ClinicalSearchResultsFactory(this.conversionStrategy);
    }

    @Test
    public void testGetResourceResults() {
        expect(this.result.getChildren()).andReturn(Collections.singleton(this.result)).times(2);
        expect(this.result.getStatus()).andReturn(SearchStatus.SUCCESSFUL).times(2);
        expect(this.conversionStrategy.convertResult(this.result)).andReturn(Collections.singletonList(null));
        replay(this.result, this.content, this.conversionStrategy);
        ClinicalSearchResults results = this.factory.createResults(this.result, "query", this.facets, 1);
        assertNotNull(results.getResourceResults());
        verify(this.result, this.content, this.conversionStrategy);
    }

    @Test
    public void testGetSearchResults() {
        expect(this.result.getChildren()).andReturn(Collections.singleton(this.result)).times(2);
        expect(this.result.getStatus()).andReturn(SearchStatus.SUCCESSFUL).times(2);
        expect(this.conversionStrategy.convertResult(this.result)).andReturn(Collections.singletonList(null));
        replay(this.result, this.content, this.conversionStrategy);
        ClinicalSearchResults results = this.factory.createResults(this.result, "query", this.facets, 1);
        assertNotNull(results.getSearchResults());
        verify(this.result, this.content, this.conversionStrategy);
    }

    @Test
    public void testGetSearchResultsFacets() {
        expect(this.result.getChildren()).andReturn(Collections.singleton(this.result)).times(4);
        expect(this.result.getStatus()).andReturn(SearchStatus.SUCCESSFUL).times(4);
        expect(this.conversionStrategy.convertResult(this.result)).andReturn(Collections.singletonList(null));
        expect(this.result.getId()).andReturn("facet");
        expect(this.conversionStrategy.convertResults(Collections.singletonList(this.result), "query"))
                .andReturn(Collections.singletonList(null));
        replay(this.result, this.content, this.conversionStrategy);
        ClinicalSearchResults results = this.factory.createResults(this.result, "query",
                Collections.singletonList("facet"), 1);
        assertNotNull(results.getSearchResults());
        verify(this.result, this.content, this.conversionStrategy);
    }

    @Test
    public void testGetTotal() {
        expect(this.result.getChildren()).andReturn(Collections.singleton(this.result)).times(2);
        expect(this.result.getStatus()).andReturn(SearchStatus.SUCCESSFUL).times(2);
        expect(this.conversionStrategy.convertResult(this.result)).andReturn(Collections.singletonList(null));
        replay(this.result, this.content, this.conversionStrategy);
        ClinicalSearchResults results = this.factory.createResults(this.result, "query", this.facets, 1);
        assertEquals(1, results.getTotal());
        verify(this.result, this.content, this.conversionStrategy);
    }
}
