package edu.stanford.irt.laneweb.metasearch;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import edu.stanford.irt.laneweb.resource.PagingData;
import edu.stanford.irt.laneweb.resource.PagingList;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.Result;

public class ClinicalSearchResultsFactory {

    private static final int RESULTS_PER_PAGE = 20;

    private ContentResultConversionStrategy conversionStrategy;

    public ClinicalSearchResultsFactory(final ContentResultConversionStrategy conversionStrategy) {
        this.conversionStrategy = conversionStrategy;
    }

    public ClinicalSearchResults createResults(final Result result, final String query, final Collection<String> facets,
            final int page) {
        List<Result> resourceResults = result
                .getChildren()
                .stream()
                .filter((final Result r) -> r.getStatus() == SearchStatus.SUCCESSFUL)
                .flatMap((final Result r) -> r.getChildren().stream())
                .filter((final Result r) -> r.getStatus() == SearchStatus.SUCCESSFUL)
                .toList();
        List<SearchResult> results = this.conversionStrategy.convertResult(result);
        int total = results.size();
        if (!facets.isEmpty()) {
            List<Result> facetResult = result.getChildren()
                    .stream()
                    .filter((final Result r) -> r.getStatus() == SearchStatus.SUCCESSFUL)
                    .flatMap((final Result r) -> r.getChildren().stream())
                    .filter((final Result r) -> r.getStatus() == SearchStatus.SUCCESSFUL)
                    .filter((final Result r) -> facets.contains(r.getId()))
                    .toList();
            results = this.conversionStrategy.convertResults(facetResult, query);
        }
        Collections.sort(results);
        PagingList<SearchResult> searchResults = new PagingList<>(results,
                new PagingData(results, page, "", RESULTS_PER_PAGE, Integer.MAX_VALUE));
        return new ClinicalSearchResults(resourceResults, searchResults, total);
    }
}
