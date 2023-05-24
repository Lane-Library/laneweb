package edu.stanford.irt.laneweb.eresources;

import java.util.regex.Pattern;

/**
 * Users sometimes prefix doi searches with variants of http://dx.doi.org/; extract a single DOI and search it against
 * the Solr "dois" index. Not combinable.
 */
public final class SingleDoiQueryInspector extends DoiQueryInspector {

    private static final Pattern DOI_PATTERN = Pattern.compile("^(10\\.\\d+[^ \\?]+)$");

    private static final Pattern PREFIX_PATTERN = Pattern.compile("\\b(?:doi: ?|[^ ]*doi[^ ]*/)(10\\.\\w+)\\b",
            Pattern.CASE_INSENSITIVE);

    @Override
    public boolean combinable() {
        return false;
    }

    @Override
    public String inspect(final String query) {
        String parsed = PREFIX_PATTERN.matcher(query).replaceAll("$1");
        if (DOI_PATTERN.matcher(parsed).find()) {
            parsed = DOI_PATTERN.matcher(parsed).replaceFirst(":::$1###");
            parsed = TMP_PATTERN.matcher(parsed).replaceFirst("dois:" + QUOTE + "$1" + QUOTE);
            parsed = EPUB_PATTERN.matcher(parsed).replaceAll("");
            return parsed;
        }
        return query;
    }
}
