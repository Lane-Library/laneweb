package edu.stanford.irt.laneweb.eresources;

import java.util.regex.Pattern;

/**
 * Users sometimes prefix doi searches with variants of http://dx.doi.org/; extract doi and search it alone as a phrase
 *
 * @author ryanmax
 */
public final class DoiQueryInspector implements QueryInspector {

    private static final Pattern DOI_PATTERN = Pattern.compile("(\\b10\\.\\d+[^ ]+\\b)");

    private static final Pattern EPUB_PATTERN = Pattern.compile("\\bepub\\b", Pattern.CASE_INSENSITIVE);

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
            parsed = DOI_PATTERN.matcher(parsed).replaceFirst(":::$1###");
            parsed = parsed.replaceFirst(":::(.*)###\\.?", QUOTE + "$1" + QUOTE);
            parsed = EPUB_PATTERN.matcher(parsed).replaceAll("");
        }
        return parsed;
    }
}
