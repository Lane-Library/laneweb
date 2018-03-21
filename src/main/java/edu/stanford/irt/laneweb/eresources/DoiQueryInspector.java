package edu.stanford.irt.laneweb.eresources;

import java.util.regex.Pattern;

/**
 * Users sometimes prefix doi searches with variants of http://dx.doi.org/; extract doi and search it alone as a phrase
 *
 * @author ryanmax
 */
public final class DoiQueryInspector implements QueryInspector {

    private static final Pattern DOI_PATTERN = Pattern.compile("(\\b10\\.\\d+[^ ]+\\b)");

    private static final String QUOTE = "\"";

    private static final Pattern REPLACED_PATTERN = Pattern.compile(".*:::(.*)###.*");

    @Override
    public boolean combinable() {
        return true;
    }

    @Override
    public String inspect(final String query) {
        String parsed = query;
        if (DOI_PATTERN.matcher(parsed).find()) {
            parsed = DOI_PATTERN.matcher(parsed).replaceFirst(":::$1###");
            parsed = REPLACED_PATTERN.matcher(parsed).replaceFirst(QUOTE + "$1" + QUOTE);
        }
        return parsed;
    }
}
