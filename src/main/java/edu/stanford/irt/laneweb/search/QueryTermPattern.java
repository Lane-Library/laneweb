package edu.stanford.irt.laneweb.search;

import java.util.Locale;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import edu.stanford.irt.laneweb.LanewebException;

/**
 * @author ryanmax
 */
public final class QueryTermPattern {

    private static final Pattern AND_PATTERN = Pattern.compile(" (and|&) ");

    private static final String EMPTY = "";

    private static final Pattern INVERT_COMMAS_PATTERN = Pattern.compile("(\\(?(([\\w \\-])+), (([\\w \\-])+)\\)?)");

    private static final String INVERT_REPLACEMENT = "$1 and $4 $2";

    private static final String MAYBE_NONWORD = "\\\\W?";

    private static final String NONWORD = "\\\\W";

    private static final Pattern NULL_QUERY_PATTERN = Pattern.compile(".*");

    private static final Pattern PARENS_PATTERN = Pattern.compile("[\\(\\)]");

    private static final String PIPE = "|";

    private static final Pattern PIPE_PATTERN = Pattern.compile("\\|\\|");

    private static final Pattern QUOTES_PATTERN = Pattern.compile("\\\"");

    private static final Pattern SPACE_HYPHEN_PATTERN = Pattern.compile("[- ]");

    private static final Pattern UNACCEPTABLE_CHARS_PATTERN = Pattern.compile("[^a-zA-Z0-9,\\-_\" &]");

    private QueryTermPattern() {
        // empty constructor
    }

    /**
     * normalize query terms for use in regex pattern, where normal means:
     *
     * <pre>
     *  trim
     *  lower-case
     *  remove parentheses
     *  replace UNACCEPTABLE_CHARS with \W
     *  replace quotes with "\W?"
     *  invert comma separated terms: Heparin, Low-Molecular-Weight becomes Low-Molecular-Weight Heparin
     *  replace hyphens and spaces with "\W"
     * </pre>
     *
     * @param query
     *            the query text
     * @return a Pattern constructed from the query.
     * @throws LanewebException
     *             if there was a PatternSyntaxException in order to report the original query
     */
    public static Pattern getPattern(final String query) {
        if (query == null) {
            return NULL_QUERY_PATTERN;
        }
        String normalQuery = query.trim().toLowerCase(Locale.US);
        normalQuery = INVERT_COMMAS_PATTERN.matcher(normalQuery).replaceAll(INVERT_REPLACEMENT);
        normalQuery = PARENS_PATTERN.matcher(normalQuery).replaceAll(EMPTY);
        normalQuery = UNACCEPTABLE_CHARS_PATTERN.matcher(normalQuery).replaceAll(NONWORD);
        normalQuery = QUOTES_PATTERN.matcher(normalQuery).replaceAll(MAYBE_NONWORD);
        normalQuery = AND_PATTERN.matcher(normalQuery).replaceAll(PIPE);
        normalQuery = SPACE_HYPHEN_PATTERN.matcher(normalQuery).replaceAll(NONWORD);
        // education, medical AND "cognitive+load" was generated two "||" -->
        // bug 65768
        normalQuery = PIPE_PATTERN.matcher(normalQuery).replaceAll(PIPE);
        try {
            return Pattern.compile(normalQuery, Pattern.CASE_INSENSITIVE);
        } catch (PatternSyntaxException e) {
            throw new LanewebException("error creating Pattern for: " + query + "\n" + e.getMessage(), e);
        }
    }
}
