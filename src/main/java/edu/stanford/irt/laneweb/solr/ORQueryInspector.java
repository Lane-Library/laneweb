package edu.stanford.irt.laneweb.solr;

/**
 * Boolean OR searches are problematic using edismax parser in Solr 5.5+; more info here:
 * https://issues.apache.org/jira/browse/SOLR-8812
 *
 * @author ryanmax
 */
public final class ORQueryInspector implements QueryInspector {

    @Override
    public String inspect(final String query) {
        if (query.contains(" OR ")) {
            StringBuilder sb = new StringBuilder();
            sb.append('(');
            sb.append(query);
            sb.append(") OR (");
            sb.append(query);
            sb.append(')');
            return sb.toString();
        }
        return query;
    }
}