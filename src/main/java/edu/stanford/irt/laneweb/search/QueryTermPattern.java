/**
 * 
 */
package edu.stanford.irt.laneweb.search;

import java.util.regex.Pattern;

/**
 * @author ryanmax
 */
public class QueryTermPattern {

    private static final String EMPTY = "";

    private static final Pattern HYPHEN_PATTERN = Pattern.compile("\\-");

    private static final Pattern UNACCEPTABLE_CHARS_PATTERN = Pattern.compile("[^a-zA-Z0-9,-_ ]");

    private static final Pattern INVERT_COMMAS_PATTERN = Pattern.compile("(\\(?((\\w| |-|_)+), ((\\w| |-|_)+)\\)?)");

    private static final String INVERT_REPLACEMENT = "$1 and ($4 $2)";

    private static final String PERIOD = "\\.";

        /**
         * normalize query terms for use in regex pattern, where normal means:
         * 
         * <pre>
         *  lower-case
         *  invert comma separated terms: Heparin, Low-Molecular-Weight becomes Low-Molecular-Weight Heparin
         *  replace hyphens with "."
         *  strip [^a-zA-Z0-9,-_ ]
         * </pre>
         * 
         * @param query
         * @return String to use in regExp pattern
         */
    public static Pattern getPattern(String query) {
        String normalQuery;
        normalQuery = query.toLowerCase();
        normalQuery = INVERT_COMMAS_PATTERN.matcher(normalQuery).replaceAll(INVERT_REPLACEMENT);
        normalQuery = HYPHEN_PATTERN.matcher(normalQuery).replaceAll(PERIOD);
        normalQuery = UNACCEPTABLE_CHARS_PATTERN.matcher(normalQuery).replaceAll(EMPTY);
        return Pattern.compile(normalQuery.replaceAll(" and ", "|"), Pattern.CASE_INSENSITIVE);
    }
}
