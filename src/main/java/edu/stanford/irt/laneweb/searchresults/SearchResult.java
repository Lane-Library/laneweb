/**
 * 
 */
package edu.stanford.irt.laneweb.searchresults;

import edu.stanford.irt.laneweb.Resource;

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

    /**
     * @return the dedupTitle
     */
    String getDedupTitle();
    
}
