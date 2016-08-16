package edu.stanford.irt.laneweb.search;

import java.util.List;

import edu.stanford.irt.laneweb.resource.PagingList;
import edu.stanford.irt.search.impl.Result;

public class ClinicalSearchResults {

    private List<Result> resourceResults;

    private PagingList<SearchResult> searchResults;

    private int total;

    public ClinicalSearchResults(final List<Result> resourceResults, final PagingList<SearchResult> searchResults,
            final int total) {
        this.resourceResults = resourceResults;
        this.searchResults = searchResults;
        this.total = total;
    }

    public List<Result> getResourceResults() {
        return this.resourceResults;
    }

    public PagingList<SearchResult> getSearchResults() {
        return this.searchResults;
    }

    public int getTotal() {
        return this.total;
    }
}
