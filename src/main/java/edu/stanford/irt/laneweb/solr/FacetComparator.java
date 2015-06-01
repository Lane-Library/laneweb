package edu.stanford.irt.laneweb.solr;

import java.io.Serializable;
import java.util.Comparator;

public class FacetComparator implements Comparator<Facet>, Serializable {

    /** for Serializable. */
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(final Facet facet1, final Facet facet2) {
        if (facet1 == null || facet2 == null) {
            throw new IllegalArgumentException("cannot compare " + facet1 + " to " + facet2);
        }
        int countDiff = (int) (facet2.getCount() - facet1.getCount());
        if (countDiff != 0) {
            return countDiff;
        }
        return facet1.getName().compareTo(facet2.getName());
    }
}
