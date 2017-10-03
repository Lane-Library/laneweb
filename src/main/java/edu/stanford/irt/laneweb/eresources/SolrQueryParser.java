package edu.stanford.irt.laneweb.eresources;

import java.util.List;

public final class SolrQueryParser {

    private List<QueryInspector> inspectors;

    public SolrQueryParser(final List<QueryInspector> parsers) {
        this.inspectors = parsers;
    }

    public String parse(final String query) {
        String parsedQuery = query;
        for (QueryInspector parser : this.inspectors) {
            if (!parser.combinable()) {
                parsedQuery = parser.inspect(query);
                if (!query.equals(parsedQuery)) {
                    return parsedQuery;
                }
            }
        }
        for (QueryInspector parser : this.inspectors) {
            if (parser.combinable()) {
                parsedQuery = parser.inspect(parsedQuery);
            }
        }
        return parsedQuery;
    }
}
