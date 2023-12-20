package edu.stanford.irt.laneweb.eresources;

import java.util.regex.Pattern;

/**
 * Inspect for pmid queries
 *
 * @author ryanmax
 */
public final class PmidQueryInspector implements QueryInspector {

    private static final Pattern PMID_PATTERN = Pattern
            .compile("(?:pmid|pubmed|pubmed pmid|pubmed ?id) ?[ :#]? ?(\\d{1,8})\\b", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean combinable() {
        return true;
    }

    @Override
    public String inspect(final String query) {
        return PMID_PATTERN.matcher(query).replaceAll("pmid:$1");
    }
}
