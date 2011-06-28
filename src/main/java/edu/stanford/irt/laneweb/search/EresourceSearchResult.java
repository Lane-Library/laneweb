package edu.stanford.irt.laneweb.search;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.EresourceResource;

/**
 * @author ryanmax
 */
public class EresourceSearchResult extends EresourceResource implements SearchResult {

    private String sortTitle;

    public EresourceSearchResult(final Eresource eresource) {
        super(eresource);
        this.sortTitle = NON_FILING_PATTERN.matcher(eresource.getTitle()).replaceFirst("");
        this.sortTitle = this.sortTitle.toLowerCase().replaceAll("\\W", "");
    }

    public int compareTo(final SearchResult other) {
        int scoreCmp = other.getScore() - this.eresource.getScore();
        if (scoreCmp == 0) {
            scoreCmp = this.sortTitle.compareTo(other.getSortTitle());
            if (scoreCmp == 0 && !other.equals(this)) {
                //This happens when more than one eresource has the same title as the query
                scoreCmp = -1;
            }
        }
        return (scoreCmp != 0 ? scoreCmp : this.sortTitle.compareTo(other.getSortTitle()));
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof EresourceSearchResult) {
            return ((EresourceSearchResult) other).eresource.equals(this.eresource);
        }
        return false;
    }

    public int getScore() {
        return this.eresource.getScore();
    }

    public String getSortTitle() {
        return this.sortTitle;
    }

    @Override
    public int hashCode() {
        return this.sortTitle.hashCode();
    }
}
