/**
 * 
 */
package edu.stanford.irt.laneweb.search;

import java.util.regex.Pattern;

/**
 * Strip common preceding text from queries containing PMIDs
 * <p>
 * NOTE: only handles 8 digit PMIDs ... others ignored.
 * </p>
 * 
 * @author ryanmax
 */
public class PMIDNormalizer {

    private static final Pattern PMID_PATTERN = Pattern.compile("(?:pmid|pubmed ?id)? ?(?: |:|#)? ?([0-9]{8})\\b",
            Pattern.CASE_INSENSITIVE);

    private static final String REPLACEMENT = "$1";

    public static String normalize(final String query) {
        return PMID_PATTERN.matcher(query).replaceAll(REPLACEMENT);
    }
}
