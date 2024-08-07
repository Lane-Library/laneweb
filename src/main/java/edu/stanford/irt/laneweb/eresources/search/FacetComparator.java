package edu.stanford.irt.laneweb.eresources.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import edu.stanford.irt.laneweb.eresources.model.solr.FacetFieldEntry;

public class FacetComparator implements Comparator<FacetFieldEntry> {

    private static final String PUBLICATION_TYPE = "publicationType:";

    private Collection<String> prioritizedPublicationTypes;

    private Collection<String> priorityOverAll;

    public FacetComparator(final Collection<String> priorityPublicationTypes) {
        this.prioritizedPublicationTypes = new ArrayList<>();
        for (String pt : priorityPublicationTypes) {
            this.prioritizedPublicationTypes.add(PUBLICATION_TYPE + pt);
        }
        this.priorityOverAll = new ArrayList<>();
    }

    public void addTopPrioritiesFromFacets(final String facets) {
        String[] facetFieldEntities = facets.split("::");
        for (String field : facetFieldEntities) {
            this.priorityOverAll.add(field.replace("\"", ""));
        }
    }

    public void addTopPriority(final String facet) {
        this.priorityOverAll.add(facet);
    }

    @Override
    public int compare(final FacetFieldEntry facet1, final FacetFieldEntry facet2) {
        if (facet1 == null || facet2 == null) {
            throw new IllegalArgumentException("cannot compare " + facet1 + " to " + facet2);
        }
        String facet1Name = getFacetNameAndValue(facet1);
        String facet2Name = getFacetNameAndValue(facet2);
        int countDiff = facet1.getValueCount() - facet2.getValueCount();
        if (this.priorityOverAll.contains(facet1Name) && !this.priorityOverAll.contains(facet2Name)) {
            return -1;
        } else if (!this.priorityOverAll.contains(facet1Name) && this.priorityOverAll.contains(facet2Name)) {
            return 1;
        } else if (this.prioritizedPublicationTypes.contains(facet1Name)
                && this.prioritizedPublicationTypes.contains(facet2Name)) {
            return comparePublicationTypes(facet1, facet2);
        } else if (this.prioritizedPublicationTypes.contains(facet1Name)
                && !this.prioritizedPublicationTypes.contains(facet2Name)) {
            return -1;
        } else if (!this.prioritizedPublicationTypes.contains(facet1Name)
                && this.prioritizedPublicationTypes.contains(facet2Name)) {
            return 1;
        } else if (countDiff > 0) {
            return -1;
        } else if (countDiff < 0) {
            return 1;
        } else {
            return facet1Name.compareTo(facet2Name);
        }
    }

    private int comparePublicationTypes(final FacetFieldEntry facet1, final FacetFieldEntry facet2) {
        if (facet1.getValueCount() == facet2.getValueCount()) {
            return getPosition(facet1, facet2);
        }
        return (facet1.getValueCount() > facet2.getValueCount()) ? -1 : 1;
    }

    private String getFacetNameAndValue(final FacetFieldEntry facet) {
        return new StringBuilder(facet.getKey().getName()).append(':').append(facet.getValue()).toString();
    }

    private int getPosition(final FacetFieldEntry facet1, final FacetFieldEntry facet2) {
        String facet1Name = getFacetNameAndValue(facet1);
        String facet2Name = getFacetNameAndValue(facet2);
        for (String pt : this.prioritizedPublicationTypes) {
            if (pt.equals(facet1Name)) {
                return -1;
            }
            if (pt.equals(facet2Name)) {
                return 1;
            }
        }
        return 0;
    }
}
