package edu.stanford.irt.laneweb.search;

import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.Result;

/**
 * @author ryanmax
 */
public class ContentResultSearchResult implements SearchResult {

    private ContentResult contentResult;

    private int hashCode;

    private Result resourceResult;

    private int score;

    private String sortTitle;

    public ContentResultSearchResult(final ContentResult contentResult, final Result resourceResult, final int score) {
        this.contentResult = contentResult;
        this.resourceResult = resourceResult;
        this.sortTitle = NON_FILING_PATTERN.matcher(this.contentResult.getTitle()).replaceFirst("");
        this.sortTitle = WHITESPACE.matcher(this.sortTitle).replaceAll("").toLowerCase();
        this.score = score;
        this.hashCode = this.sortTitle.hashCode();
    }

    public int compareTo(final SearchResult o) {
        int scoreCmp = o.getScore() - this.score;
        int titleCmp = this.sortTitle.compareTo(o.getSortTitle());
        if (titleCmp == 0 && scoreCmp == 0) {
            ContentResultSearchResult other = (ContentResultSearchResult) o;
            if (other.getResourceResult().getId().equals(this.getResourceResult().getId())) {
                String otherContentId = other.contentResult.getContentId();
                String thisContentId = this.contentResult.getContentId();
                if (null == otherContentId || null == thisContentId) {
                    return other.contentResult.getURL().compareTo(this.contentResult.getURL());
                }
                return otherContentId.compareTo(thisContentId);
            }
        }
        return (scoreCmp != 0 ? scoreCmp : titleCmp);
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof ContentResultSearchResult) {
            ContentResultSearchResult other = (ContentResultSearchResult) object;
            if (other.hashCode == this.hashCode) {
                if (other.getResourceResult().getId().equals(this.getResourceResult().getId())) {
                    String otherContentId = other.contentResult.getContentId();
                    String thisContentId = this.contentResult.getContentId();
                    if (null == otherContentId || null == thisContentId) {
                        return other.contentResult.getURL().equals(this.contentResult.getURL());
                    }
                    return otherContentId.equals(thisContentId);
                }
                return other.sortTitle.equals(this.sortTitle);
            }
        }
        return false;
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

    @Override
    public int hashCode() {
        return this.hashCode;
    }
}
