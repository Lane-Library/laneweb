package edu.stanford.irt.laneweb.search;

import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.Result;

/**
 * @author ryanmax
 */
public class ContentResultSearchResult implements SearchResult {

    private ContentResult contentResult;

    private Result resourceResult;

    private int score;

    private String sortTitle;

    public ContentResultSearchResult(final ContentResult contentResult, final Result resourceResult, final int score) {
        this.contentResult = contentResult;
        this.resourceResult = resourceResult;
        this.score = score;
    }

    public int compareTo(final SearchResult o) {
        int value = o.getScore() - this.score;
        if (value == 0) {
            value = getSortTitle().compareTo(o.getSortTitle());
            if (value == 0 && o instanceof ContentResultSearchResult) {
                ContentResultSearchResult other = (ContentResultSearchResult) o;
                value = this.contentResult.compareTo(other.getContentResult());
            } else if (value == 0) {
                // arbitrarily rank eresource results higher
                value = 1;
            }
        }
        return value;
    }

    @Override
    public boolean equals(final Object object) {
        boolean equals = false;
        if (object instanceof ContentResultSearchResult) {
            ContentResultSearchResult other = (ContentResultSearchResult) object;
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
            String temp = NON_FILING_PATTERN.matcher(this.contentResult.getTitle()).replaceFirst("");
            this.sortTitle = WHITESPACE.matcher(temp).replaceAll("").toLowerCase();
        }
        return this.sortTitle;
    }

    @Override
    public int hashCode() {
        return getSortTitle().hashCode();
    }

    private int compareToIgnoreScore(final ContentResultSearchResult other) {
        int value = getSortTitle().compareTo(other.getSortTitle());
        if (value == 0) {
            value = this.contentResult.compareTo(other.getContentResult());
        }
        return value;
    }
}
