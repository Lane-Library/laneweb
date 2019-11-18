package edu.stanford.irt.laneweb.eresources;

import java.util.regex.Pattern;

/**
 * Detect ORCIDs in queries and quote them to improve search precision.
 *
 * @author ryanmax
 */
public final class OrcidQueryInspector implements QueryInspector {

    private static final Pattern ORCID_PATTERN = Pattern.compile("(\\b(?:\\d{4}\\-){3}\\d{3}[01Xx]\\b)");

    @Override
    public boolean combinable() {
        return true;
    }

    @Override
    public String inspect(final String query) {
        String parsed = query;
        if (ORCID_PATTERN.matcher(parsed).find()) {
            parsed = ORCID_PATTERN.matcher(parsed).replaceFirst("\"$1\"");
        }
        return parsed;
    }
}
