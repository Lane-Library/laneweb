package edu.stanford.irt.laneweb.metasearch;

import java.util.Locale;
import java.util.regex.Pattern;

import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;

/**
 * @author ryanmax
 */
public class SearchResult implements Comparable<SearchResult> {

    private static final Pattern NON_FILING_PATTERN = Pattern.compile("^(a|an|the) ", Pattern.CASE_INSENSITIVE);

    private static final Pattern WHITESPACE = Pattern.compile("\\W");

    private final ContentResult contentResult;

    private final Result resourceResult;

    private final int score;

    private final String sortTitle;

    private final String title;

    public SearchResult(final ContentResult contentResult, final Result resourceResult, final int score) {
        this.score = score < 0 ? 0 : score;
        this.title = contentResult.getTitle();
        this.contentResult = contentResult;
        this.resourceResult = resourceResult;
        String temp = "";
        if (this.title != null) {
            temp = NON_FILING_PATTERN.matcher(this.title).replaceFirst("");
            temp = WHITESPACE.matcher(temp).replaceAll("").toLowerCase(Locale.US);
        }
        this.sortTitle = temp;
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
        return this.sortTitle;
    }

    public boolean hasAdditionalText() {
        String description = this.contentResult.getDescription();
        return description != null && description.length() > 0;
    }

    @Override
    public int hashCode() {
        return this.sortTitle.hashCode();
    }

    @Override
    public String toString() {
        return "SearchResult [id=" + this.contentResult.getId() + ", score=" + this.score + ", title=" + this.title
                + "]";
    }

    private int compareToIgnoreScore(final SearchResult other) {
        int value = getSortTitle().compareTo(other.getSortTitle());
        if (value == 0) {
            value = this.contentResult.compareTo(other.contentResult);
        }
        return value;
    }
}
