package edu.stanford.irt.laneweb.search;

import java.util.regex.Pattern;

/**
 * @author ryanmax
 */
public class QueryTermPattern {

    private static final Pattern HYPHEN_PATTERN = Pattern.compile("\\-");

    private static final Pattern INVERT_COMMAS_PATTERN = Pattern.compile("(\\(?((\\w| |-|_)+), ((\\w| |-|_)+)\\)?)");

    private static final String INVERT_REPLACEMENT = "$1 and ($4 $2)";

    private static final String NONWORD = "\\\\W";

    private static final Pattern SPACE_PATTERN = Pattern.compile(" ");

    private static final Pattern UNACCEPTABLE_CHARS_PATTERN = Pattern.compile("[^a-zA-Z0-9,-_ ]");

    /**
     * normalize query terms for use in regex pattern, where normal means:
     * 
     * <pre>
     *  lower-case
     *  strip [^a-zA-Z0-9,-_ ]
     *  invert comma separated terms: Heparin, Low-Molecular-Weight becomes Low-Molecular-Weight Heparin
     *  replace hyphens and spaces with "\W"
     * </pre>
     * 
     * @param query
     * @return String to use in regExp pattern
     */
    public static Pattern getPattern(final String query) {
        String normalQuery;
        normalQuery = query.toLowerCase();
        normalQuery = UNACCEPTABLE_CHARS_PATTERN.matcher(normalQuery).replaceAll(NONWORD);
        normalQuery = INVERT_COMMAS_PATTERN.matcher(normalQuery).replaceAll(INVERT_REPLACEMENT);
        normalQuery = normalQuery.replaceAll(" and ", "|");
        normalQuery = HYPHEN_PATTERN.matcher(normalQuery).replaceAll(NONWORD);
        normalQuery = SPACE_PATTERN.matcher(normalQuery).replaceAll(NONWORD);
        return Pattern.compile(normalQuery, Pattern.CASE_INSENSITIVE);
    }
}
