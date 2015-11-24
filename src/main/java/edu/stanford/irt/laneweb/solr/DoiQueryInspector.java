package edu.stanford.irt.laneweb.solr;

import java.util.regex.Pattern;

/**
 * Users sometimes prefix doi searches with variants of http://dx.doi.org/; inspect and clean accordingly
 *
 * @author ryanmax
 */
public final class DoiQueryInspector implements QueryInspector {

    private static final Pattern DOI_PATTERN = Pattern.compile("(?:http:\\/\\/)?(?:dx\\.)?doi\\.org/(10\\.\\w+)\\b",
            Pattern.CASE_INSENSITIVE);

    @Override
    public String inspect(final String query) {
        return DOI_PATTERN.matcher(query).replaceAll("$1");
    }
}