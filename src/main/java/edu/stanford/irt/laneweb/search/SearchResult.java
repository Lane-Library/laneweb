package edu.stanford.irt.laneweb.search;

/**
 * @author ryanmax
 */
public interface SearchResult extends Comparable<SearchResult> {

    /**
     * @return the search score
     */
    int getScore();

    /**
     * @return the sortTitle
     */
    String getSortTitle();
    
    /**
     * @return whether or not there is additional text
     */
    boolean hasAdditionalText();
}
