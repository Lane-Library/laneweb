package edu.stanford.irt.laneweb.mesh;

import java.io.Serializable;
import java.util.Comparator;

public class MeshSuggestComparator implements Comparator<String>, Serializable {

    /** for Serializable. */
    private static final long serialVersionUID = 1L;

    private String query;

    public MeshSuggestComparator(final String query) {
        this.query = query.toUpperCase();
    }

    public int compare(final String heading1, final String heading2) {
        String upper1 = heading1.toUpperCase();
        String upper2 = heading2.toUpperCase();
        int weightDiff = queryWeight(upper2) - queryWeight(upper1);
        if (weightDiff != 0) {
            return weightDiff;
        }
        return upper1.compareTo(upper2);
    }

    private int queryWeight(final String heading) {
        int index = heading.indexOf(this.query);
        if (index == 0) {
            return 2;
        } else if (index > 0) {
            return 1;
        }
        return 0;
    }
}
