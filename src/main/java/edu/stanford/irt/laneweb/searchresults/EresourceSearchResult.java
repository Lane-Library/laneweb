/**
 * 
 */
package edu.stanford.irt.laneweb.searchresults;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.EresourceResource;

/**
 * @author ryanmax
 * 
 * $Id$
 */
public class EresourceSearchResult extends EresourceResource implements SearchResult {

    private String sortTitle;

    public EresourceSearchResult(Eresource eresource) {
        super(eresource);
        this.sortTitle = NON_FILING_PATTERN.matcher(eresource.getTitle()).replaceFirst("");
    }

    public int getScore() {
        return this.eresource.getScore();
    }

    public String getSortTitle() {
        return this.sortTitle;
    }

    public int compareTo(SearchResult o) {
        int scoreCmp = o.getScore() - this.eresource.getScore();
        return (scoreCmp != 0 ? scoreCmp : this.sortTitle.compareTo(o.getSortTitle()));
    }

    @Override
    public int hashCode() {
        return this.sortTitle.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof EresourceSearchResult)) {
            return false;
        }
        EresourceSearchResult eres = (EresourceSearchResult) other;
        return eres.sortTitle.equals(this.sortTitle);
    }
}
