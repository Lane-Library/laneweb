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

    public static final Pattern NON_FILING_PATTERN = Pattern.compile("^(a|an|the) ",Pattern.CASE_INSENSITIVE);

    /**
     * @return the search score
     */
    int getScore();

    /**
     * @return the sortTitle
     */
    String getSortTitle();
    
}
