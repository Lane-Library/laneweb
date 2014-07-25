package edu.stanford.irt.laneweb.search;

import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

/**
 * @author ryanmax
 */
public class ContentResultSearchResult extends AbstractSearchResult {

    private ContentResult contentResult;

    private int hashCode;

    private Result resourceResult;

    public ContentResultSearchResult(final ContentResult contentResult, final Result resourceResult, final int score) {
        super(score, contentResult.getTitle());
        this.contentResult = contentResult;
        this.resourceResult = resourceResult;
    }

    public int compareTo(final SearchResult o) {
        int value = o.getScore() - getScore();
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

    @Override
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

    private int compareToIgnoreScore(final ContentResultSearchResult other) {
        int value = getSortTitle().compareTo(other.getSortTitle());
        if (value == 0) {
            value = this.contentResult.compareTo(other.getContentResult());
        }
        return value;
    }
}
