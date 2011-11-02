package edu.stanford.irt.laneweb.search;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import edu.stanford.irt.laneweb.LanewebException;

/**
 * @author ryanmax
 */
public final class QueryTermPattern {

    private static final Pattern HYPHEN_PATTERN = Pattern.compile("\\-");

    private static final Pattern INVERT_COMMAS_PATTERN = Pattern.compile("(\\(?((\\w| |-|_)+), ((\\w| |-|_)+)\\)?)");

    private static final String INVERT_REPLACEMENT = "$1 and $4 $2";

    private static final String MAYBE_NONWORD = "\\\\W?";

    private static final String NONWORD = "\\\\W";

    private static final Pattern REPLACE_QUOTES_PATTERN = Pattern.compile("\\\"");

    private static final Pattern SPACE_PATTERN = Pattern.compile(" ");

    private static final Pattern UNACCEPTABLE_CHARS_PATTERN = Pattern.compile("[^a-zA-Z0-9,-_ ]");

    /**
     * normalize query terms for use in regex pattern, where normal means:
     * 
     * <pre>
     *  trim
     *  lower-case
     *  replace quotes with "\W?"
     *  replace [^a-zA-Z0-9,-_ ] with \W
     *  invert comma separated terms: Heparin, Low-Molecular-Weight becomes Low-Molecular-Weight Heparin
     *  replace hyphens and spaces with "\W"
     * </pre>
     * 
     * @param query
     * @return String to use in regExp pattern
     * @throws LanewebException if there was a PatternSyntaxException in order to report the original query
     */
    public static Pattern getPattern(final String query) {
        String normalQuery;
        normalQuery = query.trim().toLowerCase();
        normalQuery = REPLACE_QUOTES_PATTERN.matcher(normalQuery).replaceAll(MAYBE_NONWORD);
        normalQuery = INVERT_COMMAS_PATTERN.matcher(normalQuery).replaceAll(INVERT_REPLACEMENT);
        normalQuery = UNACCEPTABLE_CHARS_PATTERN.matcher(normalQuery).replaceAll(NONWORD);
        normalQuery = normalQuery.replaceAll(" and ", "|");
        normalQuery = HYPHEN_PATTERN.matcher(normalQuery).replaceAll(NONWORD);
        normalQuery = SPACE_PATTERN.matcher(normalQuery).replaceAll(NONWORD);
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
