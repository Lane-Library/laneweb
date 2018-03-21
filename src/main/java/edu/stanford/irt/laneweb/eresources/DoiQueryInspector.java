package edu.stanford.irt.laneweb.eresources;

import java.util.regex.Pattern;

/**
 * Users sometimes prefix doi searches with variants of http://dx.doi.org/; inspect and clean accordingly
 *
 * @author ryanmax
 */
public final class DoiQueryInspector implements QueryInspector {

    private static final Pattern DOI_PATTERN = Pattern.compile("(\\b10\\.\\d+[^ ]+\\b)");

    private static final Pattern PREFIX_PATTERN = Pattern
            .compile("(?:https?:\\/\\/)?(?:dx\\.)?doi\\.org/(10\\.\\w+)\\b", Pattern.CASE_INSENSITIVE);

    private static final String QUOTE = "\"";

    @Override
    public boolean combinable() {
        return true;
    }

    @Override
    public String inspect(final String query) {
        String parsed = PREFIX_PATTERN.matcher(query).replaceAll("$1");
        if (DOI_PATTERN.matcher(parsed).find()) {
            // if a DOI is found, strip it and quote it alone
            parsed = DOI_PATTERN.matcher(parsed).replaceFirst(":::$1###");
            parsed = parsed.replaceFirst(".*:::(.*)###.*", QUOTE + "$1" + QUOTE);
        }
        return parsed;
    }
}
