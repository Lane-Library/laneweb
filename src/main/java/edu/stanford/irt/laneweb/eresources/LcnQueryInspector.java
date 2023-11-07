package edu.stanford.irt.laneweb.eresources;

import java.util.regex.Pattern;

/**
 * Inspect for Lane control number queries
 *
 * @author ryanmax
 */
public final class LcnQueryInspector implements QueryInspector {

    private static final Pattern PATTERN = Pattern.compile("\\bbibid ?:? ?l?(\\d{1,8})\\b", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean combinable() {
        return false;
    }

    @Override
    public String inspect(final String query) {
        return PATTERN.matcher(query).replaceAll("id:bib-$1");
    }
}
