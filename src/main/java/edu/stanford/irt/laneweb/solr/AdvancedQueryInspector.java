package edu.stanford.irt.laneweb.solr;

/**
 * Toggle off query parsing so user can send query directly to solr
 *
 * @author ryanmax
 */
public final class AdvancedQueryInspector implements QueryInspector {

    private static final String TOGGLE_OFF = "advanced:true";

    @Override
    public String inspect(final String query) {
        if (query.contains(TOGGLE_OFF)) {
            return query.replace(TOGGLE_OFF, "").trim();
        }
        return query;
    }
}