package edu.stanford.irt.laneweb.eresources;

import java.util.regex.Pattern;

/**
 * Inspect for PMC queries
 *
 * @author ryanmax
 */
public final class PmcQueryInspector implements QueryInspector {

    // PMCID: PMC1234567
    // PMCID:PMC1234567
    // PMC1234567
    private static final Pattern PMC_PATTERN = Pattern.compile("\\b(?:pubmed central )?(?:pmcid: ?)?(PMC\\d{1,8})(?:\\.|\\b)",
            Pattern.CASE_INSENSITIVE);

    // https://www.ncbi.nlm.nih.gov/pmc/articles/PMC1496744/
    private static final Pattern PMC_URL_PATTERN = Pattern
            .compile("\\bhttps?://[a-z\\.\\-/]+/pmc/articles/(PMC\\d{1,8})(?:/|\\b)", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean combinable() {
        return true;
    }

    @Override
    public String inspect(final String query) {
        String q = PMC_URL_PATTERN.matcher(query).replaceAll(" $1 ");
        return PMC_PATTERN.matcher(q).replaceAll("\"$1\"").trim();
    }
}
