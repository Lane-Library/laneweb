package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.mock;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.resource.PagingList;
import edu.stanford.irt.search.impl.Result;

public class ClinicalSearchResultsTest {

    private List<Result> resourceResults;

    private ClinicalSearchResults results;

    private PagingList<SearchResult> searchResults;

    private int total;

    @BeforeEach
    public void setUp() {
        this.resourceResults = Collections.emptyList();
        this.searchResults = mock(PagingList.class);
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
