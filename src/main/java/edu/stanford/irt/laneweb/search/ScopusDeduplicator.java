package edu.stanford.irt.laneweb.search;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ScopusDeduplicator {

    public void removeDuplicates(final Set<ContentResultSearchResult> searchResults) {
        List<ContentResultSearchResult> scopusResults = new LinkedList<ContentResultSearchResult>();
        for (ContentResultSearchResult searchResult : searchResults) {
            if ("scopus".equals(searchResult.getResourceResult().getId())) {
                scopusResults.add(searchResult);
            }
        }
        for (ContentResultSearchResult scopusResult : scopusResults) {
            if (isDuplicate(scopusResult, searchResults)) {
                searchResults.remove(scopusResult);
            }
        }
    }

    private boolean isDuplicate(final ContentResultSearchResult scopusResult,
            final Set<ContentResultSearchResult> searchResults) {
        boolean isDuplicate = false;
        String scopusSortTitle = scopusResult.getSortTitle();
        for (ContentResultSearchResult searchResult : searchResults) {
            if (!"scopus".equals(searchResult.getResourceResult().getId())
                    && scopusSortTitle.equals(searchResult.getSortTitle())) {
                isDuplicate = true;
                break;
            }
        }
        return isDuplicate;
    }
}
