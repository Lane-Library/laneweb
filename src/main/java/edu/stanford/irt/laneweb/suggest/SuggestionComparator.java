package edu.stanford.irt.laneweb.suggest;

import java.io.Serializable;
import java.util.Comparator;

import com.ibm.icu.text.Normalizer;

public class SuggestionComparator implements Comparator<String>, Serializable {

    /** for Serializable. */
    private static final long serialVersionUID = 1L;

    private String query;

    public SuggestionComparator(final String query) {
        if (query == null) {
            throw new IllegalArgumentException("null query");
        }
        this.query = normalize(query);
    }

    public int compare(final String heading1, final String heading2) {
        if (heading1 == null || heading2 == null) {
            throw new IllegalArgumentException("cannot compare " + heading1 + " to " + heading2);
        }
        String upper1 = normalize(heading1);
        String upper2 = normalize(heading2);
        int weightDiff = queryWeight(upper2) - queryWeight(upper1);
        if (weightDiff != 0) {
            return weightDiff;
        }
        return upper1.compareTo(upper2);
    }

    private String normalize(final String string) {
        String decomposed = Normalizer.decompose(string, false);
        StringBuilder sb = new StringBuilder(decomposed.length());
        char theChar;
        for (int i = 0; i < decomposed.length(); i++) {
            theChar = decomposed.charAt(i);
            if (theChar < 128) {
                sb.append(theChar);
            }
        }
        return sb.toString().toUpperCase();
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
