package edu.stanford.irt.laneweb.solr;

/**
 * Clean query strings before sending to Solr
 * <p>
 * Case 110133: Solr can parse queries with brackets and braces as expensive ranges queries, causing performance
 * problems
 * </p>
 * <p>
 * Lucence special characters:
 * https://lucene.apache.org/core/2_9_4/queryparsersyntax.html#Escaping%20Special%20Characters
 * </p>
 *
 * @author ryanmax
 */
public final class SolrQueryParser {

    private static final String SPACE_TO_SPACE = " TO ";

    public static String parse(final String query) {
        return maybeEscape(query);
    }

    private static boolean isEscapableCharacter(final char c) {
        if (c == '[' || c == ']' || c == '{' || c == '}') {
            return true;
        }
        return false;
    }

    private static String maybeEscape(final String query) {
        if (needsEscaping(query)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < query.length(); i++) {
                char c = query.charAt(i);
                if (isEscapableCharacter(c)) {
                    sb.append('\\');
                }
                sb.append(c);
            }
            return sb.toString();
        }
        return query;
    }

    private static boolean needsEscaping(final String query) {
        // escape brackets/braces if range query lacks the " TO " keyword
        if ((query.contains("[") && query.contains("]")) || (query.contains("{") && query.contains("}"))) {
            return !query.contains(SPACE_TO_SPACE);
        }
        return false;
    }
}
