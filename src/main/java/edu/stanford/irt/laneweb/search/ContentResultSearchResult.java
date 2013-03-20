package edu.stanford.irt.laneweb.search;

import java.util.regex.Pattern;

import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.Result;

/**
 * @author ryanmax
 */
public class ContentResultSearchResult implements SearchResult {

    private static final Pattern YEAR_PATTERN = Pattern.compile(".*(\\d{4}).*");

    private String compareString;

    private ContentResult contentResult;

    private int hashCode;

    // TODO: move this to the ContentResult
    private String publicationText;

    private Result resourceResult;

    private int score;

    private String sortTitle;

    public ContentResultSearchResult(final ContentResult contentResult, final Result resourceResult, final int score) {
        this.contentResult = contentResult;
        this.resourceResult = resourceResult;
        this.sortTitle = NON_FILING_PATTERN.matcher(this.contentResult.getTitle()).replaceFirst("");
        this.sortTitle = WHITESPACE.matcher(this.sortTitle).replaceAll("").toLowerCase();
        this.score = score;
        this.compareString = buildCompareString(contentResult);
        this.hashCode = this.sortTitle.hashCode();
    }

    private String buildCompareString(final ContentResult cResult) {
        StringBuilder sb = new StringBuilder();
        String pubDate = cResult.getPublicationDate();
        String pubVolume = cResult.getPublicationVolume();
        String pubIssue = cResult.getPublicationIssue();
        String pubAuthor = cResult.getAuthor();
        if (null != pubDate) {
            sb.append(YEAR_PATTERN.matcher(pubDate).replaceFirst("$1"));
        }
        if (null != pubVolume) {
            sb.append(pubVolume);
        }
        if (null != pubIssue) {
            sb.append(pubIssue);
        }
        if (null != pubAuthor) {
            sb.append(WHITESPACE.matcher(pubAuthor).replaceAll("").toLowerCase());
        }
        return sb.toString();
    }

    @Override
    public int compareTo(final SearchResult o) {
        int scoreCmp = o.getScore() - this.score;
        int titleCmp = this.sortTitle.compareTo(o.getSortTitle());
        if (titleCmp == 0 && scoreCmp == 0 && o instanceof ContentResultSearchResult) {
            ContentResultSearchResult other = (ContentResultSearchResult) o;
            String otherContentId = other.contentResult.getContentId();
            String thisContentId = this.contentResult.getContentId();
            if (null != otherContentId && null != thisContentId) {
                if (otherContentId.equals(thisContentId)) {
                    return 0;
                }
                // different contentIds: compare by score, then by contentId (newer PMIDs are larger and come first)
                return otherContentId.compareTo(thisContentId);
            }
            if (!other.compareString.isEmpty() && !this.compareString.isEmpty()) {
                // opposite order from title compare because newer should come first (date is first string)
                return other.compareString.compareTo(this.compareString);
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
                if (!other.compareString.isEmpty() && !this.compareString.isEmpty()) {
                    return other.compareString.equals(this.compareString);
                }
                return other.sortTitle.equals(this.sortTitle);
            }
        }
        return false;
    }

    public ContentResult getContentResult() {
        return this.contentResult;
    }

    public String getPublicationText() {
        if (this.publicationText == null) {
            StringBuilder sb = new StringBuilder();
            String title = this.contentResult.getPublicationTitle();
            if (title != null) {
                sb.append(title).append(". ");
                String date = this.contentResult.getPublicationDate();
                if (date != null && date.length() > 0) {
                    sb.append(date);
                }
                String volume = this.contentResult.getPublicationVolume();
                if (volume != null && volume.length() > 0) {
                    sb.append(';').append(volume);
                }
                String issue = this.contentResult.getPublicationIssue();
                if (issue != null && issue.length() > 0) {
                    sb.append('(').append(issue).append(')');
                }
                String pages = this.contentResult.getPages();
                if (pages != null && pages.length() > 0) {
                    sb.append(':').append(pages).append('.');
                }
            }
            this.publicationText = sb.toString();
        }
        return this.publicationText;
    }

    public Result getResourceResult() {
        return this.resourceResult;
    }

    @Override
    public int getScore() {
        return this.score;
    }

    @Override
    public String getSortTitle() {
        return this.sortTitle;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }
}
