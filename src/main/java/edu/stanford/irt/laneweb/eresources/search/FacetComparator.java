package edu.stanford.irt.laneweb.eresources.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class FacetComparator implements Comparator<Facet>, Serializable {

    private static final String COLON = ":";

    private static final String LAST_X_YEARS_PREFIX = "date:[20";

    private static final String PUBLICATION_TYPE = "publicationType:";

    /** for Serializable. */
    private static final long serialVersionUID = 1L;

    private Collection<String> prioritizedPublicationTypes = new ArrayList<>();

    public FacetComparator(final Collection<String> priorityPublicationTypes) {
        for (String pt : priorityPublicationTypes) {
            this.prioritizedPublicationTypes.add(PUBLICATION_TYPE + pt);
        }
    }

    @Override
    public int compare(final Facet facet1, final Facet facet2) {
        int retValue = 0;
        if (facet1 == null || facet2 == null) {
            throw new IllegalArgumentException("cannot compare " + facet1 + " to " + facet2);
        }
        String facet1Name = new StringBuilder(facet1.getFieldName()).append(COLON).append(facet1.getValue()).toString();
        String facet2Name = new StringBuilder(facet2.getFieldName()).append(COLON).append(facet2.getValue()).toString();
        int countDiff = (int) (facet2.getCount() - facet1.getCount());
        // case 110630: Move 5 years to be higher then last 10 years within Year filter
        if (facet1Name.startsWith(LAST_X_YEARS_PREFIX) && facet2Name.startsWith(LAST_X_YEARS_PREFIX)) {
            retValue = facet2Name.compareTo(facet1Name);
        }
        // case 131450: indent Digital and Print subtypes of Book and Journal
        else if ((facet1Name + facet2Name).matches("(type:.*){2}")) {
            retValue = facet1Name.compareTo(facet2Name);
        }
        // cases 110125 and 121834: give some Article Types display priority
        else if (this.prioritizedPublicationTypes.contains(facet1Name)
                && !this.prioritizedPublicationTypes.contains(facet2Name)) {
            retValue = -1;
        } else if (this.prioritizedPublicationTypes.contains(facet2Name)
                && !this.prioritizedPublicationTypes.contains(facet1Name)) {
            retValue = 1;
        } else if (countDiff != 0) {
            retValue = countDiff;
        } else {
            retValue = facet1Name.compareTo(facet2Name);
        }
        return retValue;
    }
}
