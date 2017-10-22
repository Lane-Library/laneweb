package edu.stanford.irt.laneweb.suggest;

import java.io.Serializable;
import java.text.Normalizer;
import java.util.Comparator;
import java.util.Locale;

import edu.stanford.irt.suggest.Suggestion;

public class SuggestionComparator implements Comparator<Suggestion>, Serializable {

    /** for Serializable. */
    private static final long serialVersionUID = 1L;

    private String query;

    public SuggestionComparator(final String query) {
        if (query == null) {
            throw new IllegalArgumentException("null query");
        }
        this.query = toUpperCaseASCII(query);
    }

    @Override
    public int compare(final Suggestion suggestion1, final Suggestion suggestion2) {
        if (suggestion1 == null || suggestion2 == null) {
            throw new IllegalArgumentException("cannot compare " + suggestion1 + " to " + suggestion2);
        }
        String upper1 = toUpperCaseASCII(suggestion1.getSuggestionTitle());
        String upper2 = toUpperCaseASCII(suggestion2.getSuggestionTitle());
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

    private String toUpperCaseASCII(final String string) {
        String decomposed = Normalizer.normalize(string, Normalizer.Form.NFD);
        StringBuilder sb = new StringBuilder(decomposed.length());
        char theChar;
        for (int i = 0; i < decomposed.length(); i++) {
            theChar = decomposed.charAt(i);
            if (theChar < 128) {
                sb.append(theChar);
            }
        }
        return sb.toString().toUpperCase(Locale.US);
    }
}
