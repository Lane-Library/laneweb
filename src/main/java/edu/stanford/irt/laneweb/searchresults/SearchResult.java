/**
 * 
 */
package edu.stanford.irt.laneweb.searchresults;

import java.util.regex.Pattern;

import edu.stanford.irt.laneweb.Resource;

/**
 * @author ryanmax
 */
public interface SearchResult extends Resource, Comparable<SearchResult> {

    public static final Pattern NON_FILING_PATTERN = Pattern.compile("(?i)^(a|an|the) ");

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
