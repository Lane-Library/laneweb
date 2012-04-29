package edu.stanford.irt.laneweb.search;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import edu.stanford.irt.laneweb.LanewebException;

/**
 * @author ryanmax
 */
public final class QueryTermPattern {

    private static final Pattern INVERT_COMMAS_PATTERN = Pattern.compile("(\\(?((\\w| |-|_)+), ((\\w| |-|_)+)\\)?)");

    private static final String INVERT_REPLACEMENT = "$1 and $4 $2";

    private static final String MAYBE_NONWORD = "\\\\W?";

    private static final String NONWORD = "\\\\W";

    private static final Pattern REPLACE_QUOTES = Pattern.compile("\\\"");

    private static final Pattern SPACE_HYPHEN_PATTERN = Pattern.compile("[- ]");

    private static final Pattern UNACCEPTABLE_CHARS_PATTERN = Pattern.compile("[^a-zA-Z0-9,-_\" [\\\\\\?\\[]]");

    /**
     * normalize query terms for use in regex pattern, where normal means:
     * 
     * <pre>
     *  trim
     *  lower-case
     *  replace quotes with "\W?"
     *  replace [^a-zA-Z0-9,-_ [\\\\\\?\\[]]] with \W
     *  invert comma separated terms: Heparin, Low-Molecular-Weight becomes Low-Molecular-Weight Heparin
     *  replace hyphens and spaces with "\W"
     * </pre>
     * 
     * @param query
     * @return a Pattern constructed from the query.
     * @throws LanewebException
     *             if there was a PatternSyntaxException in order to report the
     *             original query
     */
    public static Pattern getPattern(final String query) {
        String normalQuery = query.trim().toLowerCase();
        normalQuery = INVERT_COMMAS_PATTERN.matcher(normalQuery).replaceAll(INVERT_REPLACEMENT);
        normalQuery = UNACCEPTABLE_CHARS_PATTERN.matcher(normalQuery).replaceAll(NONWORD);
        normalQuery = REPLACE_QUOTES.matcher(normalQuery).replaceAll(MAYBE_NONWORD);
        normalQuery = normalQuery.replaceAll(" and ", "|");
        normalQuery = SPACE_HYPHEN_PATTERN.matcher(normalQuery).replaceAll(NONWORD);
        // education, medical AND "cognitive+load" was generated two "||" -->
        // bug 65768
        normalQuery = normalQuery.replaceAll("\\|\\|", "|");
        try {
            return Pattern.compile(normalQuery, Pattern.CASE_INSENSITIVE);
        } catch (PatternSyntaxException e) {
            throw new LanewebException("error creating Pattern for: " + query + "\n" + e.getMessage(), e);
        }
    }

    private QueryTermPattern() {
        // empty constructor
    }
}
