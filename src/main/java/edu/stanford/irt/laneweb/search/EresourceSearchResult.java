package edu.stanford.irt.laneweb.search;

import edu.stanford.irt.laneweb.eresources.Eresource;

/**
 * @author ryanmax
 */
public class EresourceSearchResult extends AbstractSearchResult {

    private Eresource eresource;

    private int hashCode;

    public EresourceSearchResult(final Eresource eresource) {
        super(Float.floatToIntBits(eresource.getScore()), eresource.getTitle());
        this.eresource = eresource;
    }

    @Override
    public int compareTo(final SearchResult other) {
        int scoreCmp = other.getScore() - getScore();
        if (scoreCmp == 0) {
            scoreCmp = getSortTitle().compareTo(other.getSortTitle());
            if (scoreCmp == 0 && !other.equals(this)) {
                // This happens when more than one eresource has the same title
                // as the query
                if (other instanceof EresourceSearchResult) {
                    scoreCmp = ((EresourceSearchResult) other).getEresource().getRecordId()
                            - this.eresource.getRecordId();
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

    @Override
    public boolean hasAdditionalText() {
        String description = this.eresource.getDescription();
        return description != null && description.length() > 0;
    }

    @Override
    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = getSortTitle().hashCode();
        }
        return this.hashCode;
    }
}
