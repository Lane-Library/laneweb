package edu.stanford.irt.laneweb.search;

import java.util.regex.Pattern;

import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

/**
 * @author ryanmax
 */
public class SearchResult implements Comparable<SearchResult> {

    private static final Pattern NON_FILING_PATTERN = Pattern.compile("^(a|an|the) ", Pattern.CASE_INSENSITIVE);

    private static final Pattern WHITESPACE = Pattern.compile("\\W");

    private ContentResult contentResult;

    private int hashCode;

    private Result resourceResult;

    private int score;

    private String sortTitle;

    private String title;

    public SearchResult(final ContentResult contentResult, final Result resourceResult, final int score) {
        this.score = score < 0 ? 0 : score;
        this.title = contentResult.getTitle();
        this.contentResult = contentResult;
        this.resourceResult = resourceResult;
    }

    @Override
    public int compareTo(final SearchResult o) {
        int value = o.score - this.score;
        if (value == 0) {
            value = getSortTitle().compareTo(o.getSortTitle());
            if (value == 0) {
                value = this.contentResult.compareTo(o.contentResult);
            }
        }
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        boolean equals = false;
        if (object != null && getClass() == object.getClass()) {
            SearchResult other = (SearchResult) object;
            if (other.hashCode() == hashCode()) {
                equals = compareToIgnoreScore(other) == 0;
            }
        }
        return equals;
    }

    public ContentResult getContentResult() {
        return this.contentResult;
    }

    public Result getResourceResult() {
        return this.resourceResult;
    }

    public int getScore() {
        return this.score;
    }

    public String getSortTitle() {
        if (this.sortTitle == null) {
            if (this.title == null) {
                this.sortTitle = "";
            } else {
                String temp = NON_FILING_PATTERN.matcher(this.title).replaceFirst("");
                this.sortTitle = WHITESPACE.matcher(temp).replaceAll("").toLowerCase();
            }
        }
        return this.sortTitle;
    }

    public boolean hasAdditionalText() {
        String description = this.contentResult.getDescription();
        return description != null && description.length() > 0;
    }

    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = getSortTitle().hashCode();
        }
        return this.hashCode;
    }

    private int compareToIgnoreScore(final SearchResult other) {
        int value = getSortTitle().compareTo(other.getSortTitle());
        if (value == 0) {
            value = this.contentResult.compareTo(other.contentResult);
        }
        return value;
    }
}
