package edu.stanford.irt.laneweb.search;

import edu.stanford.irt.laneweb.resource.Resource;

/**
 * @author ryanmax
 */
public interface SearchResult extends Resource, Comparable<SearchResult> {

    /**
     * @return the search score
     */
    int getScore();

    /**
     * @return the sortTitle
     */
    String getSortTitle();
}
