package edu.stanford.irt.laneweb.eresources;

import java.util.regex.Pattern;

/**
 * Assume simple numeric queries are document ID searches
 *
 * @author ryanmax
 */
public final class NumberQueryInspector implements QueryInspector {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("^(\\d{1,10})$");

    @Override
    public boolean combinable() {
        return false;
    }

    @Override
    public String inspect(final String query) {
        return NUMBER_PATTERN.matcher(query).replaceFirst("recordId:$1");
    }
}
