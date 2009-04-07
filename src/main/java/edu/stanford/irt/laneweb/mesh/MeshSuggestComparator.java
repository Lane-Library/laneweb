package edu.stanford.irt.laneweb.mesh;

import java.util.Comparator;

public class MeshSuggestComparator implements Comparator<String> {

    private String query;

    public MeshSuggestComparator(final String query) {
        this.query = query.toUpperCase();
    }

    public int compare(final String heading1, final String heading2) {
        int weightDiff = queryWeight(heading2) - queryWeight(heading1);
        if (weightDiff != 0) {
            return weightDiff;
        }
        return heading1.compareToIgnoreCase(heading2);
    }

    private int queryWeight(final String heading) {
        if (heading.toUpperCase().startsWith(this.query)) {
            return 2;
        } else if (heading.toUpperCase().contains(this.query)) {
            return 1;
        }
        return 0;
    }
}
