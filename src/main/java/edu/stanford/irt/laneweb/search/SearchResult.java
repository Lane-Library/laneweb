package edu.stanford.irt.laneweb.search;

import java.util.regex.Pattern;

import edu.stanford.irt.laneweb.resource.Resource;

/**
 * @author ryanmax
 */
public interface SearchResult extends Resource, Comparable<SearchResult> {

    Pattern NON_FILING_PATTERN = Pattern.compile("^(a|an|the) ", Pattern.CASE_INSENSITIVE);

    Pattern WHITESPACE = Pattern.compile("\\W");

    /**
     * @return the search score
     */
    int getScore();

    /**
     * @return the sortTitle
     */
    String getSortTitle();
}
