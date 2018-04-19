package edu.stanford.irt.laneweb.eresources.search;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.regex.Pattern;

public class FacetComparator implements Comparator<Facet>, Serializable {

    private static final Pattern DATE_PATTERN = Pattern.compile("(date:\\[20.*){2}");

    /** for Serializable. */
    private static final long serialVersionUID = 1L;

    private static final Pattern TYPE_PATTERN = Pattern.compile("(type:.*){2}");

    private Collection<String> prioritizedPublicationTypes = new ArrayList<>();

    public FacetComparator(final Collection<String> priorityPublicationTypes) {
        for (String pt : priorityPublicationTypes) {
            this.prioritizedPublicationTypes.add("publicationType:" + pt);
        }
    }

    @Override
    public int compare(final Facet facet1, final Facet facet2) {
        if (facet1 == null || facet2 == null) {
            throw new IllegalArgumentException("cannot compare " + facet1 + " to " + facet2);
        }
        String facet1Name = getFacetNameAndValue(facet1);
        String facet2Name = getFacetNameAndValue(facet2);
        String combinedFacetNames = new StringBuilder(facet1Name).append(facet2Name).toString();
        int countDiff = (int) (facet2.getCount() - facet1.getCount());
        int retValue = 0;
        // case 110630: Move 5 years to be higher than last 10 years within Year filter
        if (DATE_PATTERN.matcher(combinedFacetNames).matches()) {
            retValue = facet2Name.compareTo(facet1Name);
        } else if (TYPE_PATTERN.matcher(combinedFacetNames).matches()) {
            // case 131450: indent Digital and Print subtypes of Book and Journal
            retValue = facet1Name.compareTo(facet2Name);
        } else if (this.prioritizedPublicationTypes.contains(facet1Name)
                && !this.prioritizedPublicationTypes.contains(facet2Name)) {
            // cases 110125 and 121834: give some Article Types display priority
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

    private String getFacetNameAndValue(final Facet facet) {
        return new StringBuilder(facet.getFieldName()).append(':').append(facet.getValue()).toString();
    }
}
