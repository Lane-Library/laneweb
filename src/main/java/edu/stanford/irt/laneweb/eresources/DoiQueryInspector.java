package edu.stanford.irt.laneweb.eresources;

import java.util.regex.Pattern;

/**
 * Users sometimes prefix doi searches with variants of http://dx.doi.org/; inspect and clean accordingly
 *
 * @author ryanmax
 */
public final class DoiQueryInspector implements QueryInspector {

    private static final Pattern DOI_PATTERN = Pattern.compile("\\b10\\.\\d+\\b");

    private static final Pattern PREFIX_PATTERN = Pattern.compile("(?:https?:\\/\\/)?(?:dx\\.)?doi\\.org/(10\\.\\w+)\\b",
            Pattern.CASE_INSENSITIVE);

    @Override
    public boolean combinable() {
        return true;
    }

    @Override
    public String inspect(final String query) {
        String parsed = PREFIX_PATTERN.matcher(query).replaceAll("$1");
        // remove slashes in DOIs because they give edismax parser trouble
        if (DOI_PATTERN.matcher(parsed).find()) {
            parsed = parsed.replace('/', ' ');
        }
        return parsed;
    }
}