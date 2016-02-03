package edu.stanford.irt.laneweb.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ScopusDeduplicator {

    public void removeDuplicates(final Collection<ContentResultSearchResult> searchResults) {
        // create a list of scopus results
        List<ContentResultSearchResult> scopusResults = new ArrayList<>();
        for (ContentResultSearchResult searchResult : searchResults) {
            if ("scopus".equals(searchResult.getResourceResult().getId())) {
                scopusResults.add(searchResult);
            }
        }
        // go through the list of scopus results and remove ones that are duplicates
        for (ContentResultSearchResult scopusResult : scopusResults) {
            if (isDuplicate(scopusResult, searchResults)) {
                searchResults.remove(scopusResult);
            }
        }
    }

    private boolean isDuplicate(final ContentResultSearchResult scopusResult,
            final Collection<ContentResultSearchResult> searchResults) {
        boolean isDuplicate = false;
        for (ContentResultSearchResult searchResult : searchResults) {
            if (!"scopus".equals(searchResult.getResourceResult().getId())
                    && scopusResult.getContentResult().isSameArticle(searchResult.getContentResult())) {
                isDuplicate = true;
                break;
            }
        }
        return isDuplicate;
    }
}
