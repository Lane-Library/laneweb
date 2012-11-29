package edu.stanford.irt.laneweb.search;

import java.util.regex.Pattern;

import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.Result;

/**
 * @author ryanmax
 */
public class ContentResultSearchResult implements SearchResult {

    private static final Pattern YEAR_PATTERN = Pattern.compile(".*(\\d{4}).*");

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

    private String buildCompareString(final ContentResult cResult) {
        StringBuffer sb = new StringBuffer();
        String pubDate = cResult.getPublicationDate();
        String pubVolume = cResult.getPublicationVolume();
        String pubAuthor = cResult.getAuthor();
        if (null != pubDate) {
            sb.append(YEAR_PATTERN.matcher(pubDate).replaceFirst("$1"));
        }
        if (null != pubVolume) {
            sb.append(pubVolume);
        }
        if (null != pubAuthor) {
            sb.append(WHITESPACE.matcher(pubAuthor).replaceAll("").toLowerCase());
        }
        return sb.toString();
    }

    public int compareTo(final SearchResult o) {
        int scoreCmp = o.getScore() - this.score;
        int titleCmp = this.sortTitle.compareTo(o.getSortTitle());
        if (titleCmp == 0) {
            ContentResultSearchResult other = (ContentResultSearchResult) o;
            String otherContentId = other.contentResult.getContentId();
            String thisContentId = this.contentResult.getContentId();
            if (null != otherContentId && null != thisContentId) {
                if (otherContentId.equals(thisContentId)) {
                    return 0;
                }
                // different contentIds: compare by score, then by contentId (newer PMIDs are larger and come first)
                return (scoreCmp != 0 ? scoreCmp : otherContentId.compareTo(thisContentId));
            }
            String otherCompareString = buildCompareString(other.contentResult);
            String thisCompareString = buildCompareString(this.contentResult);
            if (!otherCompareString.isEmpty() && !thisCompareString.isEmpty()) {
                // opposite order from title compare because newer should come first (date is first string)
                return otherCompareString.compareTo(thisCompareString);
            }
        }
        return (scoreCmp != 0 ? scoreCmp : titleCmp);
    }

    @Override
    public boolean equals(final Object object) {
        if (object instanceof ContentResultSearchResult) {
            ContentResultSearchResult other = (ContentResultSearchResult) object;
            if (other.hashCode == this.hashCode) {
                String otherContentId = other.contentResult.getContentId();
                String thisContentId = this.contentResult.getContentId();
                if (null != otherContentId && null != thisContentId) {
                    return otherContentId.equals(thisContentId);
                }
                String otherCompareString = buildCompareString(other.contentResult);
                String thisCompareString = buildCompareString(this.contentResult);
                if (!otherCompareString.isEmpty() && !thisCompareString.isEmpty()) {
                    return otherCompareString.equals(thisCompareString);
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
