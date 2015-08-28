package edu.stanford.irt.laneweb.solr;

import java.io.Serializable;
import java.util.Comparator;

public class FacetComparator implements Comparator<Facet>, Serializable {

    private static final String LAST_X_YEARS_PREFIX = "year:[";

    /** for Serializable. */
    private static final long serialVersionUID = 1L;

    @Override
    public int compare(final Facet facet1, final Facet facet2) {
        if (facet1 == null || facet2 == null) {
            throw new IllegalArgumentException("cannot compare " + facet1 + " to " + facet2);
        }
        String facet1Name = facet1.getName();
        String facet2Name = facet2.getName();
        // case 110630: Move 5 years to be higher then last 10 years within Year filter
        if (facet1Name.startsWith(LAST_X_YEARS_PREFIX) && facet2Name.startsWith(LAST_X_YEARS_PREFIX)) {
            return facet2Name.compareTo(facet1Name);
        }
        int countDiff = (int) (facet2.getCount() - facet1.getCount());
        if (countDiff != 0) {
            return countDiff;
        }
        return facet1Name.compareTo(facet2Name);
    }
}
