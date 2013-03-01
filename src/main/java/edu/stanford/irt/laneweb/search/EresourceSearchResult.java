package edu.stanford.irt.laneweb.search;

import edu.stanford.irt.eresources.Eresource;

/**
 * @author ryanmax
 */
public class EresourceSearchResult implements SearchResult {

    private Eresource eresource;

    private int hashCode;

    private String sortTitle;

    public EresourceSearchResult(final Eresource eresource) {
        this.eresource = eresource;
        this.sortTitle = NON_FILING_PATTERN.matcher(eresource.getTitle()).replaceFirst("");
        this.sortTitle = WHITESPACE.matcher(this.sortTitle).replaceAll("").toLowerCase();
        this.hashCode = this.sortTitle.hashCode();
    }

    public int compareTo(final SearchResult other) {
        int scoreCmp = other.getScore() - getScore();
        if (scoreCmp == 0) {
            scoreCmp = this.sortTitle.compareTo(other.getSortTitle());
            if (scoreCmp == 0 && !other.equals(this)) {
                // This happens when more than one eresource has the same title
                // as the query
                if (other instanceof EresourceSearchResult) {
                    scoreCmp = ((EresourceSearchResult)other).getEresource().getRecordId() - this.eresource.getRecordId();
                } else {
                    scoreCmp = -1;
                }
            }
        }
        return scoreCmp;
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof EresourceSearchResult) {
            return ((EresourceSearchResult) other).eresource.equals(this.eresource);
        }
        return false;
    }

    public Eresource getEresource() {
        return this.eresource;
    }

    public int getScore() {
        int score = this.eresource.getScore();
        return score < 0 ? 0 : score;
    }

    public String getSortTitle() {
        return this.sortTitle;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }
}
