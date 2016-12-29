package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.createMock;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.metasearch.ClinicalSearchResults;
import edu.stanford.irt.laneweb.metasearch.SearchResult;
import edu.stanford.irt.laneweb.resource.PagingList;
import edu.stanford.irt.search.impl.Result;

public class ClinicalSearchResultsTest {

    private List<Result> resourceResults;

    private ClinicalSearchResults results;

    private PagingList<SearchResult> searchResults;

    private int total;

    @Before
    public void setUp() {
        this.resourceResults = Collections.emptyList();
        this.searchResults = createMock(PagingList.class);
        this.total = 10;
        this.results = new ClinicalSearchResults(this.resourceResults, this.searchResults, this.total);
    }

    @Test
    public void testGetResourceResults() {
        assertEquals(this.resourceResults, this.results.getResourceResults());
    }

    @Test
    public void testGetSearchResults() {
        assertEquals(this.searchResults, this.results.getSearchResults());
    }

    @Test
    public void testGetTotal() {
        assertEquals(this.total, this.results.getTotal());
    }
}
