package edu.stanford.irt.laneweb.eresources;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Detect ORCIDs in queries and quote them to improve search precision.
 *
 * @author ryanmax
 */
public final class OrcidQueryInspector implements QueryInspector {

    private static final int ORCID_MAX_LENGTH = 19;

    private static final Pattern ORCID_PATTERN = Pattern.compile("(\\b(?:\\d{4}\\-){3,}\\d{3}[\\dXx]\\b)");

    @Override
    public boolean combinable() {
        return true;
    }

    @Override
    public String inspect(final String query) {
        Matcher m = ORCID_PATTERN.matcher(query);
        if (m.find() && ORCID_MAX_LENGTH == m.group(1).length()) {
            return m.replaceFirst("\"$1\"");
        }
        return query;
    }
}
