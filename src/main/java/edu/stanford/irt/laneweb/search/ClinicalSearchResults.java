package edu.stanford.irt.laneweb.search;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import edu.stanford.irt.laneweb.resource.PagingData;
import edu.stanford.irt.laneweb.resource.PagingList;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.Result;

public class ClinicalSearchResults {

    private ContentResultConversionStrategy conversionStrategy = new ContentResultConversionStrategy();

    private List<Result> resourceResults;

    private PagingList<SearchResult> searchResults;

    private int total;

    public ClinicalSearchResults(final Result result, final List<String> facets, final int page) {
        this.resourceResults = result
                .getChildren()
                .stream()
                .filter(r -> SearchStatus.SUCCESSFUL.equals(r.getStatus()))
                .flatMap(r -> r.getChildren().stream())
                .filter(r -> SearchStatus.SUCCESSFUL.equals(r.getStatus()))
                .collect(Collectors.toList());
        List<SearchResult> results = this.conversionStrategy.convertResult(result);
        this.total = results.size();
        if (facets.size() > 0) {
            List<Result> facetResult = result.getChildren()
                    .stream()
                    .filter(r -> SearchStatus.SUCCESSFUL.equals(r.getStatus()))
                    .flatMap(r -> r.getChildren().stream())
                    .filter(r -> SearchStatus.SUCCESSFUL.equals(r.getStatus()))
                    .filter(r -> facets.contains(r.getId()))
                    .collect(Collectors.toList());
            results = this.conversionStrategy.convertResults(facetResult, result.getQuery().getSearchText());
        }
        Collections.sort(results);
        this.searchResults = new PagingList<>(results, new PagingData(results, page, "", 50, Integer.MAX_VALUE));
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
